package com.tradesystem.price;

import com.tradesystem.price.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

import java.math.BigDecimal;

@Repository("priceDao")
public interface PriceDao extends JpaRepository<Price, Long> {


    @Query(value = "SELECT price FROM prices " +
            "INNER JOIN product prod ON prices.product_fk = prod.product_id " +
            "WHERE prices.buyer_fk = :buyerId AND prices.product_fk = :productId",
            nativeQuery = true)
    BigDecimal getBuyerPrice(@Param("buyerId") Long buyerId,
                             @Param("productId") Long productId);


    @Query(value = "SELECT price FROM prices " +
            "INNER JOIN product prod ON prices.product_fk = prod.product_id " +
            "WHERE prices.supplier_fk = :supplierId AND prices.product_fk = :productId",
            nativeQuery = true)
    BigDecimal getSupplierPrice(@Param("supplierId") Long supplierId,
                             @Param("productId") Long productId);
}
