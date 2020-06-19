package com.tradesystem.product;

import com.tradesystem.price.Price;
import com.tradesystem.price.pricehistory.PriceHistory;
import lombok.*;

import java.util.List;
import javax.persistence.*;

@Entity
//@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String product;

    @OneToMany(mappedBy = "product")
    private List<Price> prices;

    @OneToMany(mappedBy = "product")
    private List<PriceHistory> priceHistories;

}
