package com.tradesystem.order;

import com.tradesystem.buyer.Buyer;
import com.tradesystem.product.Product;
import com.tradesystem.product.ProductType;
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

    private String comment;

    private LocalDate date;

    private BigDecimal quantity;

    private BigDecimal sum;

    //@Column(columnDefinition = "varchar")
    //private ProductType productType;

    @ManyToOne
    @JoinColumn(name = "product_fk")
    private Product productType;

    @ManyToOne
    @JoinColumn(name = "buyer_fk")
    private Buyer buyer;

    @ManyToOne
    @JoinColumn(name = "supplier_fk")
    private Supplier supplier;

   /*


    public static void main(String[] args) {


        Map<ProductType, Double> assortmentPrices = new HashMap<>();
        assortmentPrices.put(ProductType.M_12_FRESH, 126.00);
        assortmentPrices.put(ProductType.M_5_FRESH, 300.00);
        assortmentPrices.put(ProductType.M_2_5_FRESH, 200.00);

        //Buyer buyer = new Buyer(1, "Jan", assortmentPrices);

        OrderService orderService = new OrderService();
        Map<ProductType, Double> orderedAssortments = new HashMap<>();
        orderedAssortments.put(ProductType.M_12_FRESH, 41.38);
        orderedAssortments.put(ProductType.M_2_5_FRESH, 40.00);
        orderedAssortments.put(ProductType.M_5_FRESH, 80.00);

        Order order = new Order(null, orderedAssortments);

        FinalOrder finalOrder = new FinalOrder();

        Map<ProductType, Map<Double, Double>> resultOrder = orderService.calculateBuyerOrder(order, buyer);


        resultOrder.forEach((assortment, v) -> v.forEach((quantity, sum) -> {
        System.out.println("Type: " + "\tQuantity: " + "\tSum: ");
            System.out.println(assortment + "\t\t" + quantity + "\t\t" + sum + "\n");
        }));

    }

    */
}
