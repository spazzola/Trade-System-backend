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
    public OrderDetails updateBuyerOrder(UpdateOrderRequest updateOrderRequest) {
        //1. Na podstawie dodaci listu znajdz stare zamowienie
        OrderDetails orderDetails = orderDetailsService.getOrderByTransportNumber(updateOrderRequest.getNewTransportNumber());

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
        return null;
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

}
