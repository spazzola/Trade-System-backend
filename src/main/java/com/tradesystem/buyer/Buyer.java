package com.tradesystem.buyer;


import com.tradesystem.Assortments;
import com.tradesystem.order.Order;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class Buyer {

    private int id;
    private double totalPool;
    private String name;
    private Order order;
    private Map<Assortments, Double> assortmentPrices;


    public Buyer(int id, int totalPool, String name, Map<Assortments, Double> assortmentPrices) {
        this.id = id;
        this.totalPool = totalPool;
        this.name = name;
        this.assortmentPrices = assortmentPrices;
    }

}
