package com.tradesystem.order;

import com.tradesystem.Assortments;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class Order {

    private int id;
    private String information;
    private Map<Assortments, Double> orderedAssortments;


    public Order(int id, String information, Map<Assortments, Double> orderedAssortments) {
        this.id = id;
        this.information = information;
        this.orderedAssortments = orderedAssortments;
    }

}
