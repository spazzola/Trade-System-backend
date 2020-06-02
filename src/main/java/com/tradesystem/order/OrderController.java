package com.tradesystem.order;

import com.tradesystem.orderdetails.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/order")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class OrderController {

    private OrderService orderService;
    private OrderMapper orderMapper;
    private OrderDao orderDao;
    private OrderDetailsService orderDetailsService;
    private OrderDetailsMapper orderDetailsMapper;
    private UpdateOrderDetailsService updateOrderDetailsService;

    public OrderController(OrderService orderService, OrderMapper orderMapper,
                           OrderDao orderDao, OrderDetailsService orderDetailsService,
                           OrderDetailsMapper orderDetailsMapper, UpdateOrderDetailsService updateOrderDetailsService) {
        this.orderService = orderService;
        this.orderMapper = orderMapper;
        this.orderDao = orderDao;
        this.orderDetailsService = orderDetailsService;
        this.orderDetailsMapper = orderDetailsMapper;
        this.updateOrderDetailsService = updateOrderDetailsService;
    }

    @PostMapping("/create")
    public OrderDto createOrder(@RequestBody CreateOrderRequest createOrderRequest) {
        final Order order = orderService.createOrder(createOrderRequest);

        return orderMapper.toDto(order);
    }

    @GetMapping("/getAll")
    public List<OrderDto> getOrders() {
        final List<Order> ordersDto = orderService.getAllOrders();

        return orderMapper.toDto(ordersDto);
    }

    @GetMapping("/getMonthOrders")
    public List<OrderDto> getMonthOrders(@RequestParam(value = "year") String year,
                                         @RequestParam(value = "month") String month) {
        int y = Integer.valueOf(year);
        int m = Integer.valueOf(month);
        final List<Order> ordersDto = orderDao.getMonthOrders2(m, y);

        return orderMapper.toDto(ordersDto);
    }

    @GetMapping("/getOrderByTransportNumber")
    public OrderDetailsDto getOrderByTransportNumber(@RequestParam(value = "transportNumber") String transportNumber) {
        OrderDetails orderDetails = orderDetailsService.getOrderByTransportNumber(transportNumber);
        return orderDetailsMapper.toDto(orderDetails);
    }

    @PutMapping("/updateBuyerOrder")
    public OrderDetailsDto updateBuyerOrder(@RequestBody UpdateOrderRequest updateOrderRequest) {
        OrderDetails orderDetails = updateOrderDetailsService.updateBuyerOrder(updateOrderRequest);
        return orderDetailsMapper.toDto(orderDetails);

    }
}
