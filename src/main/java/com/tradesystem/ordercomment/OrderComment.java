package com.tradesystem.ordercomment;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "order_comments")
public class OrderComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_comment_id")
    private Long id;

    @Column(name = "system_comment")
    private String systemComment;

    @Column(name = "user_comment")
    private String userComment;


}
