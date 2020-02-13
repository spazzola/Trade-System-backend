package com.tradesystem.ordercomment;

import com.tradesystem.ordercomment.OrderComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderCommentDao extends JpaRepository<OrderComment, Long> {
}
