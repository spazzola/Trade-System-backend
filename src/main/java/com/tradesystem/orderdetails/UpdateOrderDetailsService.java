package com.tradesystem.orderdetails;

import com.tradesystem.buyer.Buyer;
import com.tradesystem.invoice.Invoice;
import com.tradesystem.invoice.InvoiceDao;
import com.tradesystem.order.UpdateOrderDetailsRequest;
import com.tradesystem.ordercomment.OrderCommentService;
import com.tradesystem.payment.Payment;
import com.tradesystem.payment.PaymentDao;
import com.tradesystem.price.PriceDao;
import com.tradesystem.product.Product;
import com.tradesystem.supplier.Supplier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class UpdateOrderDetailsService {

    private OrderDetailsService orderDetailsService;
    private PaymentDao paymentDao;
    private InvoiceDao invoiceDao;
    private PriceDao priceDao;
    private OrderCommentService orderCommentService;

    public UpdateOrderDetailsService(PaymentDao paymentDao, OrderDetailsService orderDetailsService,
                                     InvoiceDao invoiceDao,  OrderCommentService orderCommentService, PriceDao priceDao) {
        this.paymentDao = paymentDao;
        this.orderDetailsService = orderDetailsService;
        this.invoiceDao = invoiceDao;
        this.orderCommentService = orderCommentService;
        this.priceDao = priceDao;
    }


    @Transactional
    public OrderDetails updateOrder(UpdateOrderDetailsRequest updateOrderDetailsRequest) {
        OrderDetails orderDetails = orderDetailsService.getOrderByTransportNumber(updateOrderDetailsRequest.getOldTransportNumber());
        Product product = orderDetails.getProduct();
        Buyer buyer = orderDetails.getOrder().getBuyer();
        Supplier supplier = orderDetails.getOrder().getSupplier();

        if (!orderDetails.getTransportNumber().equals(updateOrderDetailsRequest.getNewTransportNumber())) {
            orderDetails.setTransportNumber(updateOrderDetailsRequest.getNewTransportNumber());
        }

        if (updateOrderDetailsRequest.getNewQuantity() == null || updateOrderDetailsRequest.getNewQuantity().equals(BigDecimal.ZERO)) {
            updateOrderDetailsRequest.setNewQuantity(orderDetails.getQuantity());
        }

        if (updateOrderDetailsRequest.getNewBuyerPrice() == null || updateOrderDetailsRequest.getNewBuyerPrice().equals(BigDecimal.ZERO)) {
            BigDecimal oldBuyerPrice = priceDao.getBuyerPrice(buyer.getId(), product.getId());
            updateOrderDetailsRequest.setNewBuyerPrice(oldBuyerPrice);
        }

        if (updateOrderDetailsRequest.getNewSupplierPrice() == null || updateOrderDetailsRequest.getNewSupplierPrice().equals(BigDecimal.ZERO)) {
            BigDecimal oldSupplierPrice = priceDao.getSupplierPrice(supplier.getId(), product.getId());
            updateOrderDetailsRequest.setNewSupplierPrice(oldSupplierPrice);
        }

        updateBuyerOrder(updateOrderDetailsRequest, orderDetails);

        return updateSupplierOrder(updateOrderDetailsRequest, orderDetails);
    }

    private void updateBuyerOrder(UpdateOrderDetailsRequest updateOrderDetailsRequest, OrderDetails orderDetails) {
        List<Invoice> invoices = new ArrayList<>();
        List<Payment> payments = paymentDao.findBuyerPayment(orderDetails.getId());

        for (Payment payment : payments) {
            Long invoiceId = payment.getBuyerInvoice().getId();
            Invoice invoice = invoiceDao.findById(invoiceId)
                    .orElseThrow(RuntimeException::new);
            invoices.add(invoice);
        }

        if (invoices.size() == 1) {
            Invoice oldInvoice = invoices.get(0);

            processUpdatingBuyerInvoice(orderDetails, updateOrderDetailsRequest, oldInvoice, true);
            processUpdatingBuyerOrderDetails(orderDetails, updateOrderDetailsRequest);

        } else {
            Invoice oldInvoice = invoices.get(invoices.size() - 1);
            processUpdatingBuyerInvoice(orderDetails, updateOrderDetailsRequest, oldInvoice, false);
            processUpdatingBuyerOrderDetails(orderDetails, updateOrderDetailsRequest);

        }
        orderCommentService.addEditComment(orderDetails, " ZAMÓWIENIE EDYTOWANO");
    }

    private OrderDetails updateSupplierOrder(UpdateOrderDetailsRequest updateOrderDetailsRequest, OrderDetails orderDetails) {
        List<Invoice> invoices = new ArrayList<>();
        List<Payment> payments = paymentDao.findSupplierPayment(orderDetails.getId());

        for (Payment payment : payments) {
            Long invoiceId = payment.getSupplierInvoice().getId();
            Invoice invoice = invoiceDao.findById(invoiceId)
                    .orElseThrow(RuntimeException::new);
            invoices.add(invoice);
        }

        if (invoices.size() == 1) {
            Invoice oldInvoice = invoices.get(0);

            processUpdatingSupplierInvoice(orderDetails, updateOrderDetailsRequest, oldInvoice, true);
            processUpdatingSupplierOrderDetails(orderDetails, updateOrderDetailsRequest);

        } else {
            Invoice oldInvoice = invoices.get(invoices.size() - 1);
            processUpdatingSupplierInvoice(orderDetails, updateOrderDetailsRequest, oldInvoice, false);
            processUpdatingSupplierOrderDetails(orderDetails, updateOrderDetailsRequest);

        }
        orderCommentService.addEditComment(orderDetails, " ZAMÓWIENIE EDYTOWANO");

        return orderDetails;
    }

    private void processUpdatingBuyerInvoice(OrderDetails orderDetails,
                                             UpdateOrderDetailsRequest updateOrderDetailsRequest, Invoice invoice, boolean condition) {

        BigDecimal oldBuyerSum = orderDetails.getBuyerSum();

        BigDecimal newBuyerPrice = updateOrderDetailsRequest.getNewBuyerPrice();
        BigDecimal newBuyerSum = updateOrderDetailsRequest.getNewQuantity().multiply(newBuyerPrice);
        updateOrderDetailsRequest.setNewBuyerSum(newBuyerSum);

        BigDecimal difference = oldBuyerSum.subtract(newBuyerSum);

        BigDecimal oldAmountToUse = invoice.getAmountToUse();

        invoice.setAmountToUse(oldAmountToUse.add(difference));

        if (condition) {
            BigDecimal oldValue = invoice.getValue();
            invoice.setValue(oldValue.add(difference));
        } else {
            invoice.setUsed(false);
        }
        invoiceDao.save(invoice);
    }

    private void processUpdatingBuyerOrderDetails(OrderDetails orderDetails, UpdateOrderDetailsRequest updateOrderDetailsRequest) {
        orderDetails.setQuantity(updateOrderDetailsRequest.getNewQuantity());
        orderDetails.setBuyerSum(updateOrderDetailsRequest.getNewBuyerSum());
    }

    private void processUpdatingSupplierInvoice(OrderDetails orderDetails,
                                                UpdateOrderDetailsRequest updateOrderDetailsRequest, Invoice invoice, boolean condition) {

        BigDecimal oldSupplierSum = orderDetails.getSupplierSum();

        BigDecimal newSupplierPrice = updateOrderDetailsRequest.getNewSupplierPrice();
        BigDecimal newSupplierSum = updateOrderDetailsRequest.getNewQuantity().multiply(newSupplierPrice);
        updateOrderDetailsRequest.setNewSupplierSum(newSupplierSum);

        BigDecimal difference = oldSupplierSum.subtract(newSupplierSum);

        BigDecimal oldAmountToUse = invoice.getAmountToUse();

        invoice.setAmountToUse(oldAmountToUse.add(difference));

        if (condition) {
            BigDecimal oldValue = invoice.getValue();
            invoice.setValue(oldValue.add(difference));
        } else {
            invoice.setUsed(false);
        }
        invoiceDao.save(invoice);
    }

    private void processUpdatingSupplierOrderDetails(OrderDetails orderDetails, UpdateOrderDetailsRequest updateOrderDetailsRequest) {
        orderDetails.setQuantity(updateOrderDetailsRequest.getNewQuantity());
        orderDetails.setSupplierSum(updateOrderDetailsRequest.getNewSupplierSum());
    }

}
