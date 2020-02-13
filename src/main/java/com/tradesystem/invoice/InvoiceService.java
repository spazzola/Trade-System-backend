package com.tradesystem.invoice;

import com.tradesystem.order.OrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;


@Service
public class InvoiceService {

    @Autowired
    private InvoiceDao invoiceDao;

    @Autowired
    private OrderDao orderDao;


    public void addBuyerInvoice(Invoice invoice) {

        Long buyerId = invoice.getBuyer().getId();
        Optional<Invoice> negativeInvoice = invoiceDao.getBuyerNegativeInvoice(buyerId);

        if (negativeInvoice.isPresent()) {
            negativeInvoice.get().setUsed(true);
            invoiceDao.save(negativeInvoice.get());

            //TODO rozbic na mniejsza metode? problem z Optionalem
            BigDecimal negativeAmount = negativeInvoice.get().getAmountToUse();
            BigDecimal newAmount = invoice.getAmountToUse().add(negativeAmount);
            BigDecimal different = BigDecimal.valueOf(0.0);

            if (newAmount.compareTo(BigDecimal.ZERO) > 0) {
                different = negativeAmount;
            }
            if (newAmount.compareTo(BigDecimal.ZERO) < 0) {
                different = negativeAmount.subtract(newAmount);

            }

            if (newAmount.compareTo(BigDecimal.ZERO) < 0) {
                invoice.setComment("Pomniejszono o " + different + " z faktury o id " + negativeInvoice.get().getId());
            }
            if (newAmount.compareTo(BigDecimal.ZERO) >= 0) {
                invoice.setComment("Pomniejszono o " + negativeAmount + " z faktury o id " + negativeInvoice.get().getId());
            }

            saveInvoice(invoice, newAmount, invoice.getValue());

        } else {
            invoiceDao.save(invoice);
        }
    }


    public void addSupplierInvoice(Invoice invoice) {
        Long supplierId = invoice.getSupplier().getId();
        Optional<Invoice> negativeInvoice = invoiceDao.getSupplierNegativeInvoice(supplierId);

        if (negativeInvoice.isPresent()) {
            negativeInvoice.get().setUsed(true);
            invoiceDao.save(negativeInvoice.get());


            BigDecimal negativeAmount = negativeInvoice.get().getAmountToUse();
            BigDecimal newAmount = invoice.getAmountToUse().add(negativeAmount);
            BigDecimal different = BigDecimal.valueOf(0.0);

            if (newAmount.compareTo(BigDecimal.ZERO) > 0) {
                different = negativeAmount;
            }
            if (newAmount.compareTo(BigDecimal.ZERO) < 0) {
                different = negativeAmount.subtract(newAmount);

            }

            if (newAmount.compareTo(BigDecimal.ZERO) < 0) {
                invoice.setComment("Pomniejszono o " + different + " z faktury o id " + negativeInvoice.get().getId());
            }
            if (newAmount.compareTo(BigDecimal.ZERO) >= 0) {
                invoice.setComment("Pomniejszono o " + negativeAmount + " z faktury o id " + negativeInvoice.get().getId());
            }

            saveInvoice(invoice, newAmount, invoice.getValue());

        } else {
            invoiceDao.save(invoice);
        }
    }

    private void saveInvoice(Invoice invoice, BigDecimal newAmount, BigDecimal value) {
        invoice.setAmountToUse(newAmount);
        invoice.setValue(invoice.getValue());
        invoiceDao.save(invoice);
    }

}
