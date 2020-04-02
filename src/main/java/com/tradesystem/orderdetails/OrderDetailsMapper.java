package com.tradesystem.orderdetails;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderDetailsMapper {

    public OrderDetailsDto toDto(OrderDetails orderDetails) {
        return OrderDetailsDto.builder()
                .quantity(orderDetails.getQuantity())
                .buyerSum(orderDetails.getBuyerSum())
                .supplierSum(orderDetails.getSupplierSum())
                //.product(orderDetails.getProduct())
                //.orderComment(orderDetails.getOrderComment())
                .build();
    }


    public List<OrderDetailsDto> toDto(List<OrderDetails> orderDetails) {
        return orderDetails.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }


}
