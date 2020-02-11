package com.tradesystem.buyer;


import com.tradesystem.invoice.Invoice;
import com.tradesystem.order.Order;
import com.tradesystem.price.Price;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "buyers")
public class Buyer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "buyer_id")
    private Long id;

    private String name;

    //@OneToMany(mappedBy = "buyer")
    //private List<Product> products;

    @OneToMany(mappedBy = "buyer")
    private List<Price> prices;

    @OneToMany(mappedBy = "buyer")
    private List<Invoice> invoices;

    @OneToMany(mappedBy = "buyer")
    private List<Order> orders;




}
