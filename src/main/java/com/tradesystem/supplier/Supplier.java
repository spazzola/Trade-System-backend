package com.tradesystem.supplier;

import com.tradesystem.Assortments;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class Supplier {

    private int id;
    private int totalPool;
    private String name;
    private Map<Assortments, Double> assortmentPrices;


    public Supplier(int id, int totalPool, String name, Map<Assortments, Double> assortmentPrices) {
        this.id = id;
        this.totalPool = totalPool;
        this.name = name;
        this.assortmentPrices = assortmentPrices;
    }

}
