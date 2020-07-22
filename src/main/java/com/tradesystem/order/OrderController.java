package com.tradesystem.order;

import com.tradesystem.orderdetails.*;
import com.tradesystem.user.RoleSecurity;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
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
    private RoleSecurity roleSecurity;

    private Logger logger = LogManager.getLogger(OrderController.class);


    public OrderController(OrderService orderService, OrderMapper orderMapper,
                           OrderDao orderDao, OrderDetailsService orderDetailsService,
                           OrderDetailsMapper orderDetailsMapper, RoleSecurity roleSecurity,
                           UpdateOrderDetailsService updateOrderDetailsService) {
        this.orderService = orderService;
        this.orderMapper = orderMapper;
        this.orderDao = orderDao;
        this.orderDetailsService = orderDetailsService;
        this.orderDetailsMapper = orderDetailsMapper;
        this.updateOrderDetailsService = updateOrderDetailsService;
        this.roleSecurity = roleSecurity;
    }

    @PostMapping("/create")
    public OrderDto createOrder(@RequestBody CreateOrderRequest createOrderRequest) {
        logger.info("Dodawanie zamówienia: " + createOrderRequest);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        roleSecurity.checkUserRole(authentication);

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

    @PutMapping("/updateOrder")
    public OrderDetailsDto updateOrder(@RequestBody UpdateOrderDetailsRequest updateOrderDetailsRequest) {
        logger.info("Aktualizowanie zamówienia: " + updateOrderDetailsRequest);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        roleSecurity.checkUserRole(authentication);

        OrderDetails orderDetails = updateOrderDetailsService.updateOrder(updateOrderDetailsRequest);

        return orderDetailsMapper.toDto(orderDetails);
    }

}
