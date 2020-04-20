package com.tradesystem.supplier;

import com.tradesystem.invoice.Invoice;
import com.tradesystem.invoice.InvoiceDao;
import com.tradesystem.price.Price;
import com.tradesystem.price.PriceDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SupplierService {

    private final SupplierDao supplierDao;
    private final InvoiceDao invoiceDao;
    private final PriceDao priceDao;


    public SupplierService(SupplierDao supplierDao, InvoiceDao invoiceDao, PriceDao priceDao) {
        this.supplierDao = supplierDao;
        this.invoiceDao = invoiceDao;
        this.priceDao = priceDao;
    }


    @Transactional
    public Supplier createSupplier(SupplierDto supplierDto) {
        if (validateSupplier(supplierDto)) {
            Supplier supplier = Supplier.builder()
                    .name(supplierDto.getName())
                    .build();

            return supplierDao.save(supplier);
        }
        throw new RuntimeException("Can't create supplier");
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

    @Transactional
    public List<Price> getSupplierProducts(Long id) {
        return priceDao.getSupplierProducts(id);
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

    private boolean validateSupplier(SupplierDto supplierDto) {
        if (supplierDto.getName() == null || supplierDto.getName().equals("")) {
            return false;
        }
        Supplier buyer = supplierDao.findByName(supplierDto.getName());
        if (buyer != null) {
            return false;
        }
        return true;
    }
}
