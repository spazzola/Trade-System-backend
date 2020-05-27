package com.tradesystem.orderdetails;

import com.tradesystem.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("orderDetailsDao")
public interface OrderDetailsDao extends JpaRepository<OrderDetails, Long> {

    OrderDetails findByTransportNumber(String transportNumber);

}
