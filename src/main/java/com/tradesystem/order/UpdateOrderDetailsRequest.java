package com.tradesystem.order;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateOrderDetailsRequest {

    private String oldTransportNumber;
    private String newTransportNumber;
    private BigDecimal newQuantity;
    private BigDecimal newBuyerPrice;
    private BigDecimal newBuyerSum;
    private BigDecimal newSupplierPrice;
    private BigDecimal newSupplierSum;

}
