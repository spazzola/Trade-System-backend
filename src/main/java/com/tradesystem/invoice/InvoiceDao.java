package com.tradesystem.invoice;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("invoiceDao")
public interface InvoiceDao extends JpaRepository<Invoice, Long> {


    /***
     *
     * ======================= BUYER =======================
     *
     */

    //TODO oddzielne metody na pobranie faktur dla buyera i suppliera + DODAĆ DATY !!!
    //TODO do pobierania faktur do zaplaty za zamowienie nalezy pobrac fv ktore sa zaplacone isPaid = true
    //TODO usprawnic system aby przy rozliczaniu za order bral pod uwage tylko faktury z isPaid = true, jesli jest na false
    //TODO to wtedy generuje negative invoice

    @Query(value = "SELECT * FROM invoices " +
            "INNER JOIN buyers ON invoices.buyer_fk = buyers.buyer_id " +
            "WHERE invoices.buyer_fk = ?1 AND is_used = false AND is_paid = true",
            nativeQuery = true)
    List<Invoice> getBuyerNotUsedInvoices(Long buyerId);
    //^ dodac where = isPaid = true

    @Query(value = "SELECT * FROM invoices " +
            "INNER JOIN buyers ON invoices.buyer_fk = buyers.buyer_id " +
            "WHERE invoices.buyer_fk = ?1 AND amount_to_use < 0 AND is_used = false",
            nativeQuery = true)
    Optional<Invoice> getBuyerNegativeInvoice(Long buyerId);


    // =====Month=====

    //TODO data
    @Query(value = "SELECT * FROM invoices " +
            "WHERE buyer_fk IS NOT null AND is_paid = true " +
            "AND MONTH(invoices.date) = ?1 AND YEAR(invoices.date) = ?2",
            nativeQuery = true)
    List<Invoice> getBuyersMonthIncomedInvoices(int month, int year);


    @Query(value = "SELECT * FROM invoices " +
            "INNER JOIN buyers ON invoices.buyer_fk = buyers.buyer_id " +
            "WHERE is_used = false AND amount_to_use > 0 AND is_paid = true",
            nativeQuery = true)
    List<Invoice> getBuyersMonthNotUsedPositivesInvoices(int month, int year);
    //^ dodac where = isPaid = true

    @Query(value = "SELECT * FROM invoices " +
            "WHERE MONTH(invoices.date) = ?1 AND YEAR(invoices.date) = ?2 " +
            "AND is_used = false AND amount_to_use < 0",
            nativeQuery = true)
    Optional<List<Invoice>> getBuyersMonthNotUsedNegativeInvoices(int month, int year);


    //Co w przypadku jesli bedzie mial uzyte z ktorej fsaktury np tylko 30%? dodac warunek
    //albo wziac wszystkie faktury, od ich ogolnej wartosci odjac ta wartosc ktora zostala do uzycia
    //wynikiem bedzie uzyta kwota ktora jest szukana
    @Query(value = "SELECT * FROM invoices " +
            "WHERE is_used = true AND buyer_fk IS NOT null",
            nativeQuery = true)
    List<Invoice> getBuyersMonthUsedInvoices(int month, int year);
    //dodac daty^

     /*
        @Query(value = "SELECT * FROM invoices " +
                "INNER JOIN buyers ON invoices.buyer_fk = buyers.buyer_id " +
                "WHERE MONTH(invoices.date) = ?1 AND YEAR(invoices.date) = ?2 " +
                "AND is_used = false",
                nativeQuery = true)
        Optional<List <Invoice>> getBuyersNotUsedInvoices(int month, int year);
    */



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
    List<Invoice> getBuyersYearNotUsedPositivesInvoices(int year);

    @Query(value = "SELECT * FROM invoices " +
            "WHERE MONTH(invoices.date) = ?1 AND YEAR(invoices.date) = ?2 " +
            "AND is_used = false AND amount_to_use < 0" +
            "AND YEAR(invoices.date) = ?1",
            nativeQuery = true)
    Optional<List<Invoice>> getBuyersYearNotUsedNegativeInvoices(int year);


    @Query(value = "SELECT * FROM invoices " +
            "WHERE buyer_fk IS NOT null AND is_paid = true " +
            "AND YEAR(invoices.date) = ?1 AND is_paid = true",
            nativeQuery = true)
    List<Invoice> getBuyersYearIncomedInvoices(int year);


    /***
     *
     * ======================= SUPPLIER =======================
     *
     */


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


    // =====Month=====

    @Query(value = "SELECT * FROM invoices " +
            "WHERE supplier_fk IS NOT NULL " +
            "AND MONTH(invoices.date) = ?1 AND YEAR(invoices.date) = ?2 ",
            nativeQuery = true)
    List<Invoice> getSuppliersMonthInvoices(int month, int year);


    @Query(value = "SELECT * FROM invoices " +
            "INNER JOIN suppliers ON invoices.supplier_fk = suppliers.supplier_id " +
            "WHERE is_used = false AND amount_to_use > 0 " +
            "AND MONTH(invoices.date) = ?1 AND YEAR(invoices.date) = ?2",
            nativeQuery = true)
    List<Invoice> getSuppliersMonthNotUsedInvoices(int month, int year);


    @Query(value = "SELECT * FROM invoices " +
            "WHERE is_used = true AND supplier_fk IS NOT null " +
            "AND MONTH(invoices.date) = ?1 AND YEAR(invoices.date) = ?2",
            nativeQuery = true)
    List<Invoice> getSuppliersMonthUsedInvoices(int month, int year);



    // =====Year=====

    @Query(value = "SELECT * FROM invoices " +
            "WHERE is_used = true AND supplier_fk IS NOT null " +
            "AND YEAR(invoices.date) = ?1",
            nativeQuery = true)
    List<Invoice> getSuppliersYearUsedInvoices(int year);

    @Query(value = "SELECT * FROM invoices " +
            "INNER JOIN suppliers ON invoices.supplier_fk = suppliers.supplier_id " +
            "WHERE is_used = false AND amount_to_use > 0 " +
            "AND YEAR(invoices.date) = ?1",
            nativeQuery = true)
    List<Invoice> getSuppliersYearNotUsedInvoices(int year);

    @Query(value = "SELECT * FROM invoices " +
            "WHERE YEAR(invoices.date) = ?1",
            nativeQuery = true)
    List<Invoice> getSuppliersYearInvoices(int year);
}
