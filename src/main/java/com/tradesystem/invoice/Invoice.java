package com.tradesystem.invoice;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Invoice {

    private int id;
    private String invoiceNumber;
    private String type;
    private double totalPool;
    private double usedAmount;


    public Invoice(int id, String invoiceNumber, String type, double totalPool, double usedAmount) {
        this.id = id;
        this.invoiceNumber = invoiceNumber;
        this.type = type;
        this.totalPool = totalPool;
        this.usedAmount = usedAmount;
    }

}
