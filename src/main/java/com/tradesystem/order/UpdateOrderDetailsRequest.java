package com.tradesystem.order;

import com.tradesystem.buyer.Buyer;
import com.tradesystem.supplier.Supplier;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateOrderDetailsRequest {

    private Long id;
    private Buyer newBuyer;
    private Supplier newSupplier;
    private String oldTransportNumber;
    private String newTransportNumber;
    private BigDecimal newQuantity;
    private BigDecimal newBuyerPrice;
    private BigDecimal newBuyerSum;
    private BigDecimal newSupplierPrice;
    private BigDecimal newSupplierSum;

}
