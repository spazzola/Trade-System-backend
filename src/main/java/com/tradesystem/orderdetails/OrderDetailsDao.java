package com.tradesystem.orderdetails;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("orderDetailsDao")
public interface OrderDetailsDao extends JpaRepository<OrderDetails, Long> {

/*
    @Query(value = "SELECT * FROM orderDetails " +
            "WHERE MONTH(date) = ?1 AND YEAR(date) = ?2",
            nativeQuery = true)
    List<OrderDetails> getMonthOrders(int month, int year);
*/
/*
    @Query(value = "SELECT * FROM orders " +
            "INNER JOIN order_details on orders.order_id = order_details.order_fk " +
            "WHERE MONTH(orders.date) = ?1 AND YEAR(orders.date) = ?2",
            nativeQuery = true)
    List<OrderDetails> getMonthOrders(int month, int year);
*/
}
