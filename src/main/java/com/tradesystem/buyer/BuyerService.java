package com.tradesystem.buyer;

import com.tradesystem.invoice.Invoice;
import com.tradesystem.invoice.InvoiceDao;
import com.tradesystem.price.Price;
import com.tradesystem.price.PriceDao;
import com.tradesystem.product.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class BuyerService {

    private InvoiceDao invoiceDao;
    private BuyerDao buyerDao;
    private PriceDao priceDao;

    public BuyerService(InvoiceDao invoiceDao, BuyerDao buyerDao, PriceDao priceDao) {
        this.invoiceDao = invoiceDao;
        this.buyerDao = buyerDao;
        this.priceDao = priceDao;
    }


    @Transactional
    public Buyer createBuyer(BuyerDto buyerDto) {
        if (validateBuyer(buyerDto)) {
            Buyer buyer = Buyer.builder()
                    .name(buyerDto.getName())
                    .build();

            return buyerDao.save(buyer);
        }
        throw new RuntimeException("Nie można stworzyć kupca");
    }

    @Transactional
    public List<Buyer> getBalances() {
        List<Buyer> buyers = buyerDao.findAll();
        List<Buyer> resultBuers = new ArrayList<>();

        for (Buyer buyer : buyers) {
            buyer = setCurrentBalance(buyer);
            resultBuers.add(buyer);
        }

        return resultBuers;
    }

    @Transactional
    public List<Buyer> getAll() {
        return buyerDao.findAll();
    }

    @Transactional
    public List<Price> getBuyerProducts(Long id) {
        return priceDao.getBuyerProducts(id);
    }

    private Buyer setCurrentBalance(Buyer buyer) {
        List<Invoice> notUsedInvoices = invoiceDao.getBuyerNotUsedInvoices(buyer.getId());

        BigDecimal balance = BigDecimal.valueOf(0);

        for (Invoice invoice : notUsedInvoices) {
            balance = balance.add(invoice.getAmountToUse());
        }

        Optional<Invoice> negativeInvoice = invoiceDao.getBuyerNegativeInvoice(buyer.getId());
        if (negativeInvoice.isPresent()) {
            balance = balance.add(negativeInvoice.get().getAmountToUse());
        }

        buyer.setCurrentBalance(balance);

        return buyer;
    }

    public boolean validateBuyer(BuyerDto buyerDto) {
        if (buyerDto.getName() == null || buyerDto.getName().equals("")) {
            return false;
        }
        Buyer buyer = buyerDao.findByName(buyerDto.getName());
        if (buyer != null) {
            return false;
        }
        return true;
    }

}
