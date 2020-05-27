package com.tradesystem.price;

import com.tradesystem.buyer.Buyer;
import com.tradesystem.product.Product;
import com.tradesystem.supplier.Supplier;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
//@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "prices")
public class Price {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "price_id")
    private Long id;

    private BigDecimal price;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_fk")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "buyer_fk")
    private Buyer buyer;

    @ManyToOne
    @JoinColumn(name = "supplier_fk")
    private Supplier supplier;


}
