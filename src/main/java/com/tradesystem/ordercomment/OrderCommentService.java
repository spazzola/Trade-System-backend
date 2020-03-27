package com.tradesystem.ordercomment;

import com.tradesystem.invoice.Invoice;
import com.tradesystem.orderdetails.OrderDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class OrderCommentService {

    @Autowired
    private OrderCommentDao orderCommentDao;


    public void addLackAmountComment(OrderDetails orderDetails, BigDecimal negativeValue) {
        if (orderDetails.getOrderComment() == null) {
            //jesli jest nullem, dodac nowy komentarz
        }
        else {
            String previousComment = orderDetails.getOrderComment().getSystemComment();

            OrderComment orderComment = orderDetails.getOrderComment();
            orderComment.setSystemComment(previousComment + ", brakło " + negativeValue);
            orderDetails.setOrderComment(orderComment);
            orderCommentDao.save(orderComment);
        }
    }

    public void addBuyerComment(OrderDetails orderDetails, BigDecimal invoiceValue, Invoice invoice) {
        if (orderDetails.getOrderComment() == null) {
            String buyerName = orderDetails.getOrder().getBuyer().getName();

            OrderComment orderComment = new OrderComment();
            orderComment.setSystemComment(buyerName + ": odjęto " + invoiceValue + " z FV nr " + invoice.getInvoiceNumber());
            orderDetails.setOrderComment(orderComment);
            orderCommentDao.save(orderComment);

        } else {
            String previousComment = orderDetails.getOrderComment().getSystemComment();

            OrderComment orderComment = orderDetails.getOrderComment();
            orderComment.setSystemComment(previousComment + ", " + invoiceValue + " z FV nr " + invoice.getInvoiceNumber());

            orderDetails.setOrderComment(orderComment);
            orderCommentDao.save(orderComment);

        }
    }

    public void addSupplierComment(OrderDetails orderDetails, BigDecimal invoiceValue, Invoice invoice) {
        String supplierName = orderDetails.getOrder().getSupplier().getName();

        if (orderDetails.getOrderComment() == null) {
            //jesli jest nullem, dodac nowy komentarz
        }else {
            String previousComment = orderDetails.getOrderComment().getSystemComment();

            OrderComment orderComment = orderDetails.getOrderComment();
            orderComment.setSystemComment(previousComment + ", " + supplierName + ": odjęto " + invoiceValue + " z FV nr " + invoice.getInvoiceNumber());
            orderDetails.setOrderComment(orderComment);
            orderCommentDao.save(orderComment);
        }
    }
}
