package com.tradesystem.ordercomment;

import com.tradesystem.invoice.Invoice;
import com.tradesystem.order.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OrderCommentService {

    @Autowired
    private OrderCommentDao orderCommentDao;


    public void addLackAmountComment(Order order, BigDecimal negativeValue) {
        String previousComment = order.getOrderComment().getSystemComment();

        OrderComment orderComment = order.getOrderComment();
        orderComment.setSystemComment(previousComment + ", brakło " + negativeValue);
        order.setOrderComment(orderComment);
        orderCommentDao.save(orderComment);

    }

    public void addBuyerComment(Order order, BigDecimal invoiceValue, Invoice invoice) {
        if (order.getOrderComment() == null) {
            String buyerName = order.getBuyer().getName();

            OrderComment orderComment = new OrderComment();
            orderComment.setSystemComment(buyerName + ": odjęto " + invoiceValue + " z FV nr " + invoice.getInvoiceNumber());
            order.setOrderComment(orderComment);
            orderCommentDao.save(orderComment);

        } else {
            String previousComment = order.getOrderComment().getUserComment();

            OrderComment orderComment = order.getOrderComment();
            orderComment.setSystemComment(previousComment + ", " + invoiceValue + " z FV nr " + invoice.getInvoiceNumber());
            order.setOrderComment(orderComment);
            orderCommentDao.save(orderComment);

        }
    }

    public void addSupplierComment(Order order, BigDecimal invoiceValue, Invoice invoice) {
        String supplierName = order.getSupplier().getName();
        String previousComment = order.getOrderComment().getSystemComment();

        OrderComment orderComment = order.getOrderComment();
        orderComment.setSystemComment(previousComment + ", " + supplierName + ": odjęto " + invoiceValue + " z FV nr " + invoice.getInvoiceNumber());
        order.setOrderComment(orderComment);
        orderCommentDao.save(orderComment);

    }
}
