package com.tradesystem.order;

import com.tradesystem.invoice.Invoice;
import com.tradesystem.invoice.InvoiceDao;
import com.tradesystem.invoice.InvoiceService;
import com.tradesystem.ordercomment.OrderComment;
import com.tradesystem.ordercomment.OrderCommentDao;
import com.tradesystem.ordercomment.OrderCommentService;
import com.tradesystem.price.PriceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Service
public class OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private PriceDao priceDao;

    @Autowired
    private InvoiceDao invoiceDao;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private OrderCommentService orderCommentService;

    @Autowired
    private OrderCommentDao orderCommentDao;


    //Glowna metoda
    public void payForOrderMainMethod(Order order) {
        BigDecimal buyerSum = calculateBuyerOrder(order);
        BigDecimal supplierSum = calculateSupplierOrder(order);

        order.setSum(buyerSum);
        orderDao.save(order);

        payForBuyerOrder2(order, buyerSum);
        payForSupplierOrder2(order, supplierSum);

    }

    private BigDecimal calculateBuyerOrder(Order order) {
        Long buyerId = order.getBuyer().getId();
        Long productId = order.getProductType().getId();

        BigDecimal quantity = order.getQuantity();
        BigDecimal price = priceDao.getBuyerPrice(buyerId, productId);

        return quantity.multiply(price);
    }

    private BigDecimal calculateSupplierOrder(Order order) {
        Long supplierId = order.getSupplier().getId();
        Long productId = order.getProductType().getId();

        BigDecimal quantity = order.getQuantity();
        BigDecimal price = priceDao.getSupplierPrice(supplierId, productId);

        return quantity.multiply(price);
    }

    private void payForSupplierOrder2(Order order, BigDecimal amount) {
        List<Invoice> invoices = invoiceDao.getSupplierNotUsedInvoices(order.getSupplier().getId());

        payForSupplierOrder(order, amount, invoices);
    }

    private void payForBuyerOrder2(Order order, BigDecimal amount) {
        List<Invoice> invoices = invoiceDao.getBuyerNotUsedInvoices(order.getBuyer().getId());

        payForBuyerOrder(order, amount, invoices);
    }


    private void payForSupplierOrder(Order order, BigDecimal amount, List<Invoice> invoices) {
        BigDecimal amountToPay = amount;
        List<String> invoiceNumbers = new ArrayList<>();
        int countedInvoices = 0;
        //OrderComment orderComment = new OrderComment();
        OrderComment orderComment;

        for (Invoice invoice : invoices) {
            BigDecimal invoiceValue = invoice.getAmountToUse();

            countedInvoices++;

            if (invoiceValue.subtract(amountToPay).compareTo(BigDecimal.ZERO) > 0) {

                invoice.setAmountToUse(invoiceValue.subtract(amountToPay));
                amount = BigDecimal.valueOf(0.0);

                saveInvoice(invoice, false);

                invoiceNumbers.add(invoice.getInvoiceNumber());

                if (countedInvoices > 1) {
                    //String previousComment = order.getComment();
                    String previousComment = order.getOrderComment().getSystemComment();
                   // order.setComment(previousComment + ", " + amountToPay + " z FV nr " + invoiceNumbers.get(countedInvoices - 1));
                    orderComment = order.getOrderComment();
                    orderComment.setSystemComment(previousComment + ", " + amountToPay + " z FV nr " + invoiceNumbers.get(countedInvoices - 1));
                    order.setOrderComment(orderComment);
                    orderCommentDao.save(orderComment);
                } else {
                    orderCommentService.addSupplierComment(order, amountToPay, invoice);
                }
                orderDao.save(order);

                break;

            } else if (invoiceValue.subtract(amountToPay).compareTo(BigDecimal.ZERO) < 0) {
                invoice.setAmountToUse(BigDecimal.valueOf(0.0));
                amount = amount.subtract(invoiceValue);
                amountToPay = amount;

                orderCommentService.addSupplierComment(order, invoiceValue, invoice);

                orderDao.save(order);
                saveInvoice(invoice, true);
                invoiceNumbers.add(invoice.getInvoiceNumber());

            } else if (invoiceValue.subtract(amountToPay).compareTo(BigDecimal.ZERO) == 0) {

                invoice.setAmountToUse(BigDecimal.valueOf(0.0));
                saveInvoice(invoice, true);

                orderCommentService.addSupplierComment(order, invoiceValue, invoice);

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
            createSupplierNegativeInvoice(negativeValue, order);

            orderCommentService.addLackAmountComment(order, negativeValue);

            orderDao.save(order);
        }

    }


    private void payForBuyerOrder(Order order, BigDecimal amount, List<Invoice> invoices) {
        BigDecimal amountToPay = order.getSum();
        List<String> invoiceNumbers = new ArrayList<>();
        int countedInvoices = 0;
        OrderComment orderComment;

        for (Invoice invoice : invoices) {
            BigDecimal invoiceValue = invoice.getAmountToUse();
            countedInvoices++;

            if (invoiceValue.subtract(amountToPay).compareTo(BigDecimal.ZERO) > 0) {

                invoice.setAmountToUse(invoiceValue.subtract(amountToPay));
                amount = BigDecimal.valueOf(0.0);

                saveInvoice(invoice, false);

                invoiceNumbers.add(invoice.getInvoiceNumber());

                if (countedInvoices > 1) {
                    String previousComment = order.getOrderComment().getSystemComment();

                    orderComment = order.getOrderComment();
                    orderComment.setSystemComment(previousComment + ", " + amountToPay + " z FV nr " + invoiceNumbers.get(countedInvoices - 1));
                    order.setOrderComment(orderComment);
                    orderCommentDao.save(orderComment);
                } else {
                    orderCommentService.addBuyerComment(order, amountToPay, invoice);
                }
                orderDao.save(order);
                break;

            } else if (invoiceValue.subtract(amountToPay).compareTo(BigDecimal.ZERO) < 0) {
                invoice.setAmountToUse(BigDecimal.valueOf(0.0));
                amount = amount.subtract(invoiceValue);
                amountToPay = amount;

                orderCommentService.addBuyerComment(order, invoiceValue, invoice);

                orderDao.save(order);
                saveInvoice(invoice, true);
                invoiceNumbers.add(invoice.getInvoiceNumber());

            } else if (invoiceValue.subtract(amountToPay).compareTo(BigDecimal.ZERO) == 0) {

                invoice.setAmountToUse(BigDecimal.valueOf(0.0));
                saveInvoice(invoice, true);

                orderCommentService.addBuyerComment(order, invoiceValue, invoice);

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
            createBuyerNegativeInvoice(negativeValue, order);

            orderCommentService.addLackAmountComment(order, negativeValue);

            orderDao.save(order);
        }

    }

    private void createBuyerNegativeInvoice(BigDecimal amount, Order order) {
        Invoice invoice = new Invoice();
        invoice.setAmountToUse(amount);
        invoice.setBuyer(order.getBuyer());
        invoiceDao.save(invoice);
    }

    private void createSupplierNegativeInvoice(BigDecimal amount, Order order) {
        Invoice invoice = new Invoice();
        invoice.setAmountToUse(amount);
        invoice.setSupplier(order.getSupplier());
        invoiceDao.save(invoice);
    }
    private void saveInvoice(Invoice invoice, boolean isUsed) {
        invoice.setUsed(isUsed);
        invoiceDao.save(invoice);
    }
}
