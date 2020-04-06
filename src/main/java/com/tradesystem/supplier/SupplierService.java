package com.tradesystem.supplier;

import com.tradesystem.invoice.Invoice;
import com.tradesystem.invoice.InvoiceDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SupplierService {

    private SupplierDao supplierDao;
    private InvoiceDao invoiceDao;


    public SupplierService(SupplierDao supplierDao, InvoiceDao invoiceDao) {
        this.supplierDao = supplierDao;
        this.invoiceDao = invoiceDao;
    }


    @Transactional
    public Supplier createBuyer(SupplierDto supplierDto) {
        Supplier supplier = Supplier.builder()
                .name(supplierDto.getName())
                .build();

        return supplierDao.save(supplier);
    }

    @Transactional
    public List<Supplier> getAll() {
        return supplierDao.findAll();
    }

    @Transactional
    public List<Supplier> getBalances() {
        List<Supplier> suppliers = supplierDao.findAll();
        List<Supplier> resultSuppliers = new ArrayList<>();

        for (Supplier supplier : suppliers) {
            supplier = setCurrentBalance(supplier);
            resultSuppliers.add(supplier);
        }

        return resultSuppliers;
    }

    private Supplier setCurrentBalance(Supplier supplier) {

        List<Invoice> notUsedInvoices = invoiceDao.getSupplierNotUsedInvoices(supplier.getId());

        BigDecimal balance = BigDecimal.valueOf(0);

        for (Invoice invoice : notUsedInvoices) {
            balance = balance.add(invoice.getAmountToUse());
        }

        Optional<Invoice> negativeInvoice = invoiceDao.getSupplierNegativeInvoice(supplier.getId());
        if (negativeInvoice.isPresent()) {
            balance = balance.add(negativeInvoice.get().getAmountToUse());
        }

        supplier.setCurrentBalance(balance);

        return supplier;
    }

}
