package com.tradesystem.order;

import com.tradesystem.buyer.Buyer;
import com.tradesystem.buyer.BuyerDao;
import com.tradesystem.orderdetails.OrderDetails;
import com.tradesystem.orderdetails.OrderDetailsDto;
import com.tradesystem.orderdetails.OrderDetailsService;
import com.tradesystem.product.Product;
import com.tradesystem.product.ProductDao;
import com.tradesystem.supplier.Supplier;
import com.tradesystem.supplier.SupplierDao;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


@Service
public class OrderService {


    private BuyerDao buyerDao;
    private SupplierDao supplierDao;
    private ProductDao productDao;
    private OrderDetailsService orderDetailsService;


    public OrderService(BuyerDao buyerDao, SupplierDao supplierDao, ProductDao productDao, OrderDetailsService orderDetailsService) {
        this.buyerDao = buyerDao;
        this.supplierDao = supplierDao;
        this.productDao = productDao;
        this.orderDetailsService = orderDetailsService;
    }

    @Transactional
    public Order createOrder(CreateOrderRequest createOrderRequest) {
        Buyer buyer = buyerDao.findById(createOrderRequest.getBuyerId())
                .orElseThrow(RuntimeException::new);

        Supplier supplier = supplierDao.findById(createOrderRequest.getSupplierId())
                .orElseThrow(RuntimeException::new);

        List<OrderDetailsDto> orderDetailsDtoList = createOrderRequest.getOrderDetails();

        Order order = Order.builder()
                .date(createOrderRequest.getDate())
                .buyer(buyer)
                .supplier(supplier)
                .build();

        List<OrderDetails> orderDetailsList = new ArrayList<>();

        for (OrderDetailsDto orderDetailsDto : orderDetailsDtoList) {
            OrderDetails orderDetails = new OrderDetails();
            orderDetails.setQuantity(orderDetailsDto.getQuantity());


            Long productId = orderDetailsDto.getProduct().getProductId();
            Product product = productDao.findById(productId)
                    .orElseThrow(NoSuchElementException::new);
            orderDetails.setOrder(order);
            orderDetails.setProduct(product);
            orderDetailsList.add(orderDetails);
        }

        order.setOrderDetails(orderDetailsList);

        calculateOrder(order);

        return order;
    }

    private Order calculateOrder(Order order) {
        List<OrderDetails> orderDetails = order.getOrderDetails();
        for (OrderDetails orderDetail : orderDetails) {
            orderDetailsService.calculateOrderDetail(orderDetail);
        }
        return order;
    }

}
