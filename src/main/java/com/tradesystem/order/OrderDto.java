package com.tradesystem.order;

import java.util.List;
import com.tradesystem.buyer.BuyerDto;
import com.tradesystem.orderdetails.OrderDetailsDto;
import com.tradesystem.supplier.SupplierDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    private Long id;
    private LocalDate date;
    private BuyerDto buyer;
    private List<OrderDetailsDto> orderDetails;
    private SupplierDto supplier;

}
