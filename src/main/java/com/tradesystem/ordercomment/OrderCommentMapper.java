package com.tradesystem.ordercomment;

import org.springframework.stereotype.Component;

@Component
public class OrderCommentMapper {

    public OrderCommentDto toDto(OrderComment orderComment) {
        return OrderCommentDto.builder()
                .userComment(orderComment.getUserComment())
                .systemComment(orderComment.getSystemComment())
                .build();
    }

}
