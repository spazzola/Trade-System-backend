package com.tradesystem.orderdetails;

import com.tradesystem.buyer.Buyer;
import com.tradesystem.invoice.Invoice;
import com.tradesystem.invoice.InvoiceDao;
import com.tradesystem.ordercomment.OrderComment;
import com.tradesystem.ordercomment.OrderCommentDao;
import com.tradesystem.ordercomment.OrderCommentService;
import com.tradesystem.payment.Payment;
import com.tradesystem.payment.PaymentDao;
import com.tradesystem.price.PriceDao;
import com.tradesystem.price.pricehistory.PriceHistoryService;
import com.tradesystem.product.Product;
import com.tradesystem.supplier.Supplier;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;


@Service
public class OrderDetailsService {


    private PriceDao priceDao;
    private InvoiceDao invoiceDao;
    private OrderDetailsDao orderDetailsDao;
    private OrderCommentService orderCommentService;
    private OrderCommentDao orderCommentDao;
    private PaymentDao paymentDao;
    private PriceHistoryService priceHistoryService;


    public OrderDetailsService(PriceDao priceDao, InvoiceDao invoiceDao, OrderDetailsDao orderDetailsDao,
                               OrderCommentService orderCommentService, OrderCommentDao orderCommentDao,
                               PaymentDao paymentDao, PriceHistoryService priceHistoryService) {
        this.priceDao = priceDao;
        this.invoiceDao = invoiceDao;
        this.orderDetailsDao = orderDetailsDao;
        this.orderCommentService = orderCommentService;
        this.orderCommentDao = orderCommentDao;
        this.paymentDao = paymentDao;
        this.priceHistoryService = priceHistoryService;
    }

    @Transactional
    public void calculateOrderDetail(OrderDetails orderDetails) {
        BigDecimal buyerSum = calculateBuyerOrder(orderDetails);
        BigDecimal supplierSum = calculateSupplierOrder(orderDetails);
        orderDetails.setBuyerSum(buyerSum);
        orderDetails.setSupplierSum(supplierSum);
        orderDetailsDao.save(orderDetails);

        if (orderDetails.isCreateBuyerInvoice()) {
            createBuyerInvoice(orderDetails, buyerSum);
            addSystemComment(orderDetails);
        } else {
            payForBuyerOrder2(orderDetails, buyerSum);
        }
        payForSupplierOrder2(orderDetails, supplierSum);

    }

    @Transactional
    public OrderDetails getOrderById(Long orderId) {
        return orderDetailsDao.findById(orderId)
                .orElseThrow(RuntimeException::new);
    }

    public BigDecimal calculateBuyerOrder(OrderDetails orderDetails) {
        Long buyerId = orderDetails.getOrder().getBuyer().getId();
        Long productId = orderDetails.getProduct().getId();

        BigDecimal quantity = orderDetails.getQuantity();

        BigDecimal price;

        if (orderDetails.getTypedPrice().doubleValue() > 0) {
            price = orderDetails.getTypedPrice();
        } else {
            price = priceDao.getBuyerPrice(buyerId, productId);
        }

        if (price != null) {
            Buyer buyer = orderDetails.getOrder().getBuyer();
            Supplier supplier = orderDetails.getOrder().getSupplier();
            Product product = orderDetails.getProduct();

            priceHistoryService.createPriceHistory(buyer, supplier, product, price);
            return quantity.multiply(price).setScale(2, RoundingMode.HALF_UP);
        } else {
            throw new RuntimeException("Kupiec nie ma ustawionej ceny dla tego produktu");
        }
    }

    private BigDecimal calculateSupplierOrder(OrderDetails orderDetails) {
        Long supplierId = orderDetails.getOrder().getSupplier().getId();
        Long productId = orderDetails.getProduct().getId();

        BigDecimal quantity = orderDetails.getQuantity();

        BigDecimal price = priceDao.getSupplierPrice(supplierId, productId);
        if (price != null) {
            return quantity.multiply(price).setScale(2, RoundingMode.HALF_UP);
        } else {
            throw new RuntimeException("Dostawca nie ma ustawionej ceny dla tego produktu");
        }

    }

    private void payForSupplierOrder2(OrderDetails orderDetails, BigDecimal amount) {
        Long supplierId = orderDetails.getOrder().getSupplier().getId();
        List<Invoice> invoices = invoiceDao.getSupplierNotUsedInvoices(supplierId);

        payForSupplierOrder(orderDetails, amount, invoices);
    }

    public void payForBuyerOrder2(OrderDetails orderDetails, BigDecimal amount) {
        Long buyerId = orderDetails.getOrder().getBuyer().getId();
        List<Invoice> invoices = invoiceDao.getBuyerNotUsedInvoices(buyerId);

        //invoices.sort(Comparator.comparing(Invoice::getId));

        payForBuyerOrder(orderDetails, amount, invoices);
    }


