package com.tradesystem.price;

import com.tradesystem.buyer.Buyer;
import com.tradesystem.product.Product;
import com.tradesystem.supplier.Supplier;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@Table(name = "prices")
public class Price {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "price_id")
    private Long id;

    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "product_fk")
    private Product productType;

    @ManyToOne
    @JoinColumn(name = "buyer_fk")
    private Buyer buyer;

    @ManyToOne
    @JoinColumn(name = "supplier_fk")
    private Supplier supplier;


}
