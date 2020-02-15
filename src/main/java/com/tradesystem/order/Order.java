package com.tradesystem.order;

import com.tradesystem.buyer.Buyer;
import com.tradesystem.ordercomment.OrderComment;
import com.tradesystem.product.Product;
import com.tradesystem.supplier.Supplier;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;


@Entity
@Data
@Table(name = "orders")
public class Order {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    private LocalDate date;

    private BigDecimal quantity;

    private BigDecimal sum;

    @ManyToOne
    @JoinColumn(name = "product_fk")
    private Product productType;

    @ManyToOne
    @JoinColumn(name = "buyer_fk")
    private Buyer buyer;

    @ManyToOne
    @JoinColumn(name = "supplier_fk")
    private Supplier supplier;

    @OneToOne
    @JoinColumn(name = "order_comment_fk")
    private OrderComment orderComment;

}
