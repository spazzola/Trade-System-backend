package com.tradesystem.price;

import com.tradesystem.buyer.BuyerDto;
import com.tradesystem.product.ProductDto;
import com.tradesystem.supplier.SupplierDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceDto {


    private BigDecimal price;
    private ProductDto productType;
    private BuyerDto buyer;
    private SupplierDto supplier;

}
