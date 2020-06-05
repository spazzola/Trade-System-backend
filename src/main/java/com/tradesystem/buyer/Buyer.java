package com.tradesystem.buyer;


import com.tradesystem.invoice.Invoice;
import com.tradesystem.order.Order;
import com.tradesystem.price.Price;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "buyers")
public class Buyer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "buyer_id")
    private Long id;

    private String name;

    private BigDecimal currentBalance;

    @Transient
    private BigDecimal averageProfitPerM3;

    @OneToMany(mappedBy = "buyer")
    private List<Price> prices;

    @OneToMany(mappedBy = "buyer")
    private List<Invoice> invoices;

    @OneToMany(mappedBy = "buyer")
    private List<Order> orders;
}
