package com.tradesystem.buyer;

import com.tradesystem.invoice.Invoice;
import com.tradesystem.invoice.InvoiceDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class BuyerService {

    private InvoiceDao invoiceDao;
    private BuyerDao buyerDao;

    public BuyerService(InvoiceDao invoiceDao, BuyerDao buyerDao) {
        this.invoiceDao = invoiceDao;
        this.buyerDao = buyerDao;
    }


    @Transactional
    public Buyer createBuyer(BuyerDto buyerDto) {
        Buyer buyer = Buyer.builder()
                        .name(buyerDto.getName())
                        .build();

        return buyerDao.save(buyer);
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

}
