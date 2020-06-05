package com.tradesystem.invoice;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UpdateInvoiceService {

    private InvoiceDao invoiceDao;

    public UpdateInvoiceService(InvoiceDao invoiceDao) {
        this.invoiceDao = invoiceDao;
    }


    @Transactional
    public Invoice updateInvoice(UpdateInvoiceRequest updateInvoiceRequest) {
        Invoice invoice = invoiceDao.getByInvoiceNumber(updateInvoiceRequest.getOldInvoiceNumber());

        invoice.setValue(updateInvoiceRequest.getNewValue());
        invoice.setAmountToUse(updateInvoiceRequest.getNewValue());
        invoice.setDate(updateInvoiceRequest.getNewDate());
        invoice.setInvoiceNumber(updateInvoiceRequest.getNewInvoiceNumber());

        return invoiceDao.save(invoice);
    }
}
