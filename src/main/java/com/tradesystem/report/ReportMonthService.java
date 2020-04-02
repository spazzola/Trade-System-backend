package com.tradesystem.report;

import com.tradesystem.cost.Cost;
import com.tradesystem.cost.CostDao;
import com.tradesystem.invoice.Invoice;
import com.tradesystem.invoice.InvoiceDao;
import com.tradesystem.order.Order;
import com.tradesystem.order.OrderDao;
import com.tradesystem.orderdetails.OrderDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ReportMonthService {

    private InvoiceDao invoiceDao;
    private OrderDao orderDao;
    private CostDao costDao;


    public ReportMonthService(InvoiceDao invoiceDao, OrderDao orderDao, CostDao costDao) {
        this.invoiceDao = invoiceDao;
        this.orderDao = orderDao;
        this.costDao = costDao;
    }


    @Transactional
    public Report generateMonthReport(int month, int year) {
        List<Cost> costs = costDao.getMonthCosts(month, year);

        BigDecimal soldedValue = sumMonthlySoldedValue(month, year);
        BigDecimal boughtValue = sumMonthlyBoughtValue(month, year);

        BigDecimal buyersNotUsedValue = calculateBuyersNotUsedAmount(month, year);
        BigDecimal suppliersNotUsedValue = calculateSuppliersNotUsedValue(month, year);

        BigDecimal soldedQuantity = sumMonthlySoldedQuantity(month, year);

        BigDecimal averageSold = calculateAverageSold(month, year, soldedQuantity);
        BigDecimal averagePurchase = calculateAveragePurchase(month, year, soldedQuantity);
        BigDecimal averageEarningsPerM3 = averageSold.subtract(averagePurchase);

        BigDecimal profit = calculateProfits(month, year, costs);

        return Report.builder()
                .soldedValue(soldedValue)
                .boughtValue(boughtValue)
                .buyersNotUsedValue(buyersNotUsedValue)
                .suppliersNotUsedValue(suppliersNotUsedValue)
                .soldedQuantity(soldedQuantity)
                .averageSold(averageSold)
                .averagePurchase(averagePurchase)
                .averageEarningsPerM3(averageEarningsPerM3)
                .profit(profit)
                .type(LocalDate.now().withMonth(month).getMonth().toString())
                .build();
    }

    private BigDecimal calculateBuyersNotUsedAmount(int month, int year) {
        Optional<List<Invoice>> notUsedInvoices = invoiceDao.getBuyersMonthNotUsedPositivesInvoices(month, year);
        BigDecimal notUsedValue = BigDecimal.valueOf(0);

        if (notUsedInvoices.isPresent()) {
            for (Invoice invoice : notUsedInvoices.get()) {
                notUsedValue = notUsedValue.add(invoice.getAmountToUse());
            }
        }
        return notUsedValue;
    }

    private BigDecimal calculateSuppliersNotUsedValue(int month, int year) {
        Optional<List<Invoice>> suppliersNotUsedInvoices = invoiceDao.getSuppliersMonthNotUsedInvoices(month, year);
        BigDecimal notUsedValue = BigDecimal.valueOf(0);

        if (suppliersNotUsedInvoices.isPresent()) {
            for (Invoice invoice : suppliersNotUsedInvoices.get()) {
                notUsedValue = notUsedValue.add(invoice.getAmountToUse());
            }
        }
        return notUsedValue;
    }

    private BigDecimal calculateAverageSold(int month, int year, BigDecimal quantity) {
       Set<Order> orders = orderDao.getMonthOrders(month, year);
       BigDecimal sum = BigDecimal.valueOf(0);

       for (Order order : orders) {
           List<OrderDetails> orderDetails = order.getOrderDetails();

           for (OrderDetails orderDetail : orderDetails) {
               sum = sum.add(orderDetail.getBuyerSum());
           }
       }
        return sum.divide(quantity, 2);
    }

    private BigDecimal calculateAveragePurchase(int month, int year, BigDecimal soldedQuantity) {
        Set<Order> orders = orderDao.getMonthOrders(month, year);
        BigDecimal sum = BigDecimal.valueOf(0);

        for (Order order : orders) {
            List<OrderDetails> orderDetails = order.getOrderDetails();

            for (OrderDetails orderDetail : orderDetails) {
                sum = sum.add(orderDetail.getSupplierSum());
            }
        }
        return sum.divide(soldedQuantity, 2);
    }

    private BigDecimal sumMonthlySoldedValue(int month, int year) {
        Set<Order> orders = orderDao.getMonthOrders(month, year);
        BigDecimal sum = BigDecimal.valueOf(0);

        for (Order order : orders) {
            List<OrderDetails> orderDetails = order.getOrderDetails();

            for (OrderDetails orderDetail : orderDetails) {
                sum = sum.add(orderDetail.getBuyerSum());
            }
        }
        return sum;
    }

    private BigDecimal sumMonthlyBoughtValue(int month, int year) {
        Set<Order> orders = orderDao.getMonthOrders(month, year);
        BigDecimal sum = BigDecimal.valueOf(0);

        for (Order order : orders) {
            List<OrderDetails> orderDetails = order.getOrderDetails();

            for (OrderDetails orderDetail : orderDetails) {
                sum = sum.add(orderDetail.getSupplierSum());
            }
        }
        return sum;
    }

    private BigDecimal sumMonthlySoldedQuantity(int month, int year) {
        Set<Order> orders = orderDao.getMonthOrders(month, year);
        BigDecimal totalQuantity = BigDecimal.valueOf(0);

        for (Order order : orders) {
            List<OrderDetails> orderDetails = order.getOrderDetails();

            for (OrderDetails orderDetail : orderDetails) {
                totalQuantity = totalQuantity.add(orderDetail.getQuantity());
            }
        }
        return totalQuantity;
    }

    private BigDecimal calculateProfits(int month, int year, List<Cost> costs) {
        BigDecimal sumCosts = BigDecimal.valueOf(0);
        Optional<List<Invoice>> invoices = invoiceDao.getBuyersMonthNegativeInvoices(month, year);

        if (invoices.isPresent()) {
            List<Invoice> negativeInvoices = invoices.get();
            for (Invoice invoice : negativeInvoices) {
                sumCosts = sumCosts.add(invoice.getAmountToUse());
            }
        }

        for (Cost cost : costs) {
            sumCosts = sumCosts.add(cost.getValue().multiply(BigDecimal.valueOf(-1)));
        }

        BigDecimal suppliersUsedAmount = calculateSuppliersUsedAmount(month, year);
        BigDecimal paidOrders = calculateBuyersUsedAmount(month, year);

        BigDecimal income = paidOrders.subtract(suppliersUsedAmount);
        BigDecimal profits = income.add(sumCosts);

        return profits;
    }

    private BigDecimal calculateSuppliersUsedAmount(int month, int year) {
        List<Invoice> invoices = invoiceDao.getSuppliersMonthInvoices(month, year);

        BigDecimal generalAmountToUse = BigDecimal.valueOf(0);
        for (Invoice invoice : invoices) {
            generalAmountToUse = generalAmountToUse.add(invoice.getValue());
        }

        BigDecimal amountToUse = BigDecimal.valueOf(0);
        for (Invoice invoice : invoices) {
            amountToUse = amountToUse.add(invoice.getAmountToUse());
        }

        return generalAmountToUse.subtract(amountToUse);
    }

    private BigDecimal calculateBuyersUsedAmount(int month, int year) {
        List<Invoice> notUsedInvoices = invoiceDao.getBuyersMonthIncomedInvoices(month, year);
        BigDecimal generalValue = BigDecimal.valueOf(0);
        BigDecimal notUsedAmount = BigDecimal.valueOf(0);

        for (Invoice invoice : notUsedInvoices) {
            generalValue = generalValue.add(invoice.getValue());
            notUsedAmount = notUsedAmount.add(invoice.getAmountToUse());
        }

        return generalValue.subtract(notUsedAmount);
    }
}
