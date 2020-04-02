package com.tradesystem.orderdetails;

import com.tradesystem.ordercomment.OrderCommentDto;
import com.tradesystem.product.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailsDto {

    private BigDecimal quantity;
    private BigDecimal buyerSum;
    private BigDecimal supplierSum;
    private ProductDto product;
    private OrderCommentDto orderComment;

}