    private void payForSupplierOrder(OrderDetails orderDetails, BigDecimal amount, List<Invoice> invoices) {
        BigDecimal amountToPay = amount;
        amountToPay = amountToPay.setScale(2, RoundingMode.HALF_UP);
        List<String> invoiceNumbers = new ArrayList<>();
        int countedInvoices = 0;
        OrderComment orderComment;

        for (Invoice invoice : invoices) {
            createSupplierPayment(orderDetails, invoice);

            BigDecimal invoiceValue = invoice.getAmountToUse();

            countedInvoices++;

            if (invoiceValue.subtract(amountToPay).compareTo(BigDecimal.ZERO) > 0) {
                invoice.setAmountToUse(invoiceValue.subtract(amountToPay));
                amount = BigDecimal.valueOf(0.0);

                saveInvoice(invoice, false);

                invoiceNumbers.add(invoice.getInvoiceNumber());

                if (countedInvoices > 1) {
                    String previousComment = orderDetails.getOrderComment().getSystemComment();
                    orderComment = orderDetails.getOrderComment();
                    orderComment.setSystemComment(previousComment + ", " + amountToPay + " z FV nr " + invoiceNumbers.get(countedInvoices - 1));
                    orderDetails.setOrderComment(orderComment);
                    orderCommentDao.save(orderComment);
                } else {
                    orderCommentService.addSupplierComment(orderDetails, amountToPay, invoice);
                }
                orderDetailsDao.save(orderDetails);
                break;

            } else if (invoiceValue.subtract(amountToPay).compareTo(BigDecimal.ZERO) < 0) {
                invoice.setAmountToUse(BigDecimal.valueOf(0.0));
                amount = amount.subtract(invoiceValue);
                amountToPay = amount;

                orderCommentService.addSupplierComment(orderDetails, invoiceValue, invoice);

                orderDetailsDao.save(orderDetails);
                saveInvoice(invoice, true);
                invoiceNumbers.add(invoice.getInvoiceNumber());

            } else if (invoiceValue.subtract(amountToPay).compareTo(BigDecimal.ZERO) == 0) {

                invoice.setAmountToUse(BigDecimal.valueOf(0.0));
                saveInvoice(invoice, true);

                orderCommentService.addSupplierComment(orderDetails, invoiceValue, invoice);
                invoiceNumbers.add(invoice.getInvoiceNumber());
                amount = BigDecimal.valueOf(0.0);
                break;

            } else if (amount.compareTo(BigDecimal.ZERO) == 0) {
                saveInvoice(invoice, true);
                invoiceNumbers.add(invoice.getInvoiceNumber());
                break;

            } else {
                saveInvoice(invoice, false);
                break;
            }
        }

        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal negativeValue = amount.multiply(BigDecimal.valueOf(-1));
            createSupplierNegativeInvoice(negativeValue, orderDetails);

            Supplier supplier = orderDetails.getOrder().getSupplier();
            Invoice negativeInvoice = invoiceDao.getSupplierNegativeInvoice(supplier.getId())
                    .orElseThrow(RuntimeException::new);
            //Invoice negativeInvoice = optionalInvoice.get();
            orderCommentService.addLackAmountComment(orderDetails, negativeValue, negativeInvoice);
            orderDetailsDao.save(orderDetails);
        }

    }

    private void payForBuyerOrder(OrderDetails orderDetails, BigDecimal amount, List<Invoice> invoices) {
        BigDecimal amountToPay = orderDetails.getBuyerSum();
        amountToPay = amountToPay.setScale(2, RoundingMode.HALF_UP);
        List<String> invoiceNumbers = new ArrayList<>();
        int countedInvoices = 0;
        OrderComment orderComment;

        for (Invoice invoice : invoices) {
            createBuyerPayment(orderDetails, invoice);

            BigDecimal invoiceValue = invoice.getAmountToUse();
            countedInvoices++;

            if (invoiceValue.subtract(amountToPay).compareTo(BigDecimal.ZERO) > 0) {

                invoice.setAmountToUse(invoiceValue.subtract(amountToPay));
                amount = BigDecimal.valueOf(0.0);

                saveInvoice(invoice, false);

                invoiceNumbers.add(invoice.getInvoiceNumber());

                if (countedInvoices > 1) {
                    String previousComment = orderDetails.getOrderComment().getSystemComment();

                    orderComment = orderDetails.getOrderComment();
                    orderComment.setSystemComment(previousComment + ", " + amountToPay + " z FV nr " + invoiceNumbers.get(countedInvoices - 1));
                    orderDetails.setOrderComment(orderComment);
                    orderCommentDao.save(orderComment);
                } else {
                    orderCommentService.addBuyerComment(orderDetails, amountToPay, invoice);
                }
                orderDetailsDao.save(orderDetails);
                break;

            } else if (invoiceValue.subtract(amountToPay).compareTo(BigDecimal.ZERO) < 0) {
                invoice.setAmountToUse(BigDecimal.valueOf(0.0));
                amount = amount.subtract(invoiceValue);
                amountToPay = amount;

                orderCommentService.addBuyerComment(orderDetails, invoiceValue, invoice);
                orderDetailsDao.save(orderDetails);
                saveInvoice(invoice, true);
                invoiceNumbers.add(invoice.getInvoiceNumber());

            } else if (invoiceValue.subtract(amountToPay).compareTo(BigDecimal.ZERO) == 0) {

                invoice.setAmountToUse(BigDecimal.valueOf(0.0));
                saveInvoice(invoice, true);

                orderCommentService.addBuyerComment(orderDetails, invoiceValue, invoice);

                invoiceNumbers.add(invoice.getInvoiceNumber());
                amount = BigDecimal.valueOf(0.0);
                break;

            } else if (amount.compareTo(BigDecimal.ZERO) == 0) {
                saveInvoice(invoice, true);
                invoiceNumbers.add(invoice.getInvoiceNumber());
                break;

            } else {
                saveInvoice(invoice, false);
                break;
            }
        }

        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal negativeValue = amount.multiply(BigDecimal.valueOf(-1));
            createBuyerNegativeInvoice(negativeValue, orderDetails);

            Buyer buyer = orderDetails.getOrder().getBuyer();
            Invoice negativeInvoice = invoiceDao.getBuyerNegativeInvoice(buyer.getId())
                    .orElseThrow(RuntimeException::new);
            //Invoice negativeInvoice = optionalInvoice.get();
            orderCommentService.addLackAmountComment(orderDetails, negativeValue, negativeInvoice);
            orderDetailsDao.save(orderDetails);
        }

    }

    private void createBuyerNegativeInvoice(BigDecimal amount, OrderDetails orderDetails) {
        Buyer buyer = orderDetails.getOrder().getBuyer();
        Optional<Invoice> negativeInvoice = invoiceDao.getBuyerNegativeInvoice(buyer.getId());

        if (negativeInvoice.isPresent()) {
            Invoice invoice = negativeInvoice.get();
            BigDecimal invoiceValue = invoice.getValue();
            BigDecimal invoiceAmount = invoice.getAmountToUse();
            BigDecimal newInvoiceAmount = invoiceAmount.add(amount);
            invoice.setValue(invoiceValue.add(amount));
            invoice.setAmountToUse(newInvoiceAmount);
            invoiceDao.save(invoice);

            createBuyerPayment(orderDetails, invoice);
        } else {
            Invoice invoice = new Invoice();
            invoice.setInvoiceNumber("Negatywna");
            invoice.setAmountToUse(amount);
            invoice.setValue(amount);
            invoice.setDate(LocalDate.now());
            invoice.setBuyer(buyer);
            invoiceDao.save(invoice);

            createBuyerPayment(orderDetails, invoice);
        }

    }

    private void createSupplierNegativeInvoice(BigDecimal amount, OrderDetails orderDetails) {
        Supplier supplier = orderDetails.getOrder().getSupplier();
        Optional<Invoice> negativeInvoice = invoiceDao.getSupplierNegativeInvoice(supplier.getId());

        if (negativeInvoice.isPresent()) {
            Invoice invoice = negativeInvoice.get();
            BigDecimal invoiceAmount = invoice.getAmountToUse();
            BigDecimal newInvoiceAmount = invoiceAmount.add(amount);

            invoice.setAmountToUse(newInvoiceAmount);
            invoiceDao.save(invoice);

            createSupplierPayment(orderDetails, invoice);
        } else {
            Invoice invoice = new Invoice();
            invoice.setInvoiceNumber("Negatywna");
            invoice.setAmountToUse(amount);
            invoice.setValue(amount);
            invoice.setDate(LocalDate.now());
            invoice.setSupplier(supplier);
            invoiceDao.save(invoice);

            createSupplierPayment(orderDetails, invoice);
        }
    }

    private void saveInvoice(Invoice invoice, boolean isUsed) {
        invoice.setUsed(isUsed);
        invoiceDao.save(invoice);
    }

    private void createBuyerPayment(OrderDetails orderDetails, Invoice invoice) {
        Payment payment = new Payment();
        payment.setOrderDetails(orderDetails);
        payment.setBuyerInvoice(invoice);
        paymentDao.save(payment);
    }

    private void createSupplierPayment(OrderDetails orderDetails, Invoice invoice) {
        Payment payment = new Payment();
        payment.setOrderDetails(orderDetails);
        payment.setSupplierInvoice(invoice);
        paymentDao.save(payment);
    }

    public void createBuyerInvoice(OrderDetails orderDetails, BigDecimal buyerSum) {
        Invoice invoice = Invoice.builder()
                .buyer(orderDetails.getOrder().getBuyer())
                .date(orderDetails.getOrder().getDate())
                .amountToUse(BigDecimal.ZERO)
                .value(buyerSum)
                .isPaid(false)
                .isUsed(false)
                .isCreatedToOrder(true)
                .invoiceNumber(orderDetails.getInvoiceNumber())
                .build();
        invoiceDao.save(invoice);
        createBuyerPayment(orderDetails, invoice);
    }

    public void addSystemComment(OrderDetails orderDetails) {
        OrderComment orderComment = new OrderComment();
        orderComment.setSystemComment("Wygenerowano do zamówienia fakturę o nr " + orderDetails.getInvoiceNumber());
        orderDetails.setOrderComment(orderComment);
        orderDetailsDao.save(orderDetails);
    }
}
