package com.tradesystem.orderdetails;

import com.tradesystem.invoice.Invoice;
import com.tradesystem.invoice.InvoiceDao;
import com.tradesystem.order.UpdateOrderRequest;
import com.tradesystem.payment.Payment;
import com.tradesystem.payment.PaymentDao;
import com.tradesystem.price.PriceDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class UpdateOrderDetailsService {

    private OrderDetailsService orderDetailsService;
    private OrderDetailsDao orderDetailsDao;
    private PaymentDao paymentDao;
    private InvoiceDao invoiceDao;
    private PriceDao priceDao;

    public UpdateOrderDetailsService(OrderDetailsDao orderDetailsDao, PaymentDao paymentDao,
                                     OrderDetailsService orderDetailsService, InvoiceDao invoiceDao, PriceDao priceDao) {
        this.orderDetailsDao = orderDetailsDao;
        this.paymentDao = paymentDao;
        this.orderDetailsService = orderDetailsService;
        this.invoiceDao = invoiceDao;
    }


    @Transactional
    public OrderDetails updateOrder(UpdateOrderRequest updateOrderRequest) {
        OrderDetails orderDetails = updateBuyerOrder(updateOrderRequest);
        orderDetails = updateSupplierOrder(updateOrderRequest);

        return orderDetails;
    }

    @Transactional
    public OrderDetails updateBuyerOrder(UpdateOrderRequest updateOrderRequest) {
        //1. Na podstawie dodaci listu znajdz stare zamowienie
        OrderDetails orderDetails = orderDetailsService.getOrderByTransportNumber(updateOrderRequest.getOldTransportNumber());

        if (!orderDetails.getTransportNumber().equals(updateOrderRequest.getNewTransportNumber())) {
            orderDetails.setTransportNumber(updateOrderRequest.getNewTransportNumber());
        }

        //2. Na podstawie znalezionego orderDetail wez faktury z ktorych zostaly sciagniete sumy dla buyera i suppliera
        List<Invoice> invoices = new ArrayList<>();
        List<Payment> payments = paymentDao.findBuyerPayment(orderDetails.getId());

        for (Payment payment : payments) {
            Long invoiceId = payment.getBuyerInvoice().getId();
            Invoice invoice = invoiceDao.findById(invoiceId)
                    .orElseThrow(RuntimeException::new);
            invoices.add(invoice);
        }

        //      a) Jesli zamowienie jest polaczone z jedna FV (negatywna, ktora moze byc suma wielu zamowien lub jedna zaplacona):
        //          -zmniejszamy/zwiekszamy wartosc FV o roznice pomiedzy starym a nowym zamowieniem
        if (invoices.size() == 1) {
            Invoice oldInvoice = invoices.get(0);

            processUpdatingBuyerInvoice(orderDetails, updateOrderRequest, oldInvoice, true);
            processUpdatingBuyerOrderDetails(orderDetails, updateOrderRequest);

        } else {
            Invoice oldInvoice = invoices.get(invoices.size() - 1);
            processUpdatingBuyerInvoice(orderDetails, updateOrderRequest, oldInvoice, false);
            processUpdatingBuyerOrderDetails(orderDetails, updateOrderRequest);

        }
        return orderDetails;
    }

    private void processUpdatingBuyerInvoice(OrderDetails orderDetails,
                                             UpdateOrderRequest updateOrderRequest, Invoice invoice, boolean condition) {

        BigDecimal oldBuyerSum = orderDetails.getBuyerSum();

        BigDecimal newBuyerPrice = updateOrderRequest.getNewBuyerPrice();
        BigDecimal newBuyerSum = updateOrderRequest.getNewQuantity().multiply(newBuyerPrice);
        updateOrderRequest.setNewBuyerSum(newBuyerSum);

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

    private void processUpdatingBuyerOrderDetails(OrderDetails orderDetails, UpdateOrderRequest updateOrderRequest) {
        orderDetails.setQuantity(updateOrderRequest.getNewQuantity());
        orderDetails.setBuyerSum(updateOrderRequest.getNewBuyerSum());
    }


    @Transactional
    public OrderDetails updateSupplierOrder(UpdateOrderRequest updateOrderRequest) {
        //1. Na podstawie dodaci listu znajdz stare zamowienie
        OrderDetails orderDetails = orderDetailsService.getOrderByTransportNumber(updateOrderRequest.getNewTransportNumber());

        if (!orderDetails.getTransportNumber().equals(updateOrderRequest.getNewTransportNumber())) {
            orderDetails.setTransportNumber(updateOrderRequest.getNewTransportNumber());
        }

        //2. Na podstawie znalezionego orderDetail wez faktury z ktorych zostaly sciagniete sumy dla buyera i suppliera
        List<Invoice> invoices = new ArrayList<>();
        List<Payment> payments = paymentDao.findSupplierPayment(orderDetails.getId());

        for (Payment payment : payments) {
            Long invoiceId = payment.getSupplierInvoice().getId();
            Invoice invoice = invoiceDao.findById(invoiceId)
                    .orElseThrow(RuntimeException::new);
            invoices.add(invoice);
        }

        //      a) Jesli zamowienie jest polaczone z jedna FV (negatywna, ktora moze byc suma wielu zamowien lub jedna zaplacona):
        //          -zmniejszamy/zwiekszamy wartosc FV o roznice pomiedzy starym a nowym zamowieniem
        if (invoices.size() == 1) {
            Invoice oldInvoice = invoices.get(0);

            processUpdatingSupplierInvoice(orderDetails, updateOrderRequest, oldInvoice, true);
            processUpdatingSupplierOrderDetails(orderDetails, updateOrderRequest);

        } else {
            Invoice oldInvoice = invoices.get(invoices.size() - 1);
            processUpdatingSupplierInvoice(orderDetails, updateOrderRequest, oldInvoice, false);
            processUpdatingSupplierOrderDetails(orderDetails, updateOrderRequest);

        }
        return orderDetails;
    }

    private void processUpdatingSupplierInvoice(OrderDetails orderDetails,
                                             UpdateOrderRequest updateOrderRequest, Invoice invoice, boolean condition) {

        BigDecimal oldSupplierSum = orderDetails.getSupplierSum();

        BigDecimal newSupplierPrice = updateOrderRequest.getNewSupplierPrice();
        BigDecimal newSupplierSum = updateOrderRequest.getNewQuantity().multiply(newSupplierPrice);
        updateOrderRequest.setNewSupplierSum(newSupplierSum);

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

    private void processUpdatingSupplierOrderDetails(OrderDetails orderDetails, UpdateOrderRequest updateOrderRequest) {
        orderDetails.setQuantity(updateOrderRequest.getNewQuantity());
        orderDetails.setSupplierSum(updateOrderRequest.getNewSupplierSum());
    }

}
