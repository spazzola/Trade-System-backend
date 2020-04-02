package com.tradesystem.order;

import com.tradesystem.buyer.Buyer;
import com.tradesystem.buyer.BuyerDao;
import com.tradesystem.buyer.BuyerDto;
import com.tradesystem.orderdetails.OrderDetails;
import com.tradesystem.orderdetails.OrderDetailsDao;
import com.tradesystem.orderdetails.OrderDetailsDto;
import com.tradesystem.orderdetails.OrderDetailsService;
import com.tradesystem.product.Product;
import com.tradesystem.product.ProductDao;
import com.tradesystem.supplier.Supplier;
import com.tradesystem.supplier.SupplierDao;
import com.tradesystem.supplier.SupplierDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderDetailsService orderDetailsService;

    @Autowired
    private OrderDetailsDao orderDetailsDao;

    @Autowired
    private BuyerDao buyerDao;

    @Autowired
    private SupplierDao supplierDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ProductDao productDao;

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
