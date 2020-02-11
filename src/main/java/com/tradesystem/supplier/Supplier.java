package com.tradesystem.supplier;

import java.util.List;

import com.tradesystem.invoice.Invoice;
import com.tradesystem.price.Price;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "suppliers")
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "supplier_id")
    private Long id;

    private String name;

    //@OneToMany(mappedBy = "supplier")
    //private List<Product> products;

    //@OneToOne(mappedBy = "supplier", fetch = FetchType.EAGER)
    //private Price prices;

    @OneToMany(mappedBy = "supplier")
    private List<Price> prices;

    @OneToMany(mappedBy = "supplier")
    private List<Invoice> invoices;
}
