package com.tradesystem.order;

import com.tradesystem.orderdetails.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository("orderDao")
public interface OrderDao extends JpaRepository<Order, Long> {

    //List<Order> findByBuyerId(Long buyer_id);
    Order findByBuyerId(Long buyer_id);
/*
    @Query(value = "SELECT * FROM orders " +
            "INNER JOIN order_details on orders.order_id = order_details.order_fk " +
            "WHERE MONTH(orders.date) = ?1 AND YEAR(orders.date) = ?2",
            nativeQuery = true)
    List<OrderDetails> getMonthOrders(int month, int year);
/*
    @Query(value = "SELECT * FROM orders o " +
            "INNER JOIN order_details od ON od.order_fk = o.order_id " +
            "WHERE MONTH(o.date) = ?1 AND YEAR(o.date) = ?2",
            nativeQuery = true)
    List<Order> getMonthOrders(int month, int year);

/*
    @Query(value = "SELECT orders FROM Order orders " +
            "LEFT JOIN FETCH orders.orderDetails " +
            "WHERE MONTH(orders.date) = ?1 AND YEAR(orders.date) = ?2")
    List<Order> getMonthOrders(int month, int year);
*/

    @Query(value = "SELECT * FROM orders o " +
            "LEFT JOIN order_details od ON od.order_fk = o.order_id " +
            "WHERE MONTH(o.date) = ?1 AND YEAR(o.date) = ?2",
            nativeQuery = true)
    Set<Order> getMonthOrders(int month, int year);
    /*
    @Query(value = "SELECT orders FROM Order orders " +
            "LEFT JOIN FETCH orders.orderDetails " +
            "WHERE MONTH(orders.date) = ?1 AND YEAR(orders.date) = ?2")
    List<Order> getMonthOrders(int month, int year);
*/
    @Query(value = "SELECT orders FROM Order orders LEFT JOIN FETCH orders.orderDetails " +
            "WHERE YEAR(orders.date) = ?1")
    List<Order> getYearOrders(int year);
}