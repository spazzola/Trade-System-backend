package com.tradesystem.order;

import com.tradesystem.buyer.Buyer;
import com.tradesystem.orderdetails.OrderDetails;
import com.tradesystem.supplier.Supplier;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    private LocalDate date;

    @OneToMany(mappedBy = "order")
    private List<OrderDetails> orderDetails;

    @ManyToOne
    @JoinColumn(name = "buyer_fk")
    private Buyer buyer;

    @ManyToOne
    @JoinColumn(name = "supplier_fk")
    private Supplier supplier;

}
