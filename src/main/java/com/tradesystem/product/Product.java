package com.tradesystem.product;

import com.tradesystem.price.Price;
import lombok.Data;
import java.util.List;
import javax.persistence.*;

@Entity
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    private String productType;

    @OneToMany(mappedBy = "productType")
    private List<Price> prices;


/*
    @OneToOne
    @JoinColumn(name = "price_id")
    private Price price;

    @ManyToOne
    @JoinColumn(name = "buyer_fk")
    private Buyer buyer;

    @ManyToOne
    @JoinColumn(name = "supplier_fk")
    private Supplier supplier;
    */
}
