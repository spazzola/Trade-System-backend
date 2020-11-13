package com.tradesystem.price.pricehistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface PriceHistoryDao  extends JpaRepository<PriceHistory, Long> {

    List<PriceHistory> findByBuyerId(Long buyerId);

    @Query(value = "SELECT DISTINCT * FROM pricesHistory " +
            "WHERE buyer_fk = ?1 AND supplier_fk = ?2 AND product_id = ?3",
            nativeQuery = true)
    PriceHistory findPriceForBuyerSupplierAndProduct(Long buyerId, Long supplierId, Long productId);
}
