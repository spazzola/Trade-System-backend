package com.tradesystem.ordercomment;

import com.tradesystem.buyer.Buyer;
import com.tradesystem.invoice.Invoice;
import com.tradesystem.invoice.InvoiceDao;
import com.tradesystem.orderdetails.OrderDetails;
import com.tradesystem.supplier.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class OrderCommentService {

    @Autowired
    private OrderCommentDao orderCommentDao;

    @Autowired
    private InvoiceDao invoiceDao;


    public void addLackAmountComment(OrderDetails orderDetails, BigDecimal negativeValue, Invoice invoice) {

        if (orderDetails.getOrderComment() == null) {
            addBuyerLackAmountComment(orderDetails, negativeValue, invoice);

        } else {

            String previousComment = orderDetails.getOrderComment().getSystemComment();
            OrderComment orderComment = orderDetails.getOrderComment();
            Supplier supplier = orderDetails.getOrder().getSupplier();
            Buyer buyer = orderDetails.getOrder().getBuyer();
            Optional<Invoice> supplierNegativeInvoice = invoiceDao.getSupplierNegativeInvoice(supplier.getId());

            if (supplierNegativeInvoice.isPresent()) {
                addSupplierLackAmountComment(orderDetails, negativeValue, orderComment, previousComment, invoice);
            } else {
                orderComment.setSystemComment(previousComment + ", " + buyer.getName() + ": brakło " + negativeValue);
                orderDetails.setOrderComment(orderComment);
                orderCommentDao.save(orderComment);
            }
        }
    }

    private void addBuyerLackAmountComment(OrderDetails orderDetails, BigDecimal negativeValue, Invoice invoice) {
        Buyer buyer = orderDetails.getOrder().getBuyer();
        OrderComment orderComment = new OrderComment();
        orderComment.setSystemComment(buyer.getName() + ": wartość negatywnej faktury o id " + invoice.getId() + " " + "została powiększona o " + negativeValue);
        orderDetails.setOrderComment(orderComment);
        orderCommentDao.save(orderComment);
    }


    private void addSupplierLackAmountComment(OrderDetails orderDetails, BigDecimal negativeValue,
                                             OrderComment orderComment, String previousComment, Invoice invoice) {
        Supplier supplier = orderDetails.getOrder().getSupplier();

        orderComment.setSystemComment(previousComment + " " + supplier.getName() + ": wartość negatywnej faktury o id " + invoice.getId() + " została powiększona o " + negativeValue);
        orderCommentDao.save(orderComment);
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

        String previousComment = orderDetails.getOrderComment().getSystemComment();

        OrderComment orderComment = orderDetails.getOrderComment();
        orderComment.setSystemComment(previousComment + ", " + supplierName + ": odjęto " + invoiceValue + " z FV nr " + invoice.getInvoiceNumber());
        orderDetails.setOrderComment(orderComment);
        orderCommentDao.save(orderComment);

    }
}
