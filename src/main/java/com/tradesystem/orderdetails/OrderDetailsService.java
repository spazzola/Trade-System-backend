package com.tradesystem.orderdetails;

import com.tradesystem.buyer.Buyer;
import com.tradesystem.buyer.BuyerDao;
import com.tradesystem.invoice.Invoice;
import com.tradesystem.invoice.InvoiceDao;
import com.tradesystem.invoice.InvoiceService;
import com.tradesystem.ordercomment.OrderComment;
import com.tradesystem.ordercomment.OrderCommentDao;
import com.tradesystem.ordercomment.OrderCommentService;
import com.tradesystem.price.PriceDao;
import com.tradesystem.supplier.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class OrderDetailsService {

    @Autowired
    private OrderDetailsDao orderDetailsDao;

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

    @Autowired
    private BuyerDao buyerDao;


    @Transactional
    public void calculateOrderDetail(OrderDetails orderDetails) {
        BigDecimal buyerSum = calculateBuyerOrder(orderDetails);
        BigDecimal supplierSum = calculateSupplierOrder(orderDetails);

        orderDetails.setBuyerSum(buyerSum);
        orderDetails.setSupplierSum(supplierSum);
        orderDetailsDao.save(orderDetails);

        payForBuyerOrder2(orderDetails, buyerSum);
        payForSupplierOrder2(orderDetails, supplierSum);

    }

    private BigDecimal calculateBuyerOrder(OrderDetails orderDetails) {
        Long buyerId = orderDetails.getOrder().getBuyer().getId();
        Long productId = orderDetails.getProduct().getId();

        BigDecimal quantity = orderDetails.getQuantity();
        BigDecimal price = priceDao.getBuyerPrice(buyerId, productId);

        return quantity.multiply(price);
    }

    private BigDecimal calculateSupplierOrder(OrderDetails orderDetails) {
        Long supplierId = orderDetails.getOrder().getSupplier().getId();
        Long productId = orderDetails.getProduct().getId();

        BigDecimal quantity = orderDetails.getQuantity();
        BigDecimal price = priceDao.getSupplierPrice(supplierId, productId);

        return quantity.multiply(price);
    }

    private void payForSupplierOrder2(OrderDetails orderDetails, BigDecimal amount) {
        Long supplierId = orderDetails.getOrder().getSupplier().getId();
        List<Invoice> invoices = invoiceDao.getSupplierNotUsedInvoices(supplierId);

        payForSupplierOrder(orderDetails, amount, invoices);
    }

    private void payForBuyerOrder2(OrderDetails orderDetails, BigDecimal amount) {
        Long buyerId = orderDetails.getOrder().getBuyer().getId();
        List<Invoice> invoices = invoiceDao.getBuyerNotUsedInvoices(buyerId);

        payForBuyerOrder(orderDetails, amount, invoices);
    }


    private void payForSupplierOrder(OrderDetails orderDetails, BigDecimal amount, List<Invoice> invoices) {
        BigDecimal amountToPay = amount;
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

                System.out.println("raz");
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
            Optional<Invoice> optionalInvoice = invoiceDao.getSupplierNegativeInvoice(supplier.getId());
            Invoice negativeInvoice = optionalInvoice.get();
            orderCommentService.addLackAmountComment(orderDetails, negativeValue, negativeInvoice);
            orderDetailsDao.save(orderDetails);
        }

    }


    private void payForBuyerOrder(OrderDetails orderDetails, BigDecimal amount, List<Invoice> invoices) {
        BigDecimal amountToPay = orderDetails.getBuyerSum();
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
            Optional<Invoice> optionalInvoice = invoiceDao.getBuyerNegativeInvoice(buyer.getId());
            Invoice negativeInvoice = optionalInvoice.get();
            orderCommentService.addLackAmountComment(orderDetails, negativeValue, negativeInvoice);

            orderDetailsDao.save(orderDetails);
        }

    }

    private void createBuyerNegativeInvoice(BigDecimal amount, OrderDetails orderDetails) {
        Buyer buyer = orderDetails.getOrder().getBuyer();
        Optional<Invoice> negativeInvoice = invoiceDao.getBuyerNegativeInvoice(buyer.getId());

        if (negativeInvoice.isPresent()) {
            Invoice invoice = negativeInvoice.get();
            BigDecimal invoiceAmount = invoice.getAmountToUse();
            BigDecimal newInvoiceAmount = invoiceAmount.add(amount);

            invoice.setAmountToUse(newInvoiceAmount);
            invoiceDao.save(invoice);
        } else {
            Invoice invoice = new Invoice();
            invoice.setAmountToUse(amount);
            invoice.setDate(LocalDate.now());
            invoice.setBuyer(buyer);
            invoiceDao.save(invoice);
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
        } else {
            Invoice invoice = new Invoice();
            invoice.setAmountToUse(amount);
            invoice.setValue(BigDecimal.valueOf(0));
            invoice.setDate(LocalDate.now());
            invoice.setSupplier(orderDetails.getOrder().getSupplier());
            invoiceDao.save(invoice);
        }
    }
    private void saveInvoice(Invoice invoice, boolean isUsed) {
        invoice.setUsed(isUsed);
        invoiceDao.save(invoice);
    }
}
