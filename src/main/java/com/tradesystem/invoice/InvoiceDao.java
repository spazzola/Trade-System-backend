package com.tradesystem.invoice;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("invoiceDao")
public interface InvoiceDao extends JpaRepository<Invoice, Long> {

    @Query(value = "SELECT * FROM invoices " +
            "WHERE MONTH(invoices.date) = ?1 AND YEAR(invoices.date) = ?2 AND value > 0",
            nativeQuery = true)
    List<Invoice> getMonthInvoices(int month, int year);


    @Query(value = "SELECT * FROM invoices " +
            "WHERE invoice_number = ?1 AND is_paid = false",
            nativeQuery = true)
    Invoice getByInvoiceNumber(String invoiceNumber);


    @Query(value = "SELECT * FROM invoices " +
            "WHERE buyer_fk = ?1 AND is_paid = false AND value > 0 AND to_equalize_negative_invoice != true",
            nativeQuery = true)
    Optional<List <Invoice>> getBuyerNotPaidInvoices(Long buyerId);


    @Query(value = "SELECT * FROM invoices " +
            "WHERE buyer_fk IS NOT NULL AND is_paid = false AND amount_to_use > 0 AND to_equalize_negative_invoice != true",
            nativeQuery = true)
    Optional<List <Invoice>> getBuyersNotPaidInvoices();

    /***
     *
     * ======================= BUYER =======================
     *
     */

    @Query(value = "SELECT * FROM invoices i " +
            "WHERE buyer_fk = ?1 AND MONTH(i.date) = ?2 AND YEAR(i.date) = ?3 AND value > 0",
            nativeQuery = true)
    List<Invoice> getBuyerMonthInvoices(Long buyerId, int month, int year);

    @Query(value = "SELECT * FROM invoices i " +
            "WHERE buyer_fk IS NOT NULL AND MONTH(i.date) = ?1 AND YEAR(i.date) = ?2 AND value > 0",
            nativeQuery = true)
    List<Invoice> getBuyersMonthInvoices(int month, int year);

    @Query(value = "SELECT * FROM invoices " +
            "INNER JOIN buyers ON invoices.buyer_fk = buyers.buyer_id " +
            "WHERE invoices.buyer_fk = ?1 AND is_used = false AND is_paid = true",
            nativeQuery = true)
    List<Invoice> getBuyerNotUsedInvoices(Long buyerId);


    @Query(value = "SELECT * FROM invoices " +
            "INNER JOIN buyers ON invoices.buyer_fk = buyers.buyer_id " +
            "WHERE invoices.buyer_fk = ?1 AND amount_to_use < 0 AND is_used = false",
            nativeQuery = true)
    Optional<Invoice> getBuyerNegativeInvoice(Long buyerId);


    @Query(value = "SELECT * FROM invoices " +
            "WHERE buyer_fk IS NOT null AND is_paid = true AND is_used = false AND amount_to_use > 0",
            nativeQuery = true)
    Optional<List <Invoice>> getBuyersPaidNotUsedPositiveInvoices();


    @Query(value = "SELECT * FROM invoices " +
            "WHERE buyer_fk IS NOT null AND is_used = false AND amount_to_use < 0",
            nativeQuery = true)
    Optional<List <Invoice>> getBuyersNegativeInvoices();

    @Query(value = "SELECT * FROM invoices " +
            "WHERE buyer_fk IS NOT null AND is_paid = false AND is_created_to_order = true AND to_equalize_negative_invoice = false",
            nativeQuery = true)
    Optional<List <Invoice>> getBuyersNotPaidInvoicesCreatedToOrder();

    @Query(value = "SELECT * FROM invoices " +
            "WHERE supplier_fk IS NOT null AND is_paid = true AND is_used = false AND amount_to_use > 0",
            nativeQuery = true)
    Optional<List <Invoice>> getSuppliersPaidNotUsedPositiveInvoices();


    @Query(value = "SELECT * FROM invoices " +
            "WHERE supplier_fk IS NOT null AND is_used = false AND amount_to_use < 0",
            nativeQuery = true)
    Optional<List <Invoice>> getSuppliersPaidNotUsedNegativeInvoices();

    // =====Month=====

    //TODO data
    @Query(value = "SELECT * FROM invoices " +
            "WHERE buyer_fk IS NOT null AND is_paid = true " +
            "AND MONTH(invoices.date) = ?1 AND YEAR(invoices.date) = ?2",
            nativeQuery = true)
    List<Invoice> getBuyersMonthIncomedInvoices(int month, int year);


    @Query(value = "SELECT * FROM invoices " +
            "INNER JOIN buyers ON invoices.buyer_fk = buyers.buyer_id " +
            "WHERE is_used = false AND amount_to_use > 0 AND is_paid = true " +
            "AND MONTH(invoices.date) = ?1 AND YEAR(invoices.date) = ?2",
            nativeQuery = true)
    Optional<List<Invoice>> getBuyersMonthNotUsedPositivesInvoices(int month, int year);


    @Query(value = "SELECT * FROM invoices " +
            "WHERE MONTH(invoices.date) = ?1 AND YEAR(invoices.date) = ?2 " +
            "AND is_used = false AND amount_to_use < 0",
            nativeQuery = true)
    Optional<List<Invoice>> getBuyersMonthNotUsedNegativeInvoices(int month, int year);


