package com.tradesystem.order;

import java.time.LocalDate;
import java.util.List;

import com.tradesystem.buyer.Buyer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("orderDao")
public interface OrderDao extends JpaRepository<Order, Long> {

    List<Order> findByBuyerId(Long buyer_id);

    @Query(value = "SELECT * FROM orders " +
            "WHERE MONTH(date) = ?1 AND YEAR(date) = ?2",
            nativeQuery = true)
    List<Order> getMonthlyOrders(int month, int year);

}
