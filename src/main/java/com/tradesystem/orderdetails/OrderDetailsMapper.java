package com.tradesystem.orderdetails;

import com.tradesystem.ordercomment.OrderComment;
import com.tradesystem.ordercomment.OrderCommentDto;
import com.tradesystem.product.ProductDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderDetailsMapper {

    public OrderDetailsDto toDto(OrderDetails orderDetails) {
        OrderComment orderComment = orderDetails.getOrderComment();

        OrderCommentDto orderCommentDto = OrderCommentDto.builder()
                .userComment(orderComment.getUserComment())
                .systemComment(orderComment.getSystemComment())
                .build();

        ProductDto productDto = ProductDto.builder()
                .id(orderDetails.getProduct().getId())
                .product(orderDetails.getProduct().getProduct())
                .build();

        return OrderDetailsDto.builder()
                .quantity(orderDetails.getQuantity())
                .buyerSum(orderDetails.getBuyerSum())
                .supplierSum(orderDetails.getSupplierSum())
                .product(productDto)
                .orderComment(orderCommentDto)
                .build();
    }


    public List<OrderDetailsDto> toDto(List<OrderDetails> orderDetails) {
        return orderDetails.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }


}
