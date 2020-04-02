package com.tradesystem.order;

import com.tradesystem.buyer.Buyer;
import com.tradesystem.buyer.BuyerDto;
import com.tradesystem.buyer.BuyerMapper;
import com.tradesystem.orderdetails.OrderDetailsDto;
import com.tradesystem.orderdetails.OrderDetailsMapper;
import com.tradesystem.supplier.Supplier;
import com.tradesystem.supplier.SupplierDto;
import com.tradesystem.supplier.SupplierMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderMapper {

    private BuyerMapper buyerMapper;
    private SupplierMapper supplierMapper;
    private OrderDetailsMapper orderDetailsMapper;

    public OrderMapper(BuyerMapper buyerMapper, SupplierMapper supplierMapper, OrderDetailsMapper orderDetailsMapper) {
        this.buyerMapper = buyerMapper;
        this.supplierMapper = supplierMapper;
        this.orderDetailsMapper = orderDetailsMapper;
    }

    public OrderDto toDto(Order order) {
        final Buyer buyer = order.getBuyer();
        final Supplier supplier = order.getSupplier();
        final List<OrderDetailsDto> orderDetailsDto = orderDetailsMapper.toDto(order.getOrderDetails());

        final BuyerDto buyerDto = buyerMapper.toDto(buyer);
        final SupplierDto supplierDto = supplierMapper.toDto(supplier);

        return OrderDto.builder()
                .date(order.getDate())
                .supplier(supplierDto)
                .buyer(buyerDto)
                .orderDetails(orderDetailsDto)
                .build();
    }

}
