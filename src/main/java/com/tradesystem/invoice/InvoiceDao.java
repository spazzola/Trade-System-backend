package com.tradesystem.invoice;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("invoiceDao")
public interface InvoiceDao extends JpaRepository<Invoice, Long> {


    //TODO oddzielne metody na pobranie faktur dla buyera i suppliera
    @Query(value = "SELECT * FROM invoices " +
            "INNER JOIN buyers ON invoices.buyer_fk = buyers.buyer_id " +
            "WHERE invoices.buyer_fk = ?1 AND is_used = false",
            nativeQuery = true)
    List<Invoice> getBuyerNotUsedInvoices(Long buyerId);


    @Query(value = "SELECT * FROM invoices " +
            "INNER JOIN buyers ON invoices.buyer_fk = buyers.buyer_id " +
            "WHERE invoices.buyer_fk = ?1 AND amount_to_use < 0 AND is_used = false",
            nativeQuery = true)
    Optional<Invoice> getBuyerNegativeInvoice(Long buyerId);


    @Query(value = "SELECT * FROM invoices " +
            "INNER JOIN suppliers ON invoices.supplier_fk = suppliers.supplier_id " +
            "WHERE invoices.supplier_fk = ?1 AND is_used = false",
            nativeQuery = true)
    List<Invoice> getSupplierNotUsedInvoices(Long supplierId);


    @Query(value = "SELECT * FROM invoices " +
            "INNER JOIN suppliers ON invoices.supplier_fk = suppliers.supplier_id " +
            "WHERE invoices.supplier_fk = ?1 AND amount_to_use < 0 AND is_used = false",
            nativeQuery = true)
    Optional<Invoice> getSupplierNegativeInvoice(Long supplierId);


    @Query(value = "SELECT * FROM invoices " +
            "WHERE buyer_fk != null",
            nativeQuery = true)
    List<Invoice> getBuyersIncomedInvoices(int month, int year);
}
