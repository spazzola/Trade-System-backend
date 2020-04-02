package com.tradesystem.order;

import com.tradesystem.orderdetails.OrderDetailsDto;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CreateOrderRequest {

    LocalDate date;
    Long buyerId;
    Long supplierId;
    List<OrderDetailsDto> orderDetails;

}
