package com.tradesystem.order;

import com.tradesystem.orderdetails.OrderDetails;
import com.tradesystem.orderdetails.OrderDetailsDao;
import com.tradesystem.orderdetails.OrderDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderDetailsService orderDetailsService;

    @Autowired
    private OrderDetailsDao orderDetailsDao;


    @Transactional
    public void calculateOrder(Order order) {
        List<OrderDetails> orderDetails = order.getOrderDetails();
        for (OrderDetails orderDetail : orderDetails) {
            orderDetailsService.calculateOrderDetail(orderDetail);
        }
    }

}