    @Query(value = "SELECT * FROM invoices " +
            "WHERE is_used = true AND buyer_fk IS NOT null " +
            "AND MONTH(invoices.date) = ?1 AND YEAR(invoices.date) = ?2",
            nativeQuery = true)
    List<Invoice> getBuyersMonthUsedInvoices(int month, int year);


     @Query(value = "SELECT * FROM invoices " +
             "INNER JOIN buyers ON invoices.buyer_fk = buyers.buyer_id " +
             "WHERE buyer_fk IS NOT null AND amount_to_use < 0 AND is_used = false " +
             "AND MONTH(invoices.date) = ?1 AND YEAR(invoices.date) = ?2",
             nativeQuery = true)
     Optional<List<Invoice>> getBuyersMonthNegativeInvoices(int month, int year);


    // =====Year=====


    @Query(value = "SELECT * FROM invoices " +
            "WHERE is_used = true AND buyer_fk IS NOT null " +
            "AND YEAR(invoices.date) = ?1",
            nativeQuery = true)
    List<Invoice> getBuyersYearUsedInvoices(int year);


    @Query(value = "SELECT * FROM invoices " +
            "INNER JOIN buyers ON invoices.buyer_fk = buyers.buyer_id " +
            "WHERE is_used = false AND amount_to_use > 0 AND is_paid = true " +
            "AND YEAR(invoices.date) = ?1",
            nativeQuery = true)
    Optional<List<Invoice>> getBuyersYearNotUsedPositivesInvoices(int year);


    @Query(value = "SELECT * FROM invoices " +
            "WHERE is_used = false AND amount_to_use < 0" +
            "AND YEAR(invoices.date) = ?1",
            nativeQuery = true)
    Optional<List<Invoice>> getBuyersYearNotUsedNegativeInvoices(int year);


    @Query(value = "SELECT * FROM invoices " +
            "WHERE buyer_fk IS NOT null AND is_paid = true " +
            "AND YEAR(invoices.date) = ?1 AND is_paid = true",
            nativeQuery = true)
    List<Invoice> getBuyersYearIncomedInvoices(int year);


    @Query(value = "SELECT * FROM invoices " +
            "INNER JOIN buyers ON invoices.buyer_fk = buyers.buyer_id " +
            "WHERE buyer_fk IS NOT null AND amount_to_use < 0 AND is_used = false " +
            "AND YEAR(invoices.date) = ?1",
            nativeQuery = true)
    Optional<List<Invoice>> getBuyersYearNegativeInvoices(int year);




    /***
     *
     * ======================= SUPPLIER =======================
     *
     */


    @Query(value = "SELECT * FROM invoices " +
            "INNER JOIN suppliers ON invoices.supplier_fk = suppliers.supplier_id " +
            "WHERE invoices.supplier_fk = ?1 AND is_used = false AND is_paid = true",
            nativeQuery = true)
    List<Invoice> getSupplierNotUsedInvoices(Long supplierId);


    @Query(value = "SELECT * FROM invoices " +
            "INNER JOIN suppliers ON invoices.supplier_fk = suppliers.supplier_id " +
            "WHERE invoices.supplier_fk = ?1 AND amount_to_use < 0 AND is_used = false",
            nativeQuery = true)
    Optional<Invoice> getSupplierNegativeInvoice(Long supplierId);


    // =====Month=====


    @Query(value = "SELECT * FROM invoices i " +
            "WHERE supplier_fk = ?1 AND MONTH(i.date) = ?2 AND YEAR(i.date) = ?3 AND value > 0",
            nativeQuery = true)
    List<Invoice> getSupplierMonthInvoices(Long supplierId, int month, int year);


    @Query(value = "SELECT * FROM invoices i " +
            "WHERE supplier_fk IS NOT NULL AND MONTH(i.date) = ?1 AND YEAR(i.date) = ?2 AND value > 0",
            nativeQuery = true)
    List<Invoice> getSuppliersMonthInvoices(int month, int year);


    @Query(value = "SELECT * FROM invoices " +
            "INNER JOIN suppliers ON invoices.supplier_fk = suppliers.supplier_id " +
            "WHERE is_used = false AND amount_to_use > 0 " +
            "AND MONTH(invoices.date) = ?1 AND YEAR(invoices.date) = ?2 " +
            "AND is_paid = true",
            nativeQuery = true)
    Optional<List<Invoice>> getSuppliersMonthNotUsedInvoices(int month, int year);


    // =====Year=====

    @Query(value = "SELECT * FROM invoices " +
            "INNER JOIN suppliers ON invoices.supplier_fk = suppliers.supplier_id " +
            "WHERE is_used = false AND amount_to_use > 0 AND supplier_fk IS NOT null " +
            "AND YEAR(invoices.date) = ?1 AND is_paid = true",
            nativeQuery = true)
    Optional<List<Invoice>> getSuppliersYearNotUsedInvoices(int year);


    @Query(value = "SELECT * FROM invoices " +
            "WHERE YEAR(invoices.date) = ?1 AND is_paid = true " +
            "AND supplier_fk IS NOT null",
            nativeQuery = true)
    List<Invoice> getSuppliersYearInvoices(int year);

}
