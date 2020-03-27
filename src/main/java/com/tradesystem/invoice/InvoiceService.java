package com.tradesystem.invoice;

import com.tradesystem.buyer.Buyer;
import com.tradesystem.orderdetails.OrderDetailsDao;
import com.tradesystem.supplier.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.List;


@Service
public class InvoiceService {

    @Autowired
    private InvoiceDao invoiceDao;

    @Autowired
    private OrderDetailsDao orderDetailsDao;


    public void addBuyerInvoice(Invoice invoice) {

        Long buyerId = invoice.getBuyer().getId();
        Optional<Invoice> negativeInvoice = invoiceDao.getBuyerNegativeInvoice(buyerId);

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

    public void trasnferInvoicesToNextMonth(LocalDate localDate) {
       createSupplierPositiveInvoice(localDate);
       createBuyerPositiveInvoice(localDate);
    }

    private void createSupplierPositiveInvoice(LocalDate localDate) {
        int month = localDate.getMonthValue();
        int year = localDate.getYear();
        List<Invoice> invoices = invoiceDao.getSuppliersMonthNotUsedInvoices(month, year);

        for (Invoice oldInvoice : invoices) {
            BigDecimal amountToUse = oldInvoice.getAmountToUse();
            String invoiceNumber = oldInvoice.getInvoiceNumber();
            Supplier supplier = oldInvoice.getSupplier();
            LocalDate date = oldInvoice.getDate();
            oldInvoice.setUsed(true);
            invoiceDao.save(oldInvoice);

            Invoice newInvoice = createInvoice(amountToUse, invoiceNumber, date);
            newInvoice.setSupplier(supplier);
            invoiceDao.save(newInvoice);
        }
    }


    private void createBuyerPositiveInvoice(LocalDate localDate) {
        int month = localDate.getMonthValue();
        int year = localDate.getYear();
        List<Invoice> invoices = invoiceDao.getBuyersMonthNotUsedPositivesInvoices(month, year);

        for (Invoice oldInvoice : invoices) {
            BigDecimal amountToUse = oldInvoice.getAmountToUse();
            String invoiceNumber = oldInvoice.getInvoiceNumber();
            Buyer buyer = oldInvoice.getBuyer();
            LocalDate date = oldInvoice.getDate();
            oldInvoice.setUsed(true);
            invoiceDao.save(oldInvoice);

            Invoice newInvoice = createInvoice(amountToUse, invoiceNumber, date);
            newInvoice.setBuyer(buyer);
            invoiceDao.save(newInvoice);
        }
    }

    private Invoice createInvoice(BigDecimal amountToUse, String invoiceNumber, LocalDate date) {
        Invoice invoice = new Invoice();
        invoice.setAmountToUse(amountToUse);
        invoice.setValue(amountToUse);
        invoice.setComment("Przeniesiono z FV o nr: " + invoiceNumber);
        invoice.setDate(date.plusMonths(1));

        return invoice;
    }

}
