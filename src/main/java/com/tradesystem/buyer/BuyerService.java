package com.tradesystem.buyer;

import com.tradesystem.invoice.Invoice;
import com.tradesystem.invoice.InvoiceDao;
import com.tradesystem.price.PriceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class BuyerService {



    @Autowired
    private InvoiceDao invoiceDao;

    @Autowired
    private BuyerDao buyerDao;


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

    private Buyer setCurrentBalance(Buyer buyer) {
        //get notUsed invoices
        List<Invoice> notUsedInvoices = invoiceDao.getBuyerNotUsedInvoices(buyer.getId());

        BigDecimal balance = BigDecimal.valueOf(0);
        //get amount from them
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
