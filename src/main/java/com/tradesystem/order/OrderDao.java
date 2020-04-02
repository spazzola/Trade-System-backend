package com.tradesystem.order;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository("orderDao")
public interface OrderDao extends JpaRepository<Order, Long> {

    @Query(value = "SELECT * FROM orders o " +
            "LEFT JOIN order_details od ON od.order_fk = o.order_id " +
            "WHERE MONTH(o.date) = ?1 AND YEAR(o.date) = ?2",
            nativeQuery = true)
    Set<Order> getMonthOrders(int month, int year);

    @Query(value = "SELECT orders FROM Order orders " +
            "LEFT JOIN FETCH orders.orderDetails " +
            "WHERE YEAR(orders.date) = ?1")
    Set<Order> getYearOrders(int year);

}