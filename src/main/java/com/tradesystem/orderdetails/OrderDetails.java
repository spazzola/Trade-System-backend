package com.tradesystem.orderdetails;

import com.tradesystem.buyer.Buyer;
import com.tradesystem.order.Order;
import com.tradesystem.ordercomment.OrderComment;
import com.tradesystem.product.Product;
import com.tradesystem.supplier.Supplier;
import lombok.Data;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@Table(name = "order_details")
public class OrderDetails {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_details_id")
    private Long id;

    private BigDecimal quantity;

    private BigDecimal buyerSum;

    private BigDecimal supplierSum;

    @ManyToOne(cascade=CascadeType.PERSIST)
    @JoinColumn(name = "order_fk")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_fk")
    private Product product;

    @OneToOne
    @JoinColumn(name = "order_comment_fk")
    private OrderComment orderComment;

}
