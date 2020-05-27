package com.tradesystem.order;

import com.tradesystem.orderdetails.OrderDetailsDto;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CreateOrderRequest {

    private LocalDate date;
    private Long buyerId;
    private Long supplierId;
    private List<OrderDetailsDto> orderDetails;

}
