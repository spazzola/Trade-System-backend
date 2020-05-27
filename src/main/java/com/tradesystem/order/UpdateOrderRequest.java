package com.tradesystem.order;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateOrderRequest {

    private String newTransportNumber;
    private BigDecimal newQuantity;
    private BigDecimal newBuyerSum;
    private BigDecimal newSupplierSum;

}
