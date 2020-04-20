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
import java.util.Set;


@Service
public class OrderService {

    private BuyerDao buyerDao;
    private SupplierDao supplierDao;
    private ProductDao productDao;
    private OrderDao orderDao;
    private OrderDetailsService orderDetailsService;


    public OrderService(BuyerDao buyerDao, SupplierDao supplierDao, ProductDao productDao,
                        OrderDetailsService orderDetailsService, OrderDao orderDao) {
        this.buyerDao = buyerDao;
        this.supplierDao = supplierDao;
        this.productDao = productDao;
        this.orderDetailsService = orderDetailsService;
        this.orderDao = orderDao;
    }

    @Transactional
    public List<Order> getAllOrders() {
        return orderDao.findAll();
    }

    @Transactional
    public Set<Order> getMonthOrders(int month, int year) {
        return orderDao.getMonthOrders(month, year);
    }

    @Transactional
    public Order createOrder(CreateOrderRequest createOrderRequest) {
        Buyer buyer = buyerDao.findById(createOrderRequest.getBuyerId())
                .orElseThrow(RuntimeException::new);

        Supplier supplier = supplierDao.findById(createOrderRequest.getSupplierId())
                .orElseThrow(RuntimeException::new);

        if (validateOrder(createOrderRequest)) {
            List<OrderDetailsDto> orderDetailsDtoList = createOrderRequest.getOrderDetails();

            Order order = Order.builder()
                    .date(createOrderRequest.getDate())
                    .buyer(buyer)
                    .supplier(supplier)
                    .build();

            List<OrderDetails> orderDetailsList = new ArrayList<>();

            for (OrderDetailsDto orderDetailsDto : orderDetailsDtoList) {

                if (validateOrderDetail(orderDetailsDto)) {
                    OrderDetails orderDetails = new OrderDetails();
                    Long productId = orderDetailsDto.getProduct().getId();
                    Product product = productDao.findById(productId)
                            .orElseThrow(NoSuchElementException::new);
                    orderDetails.setProduct(product);

                    orderDetails.setQuantity(orderDetailsDto.getQuantity());
                    orderDetails.setOrder(order);

                    // TODO add functionality to adding comments from user
                    // String userComment = orderDetailsDto.getOrderComment().getUserComment();
                    // orderDetails.getOrderComment().setUserComment(userComment);

                    orderDetailsList.add(orderDetails);
                } else {
                    throw new RuntimeException("Can't create order detail");
                }
            }

            order.setOrderDetails(orderDetailsList);

            return calculateOrder(order);
        } else {
            throw new RuntimeException("Can't create order");
        }

    }

    private Order calculateOrder(Order order) {
        List<OrderDetails> orderDetails = order.getOrderDetails();
        for (OrderDetails orderDetail : orderDetails) {
            orderDetailsService.calculateOrderDetail(orderDetail);
        }
        return order;
    }

    private boolean validateOrder(CreateOrderRequest order) {
        if (order.getDate() == null) {
            return false;
        }
        if (order.getOrderDetails() == null) {
            return false;
        }
        return true;
    }

    private boolean validateOrderDetail(OrderDetailsDto orderDetail) {
        if (orderDetail.getQuantity().doubleValue() <= 0) {
            return false;
        }
        if (orderDetail.getProduct() == null) {
            return false;
        }
        return true;
    }
}
