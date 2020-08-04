package com.tradesystem.orderdetails;

import com.tradesystem.buyer.Buyer;
import com.tradesystem.invoice.Invoice;
import com.tradesystem.invoice.InvoiceDao;
import com.tradesystem.order.CreateOrderRequest;
import com.tradesystem.order.OrderDao;
import com.tradesystem.order.OrderService;
import com.tradesystem.order.UpdateOrderDetailsRequest;
import com.tradesystem.ordercomment.OrderCommentService;
import com.tradesystem.payment.Payment;
import com.tradesystem.payment.PaymentDao;
import com.tradesystem.price.PriceDao;
import com.tradesystem.product.Product;
import com.tradesystem.product.ProductDto;
import com.tradesystem.supplier.Supplier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class UpdateOrderDetailsService {

    private OrderService orderService;
    private OrderDetailsService orderDetailsService;
    private PaymentDao paymentDao;
    private InvoiceDao invoiceDao;
    private PriceDao priceDao;
    private OrderDao orderDao;
    private OrderDetailsDao orderDetailsDao;
    private OrderCommentService orderCommentService;

    public UpdateOrderDetailsService(OrderService orderService, PaymentDao paymentDao, OrderDetailsService orderDetailsService,
                                     InvoiceDao invoiceDao,  OrderCommentService orderCommentService,
                                     PriceDao priceDao, OrderDetailsDao orderDetailsDao, OrderDao orderDao) {
        this.orderService = orderService;
        this.paymentDao = paymentDao;
        this.orderDetailsService = orderDetailsService;
        this.invoiceDao = invoiceDao;
        this.orderCommentService = orderCommentService;
        this.priceDao = priceDao;
        this.orderDao = orderDao;
        this.orderDetailsDao = orderDetailsDao;
    }


    @Transactional
    public void updateOrder(UpdateOrderDetailsRequest updateOrderDetailsRequest) {
        OrderDetails orderDetails = orderDetailsService.getOrderById(updateOrderDetailsRequest.getId());
        orderDetails.setTypedPrice(BigDecimal.ZERO);
        Product product = orderDetails.getProduct();
        Buyer oldBuyer = orderDetails.getOrder().getBuyer();
        Supplier oldSupplier = orderDetails.getOrder().getSupplier();

        if (!orderDetails.getTransportNumber().equals(updateOrderDetailsRequest.getNewTransportNumber())) {
            orderDetails.setTransportNumber(updateOrderDetailsRequest.getNewTransportNumber());
        }

        if (updateOrderDetailsRequest.getNewQuantity() == null || updateOrderDetailsRequest.getNewQuantity().equals(BigDecimal.ZERO)) {
            updateOrderDetailsRequest.setNewQuantity(orderDetails.getQuantity());
        }

        if (updateOrderDetailsRequest.getNewBuyerPrice() == null || updateOrderDetailsRequest.getNewBuyerPrice().equals(BigDecimal.ZERO)) {
            BigDecimal oldBuyerPrice = priceDao.getBuyerPrice(oldBuyer.getId(), product.getId());
            updateOrderDetailsRequest.setNewBuyerPrice(oldBuyerPrice);
        }

        if (updateOrderDetailsRequest.getNewSupplierPrice() == null || updateOrderDetailsRequest.getNewSupplierPrice().equals(BigDecimal.ZERO)) {
            BigDecimal oldSupplierPrice = priceDao.getSupplierPrice(oldSupplier.getId(), product.getId());
            updateOrderDetailsRequest.setNewSupplierPrice(oldSupplierPrice);
        }

        if (checkIfMerchantIsChanged(updateOrderDetailsRequest, oldBuyer, oldSupplier)) {
            processNewOrder(updateOrderDetailsRequest, orderDetails);
        } else {
            updateBuyerOrder(updateOrderDetailsRequest, orderDetails);
            updateSupplierOrder(updateOrderDetailsRequest, orderDetails);
        }
    }

    private boolean checkIfMerchantIsChanged(UpdateOrderDetailsRequest updateOrderDetailsRequest,
                                             Buyer oldBuyer, Supplier oldSupplier) {

        Buyer newBuyer = updateOrderDetailsRequest.getNewBuyer();
        Supplier newSupplier = updateOrderDetailsRequest.getNewSupplier();

        return !newBuyer.getName().equals(oldBuyer.getName()) || !newSupplier.getName().equals(oldSupplier.getName());
    }

    private void processNewOrder(UpdateOrderDetailsRequest updateOrderDetailsRequest, OrderDetails orderDetails) {
        deletePreviousRelationships(orderDetails);
        createNewOrder(updateOrderDetailsRequest, orderDetails);
    }

    private void createNewOrder(UpdateOrderDetailsRequest updateOrderDetailsRequest, OrderDetails orderDetails) {
        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        createOrderRequest.setBuyerId(updateOrderDetailsRequest.getNewBuyer().getId());
        createOrderRequest.setSupplierId(updateOrderDetailsRequest.getNewSupplier().getId());
        createOrderRequest.setDate(orderDetails.getOrder().getDate());

        ProductDto productDto = new ProductDto();
        productDto.setId(orderDetails.getProduct().getId());

        List<OrderDetailsDto> newOrderDetails = new ArrayList<>();
        OrderDetailsDto orderDetailsDto = new OrderDetailsDto();
        orderDetailsDto.setProduct(productDto);
        orderDetailsDto.setQuantity(updateOrderDetailsRequest.getNewQuantity());
        orderDetailsDto.setTransportNumber(updateOrderDetailsRequest.getNewTransportNumber());
        orderDetailsDto.setTypedPrice(BigDecimal.ZERO);
        orderDetailsDto.setCreateBuyerInvoice(orderDetails.isCreateBuyerInvoice());
        orderDetailsDto.setInvoiceNumber(orderDetails.getInvoiceNumber());

        System.out.println(orderDetails.isCreateBuyerInvoice() + " create  buyer invoice =======");
        System.out.println(orderDetails.getInvoiceNumber() + " invoice number");

        newOrderDetails.add(orderDetailsDto);
        createOrderRequest.setOrderDetails(newOrderDetails);

        orderService.createOrder(createOrderRequest);
    }

    private void deletePreviousRelationships(OrderDetails orderDetails) {
        List<Payment> payments = paymentDao.findByOrderDetailsId(orderDetails.getId());
        for (Payment payment : payments) {
            if (payment.getBuyerInvoice() != null) {
                processDeletingBuyerInvoice(payment, orderDetails);
            } else {
                processDeletingSupplierInvoice(payment, orderDetails);
            }
            paymentDao.delete(payment);
        }
        orderDetailsDao.delete(orderDetails);
        orderDao.delete(orderDetails.getOrder());
    }

    private void processDeletingBuyerInvoice(Payment payment, OrderDetails orderDetails) {
        Invoice invoice = payment.getBuyerInvoice();
        BigDecimal oldInvoiceValue = invoice.getAmountToUse();

        if (invoice.isCreatedToOrder()) {
            invoiceDao.delete(invoice);
        }

        if (invoice.getAmountToUse().compareTo(BigDecimal.ZERO) < 0) {
            if (checkIfValuesAreEqual(invoice.getAmountToUse(), orderDetails.getBuyerSum())) {
                invoiceDao.delete(invoice);
            } else {
                invoice.setAmountToUse(oldInvoiceValue.add(orderDetails.getBuyerSum()));
            }
        }

        if (invoice.getAmountToUse().compareTo(BigDecimal.ZERO) > 0) {
            invoice.setAmountToUse(oldInvoiceValue.add(orderDetails.getBuyerSum()));
        }
    }

    private boolean checkIfValuesAreEqual(BigDecimal negativeInvoiceValue, BigDecimal orderDetailsValue) {
        BigDecimal convertedInvoiceValue = negativeInvoiceValue.multiply(BigDecimal.valueOf(-1));

        return convertedInvoiceValue.compareTo(orderDetailsValue) == 0;
    }

    private void processDeletingSupplierInvoice(Payment payment, OrderDetails orderDetails) {
        //Invoice invoice = invoiceDao.findById(payment.getSupplierInvoice().getId())
                //.orElseThrow(RuntimeException::new);
        Invoice invoice = payment.getSupplierInvoice();
        BigDecimal oldInvoiceValue = invoice.getAmountToUse();

        if (invoice.isCreatedToOrder()) {
            invoiceDao.delete(invoice);
        }

        if (invoice.getAmountToUse().compareTo(BigDecimal.ZERO) < 0) {
            if (checkIfValuesAreEqual(invoice.getAmountToUse(), orderDetails.getSupplierSum())) {
                invoiceDao.delete(invoice);
            } else {
                invoice.setAmountToUse(oldInvoiceValue.add(orderDetails.getSupplierSum()));
            }
        }

        if (invoice.getAmountToUse().compareTo(BigDecimal.ZERO) > 0) {
            invoice.setAmountToUse(oldInvoiceValue.add(orderDetails.getSupplierSum()));
        }
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
                                             UpdateOrderDetailsRequest updateOrderDetailsRequest, Invoice invoice, boolean isOneInvoice) {
        BigDecimal oldBuyerSum = orderDetails.getBuyerSum();

        BigDecimal newBuyerPrice = updateOrderDetailsRequest.getNewBuyerPrice();
        BigDecimal newBuyerSum = updateOrderDetailsRequest.getNewQuantity().multiply(newBuyerPrice);
        updateOrderDetailsRequest.setNewBuyerSum(newBuyerSum);

        BigDecimal difference = oldBuyerSum.subtract(newBuyerSum);

        BigDecimal oldAmountToUse = invoice.getAmountToUse();

        if (isOneInvoice) {
            if (invoice.getAmountToUse().compareTo(BigDecimal.ZERO) < 0) {
                invoice.setAmountToUse(oldAmountToUse.add(difference));
            } else {
                BigDecimal previousInvoiceAmountToUse = oldAmountToUse.add(oldBuyerSum);
                BigDecimal newInvoiceAmountToUse = previousInvoiceAmountToUse.subtract(newBuyerSum);
                invoice.setAmountToUse(newInvoiceAmountToUse);
            }
            if (invoice.isCreatedToOrder()) {
                invoice.setAmountToUse(newBuyerSum);
                invoice.setValue(newBuyerSum);
            }
        } else {
            BigDecimal previousInvoiceAmountToUse = oldAmountToUse.add(oldBuyerSum);
            BigDecimal newInvoiceAmountToUse = previousInvoiceAmountToUse.subtract(newBuyerSum);
            invoice.setAmountToUse(newInvoiceAmountToUse);
            invoice.setUsed(false);
        }
        invoiceDao.save(invoice);
    }

    private void processUpdatingBuyerOrderDetails(OrderDetails orderDetails, UpdateOrderDetailsRequest updateOrderDetailsRequest) {
        orderDetails.setQuantity(updateOrderDetailsRequest.getNewQuantity());
        orderDetails.setBuyerSum(updateOrderDetailsRequest.getNewBuyerSum());
    }

    private void processUpdatingSupplierInvoice(OrderDetails orderDetails,
                                                UpdateOrderDetailsRequest updateOrderDetailsRequest, Invoice invoice, boolean isOneInvoice) {

        BigDecimal oldSupplierSum = orderDetails.getSupplierSum();

        BigDecimal newSupplierPrice = updateOrderDetailsRequest.getNewSupplierPrice();
        BigDecimal newSupplierSum = updateOrderDetailsRequest.getNewQuantity().multiply(newSupplierPrice);
        updateOrderDetailsRequest.setNewSupplierSum(newSupplierSum);

        BigDecimal difference = oldSupplierSum.subtract(newSupplierSum);

        BigDecimal oldAmountToUse = invoice.getAmountToUse();

        if (isOneInvoice) {
            if (invoice.getAmountToUse().compareTo(BigDecimal.ZERO) < 0) {
                invoice.setAmountToUse(oldAmountToUse.add(difference));
            } else {
                BigDecimal previousInvoiceAmountToUse = oldAmountToUse.add(oldSupplierSum);
                BigDecimal newInvoiceAmountToUse = previousInvoiceAmountToUse.subtract(oldSupplierSum);
                invoice.setAmountToUse(newInvoiceAmountToUse);
            }
        } else {
            BigDecimal previousInvoiceAmountToUse = oldAmountToUse.add(oldSupplierSum);
            BigDecimal newInvoiceAmountToUse = previousInvoiceAmountToUse.subtract(oldSupplierSum);
            invoice.setAmountToUse(newInvoiceAmountToUse);
            invoice.setUsed(false);
        }
        invoiceDao.save(invoice);
    }

    private void processUpdatingSupplierOrderDetails(OrderDetails orderDetails, UpdateOrderDetailsRequest updateOrderDetailsRequest) {
        orderDetails.setQuantity(updateOrderDetailsRequest.getNewQuantity());
        orderDetails.setSupplierSum(updateOrderDetailsRequest.getNewSupplierSum());
    }

}
