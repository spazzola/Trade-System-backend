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
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ReportMonthService {

    private CostDao costDao;
    private OrderDao orderDao;
    private ReportDao reportDao;
    private InvoiceDao invoiceDao;
    private ReportService reportService;


    public ReportMonthService(InvoiceDao invoiceDao, OrderDao orderDao,
                              CostDao costDao, ReportDao reportDao, ReportService reportService) {
        this.invoiceDao = invoiceDao;
        this.orderDao = orderDao;
        this.costDao = costDao;
        this.reportDao = reportDao;
        this.reportService = reportService;
    }


    @Transactional
    public Report generateMonthReport(int month, int year) {
        BigDecimal sumCosts = calculateCosts(month, year);

        BigDecimal soldValue = sumMonthlySoldValue(month, year);
        BigDecimal boughtValue = sumMonthlyBoughtValue(month, year);

        BigDecimal buyersNotUsedValue = calculateBuyersNotUsedAmount(month, year);
        BigDecimal suppliersNotUsedValue = calculateSuppliersNotUsedValue(month, year);

        BigDecimal soldQuantity = sumMonthlySoldQuantity(month, year);

        BigDecimal averageSold = calculateAverageSold(month, year, soldQuantity);
        BigDecimal averagePurchase = calculateAveragePurchase(month, year, soldQuantity);
        BigDecimal averageEarningsPerM3 = averageSold.subtract(averagePurchase);

        BigDecimal profit = calculateProfits(month, year, sumCosts, boughtValue);

        String reportType = LocalDate.now().withMonth(month).getMonth().toString();

        Report report = Report.builder()
                .soldValue(soldValue)
                .boughtValue(boughtValue)
                .buyersNotUsedValue(buyersNotUsedValue)
                .suppliersNotUsedValue(suppliersNotUsedValue)
                .soldQuantity(soldQuantity)
                .averageSold(averageSold)
                .averagePurchase(averagePurchase)
                .averageEarningsPerM3(averageEarningsPerM3)
                .profit(profit)
                .sumCosts(sumCosts)
                .type(reportType)
                .build();

        if (reportService.checkIfReportExist(reportType)) {
            reportDao.save(report);
        }
        else {
            Report previousReport = reportDao.findByType(report.getType());
            previousReport.setSoldValue(soldValue);
            previousReport.setBoughtValue(boughtValue);
            previousReport.setBuyersNotUsedValue(buyersNotUsedValue);
            previousReport.setSuppliersNotUsedValue(suppliersNotUsedValue);
            previousReport.setSoldQuantity(soldQuantity);
            previousReport.setAverageSold(averageSold);
            previousReport.setAverageEarningsPerM3(averageEarningsPerM3);
            previousReport.setProfit(profit);
            previousReport.setSumCosts(sumCosts);
            previousReport.setType(reportType);

            reportDao.save(previousReport);
        }
        return report;
    }

    private BigDecimal calculateCosts(int month, int year) {
        List<Cost> costs = costDao.getMonthCosts(month, year);
        BigDecimal sumCosts = BigDecimal.valueOf(0);

        for (Cost cost : costs) {
            sumCosts = sumCosts.add(cost.getValue().multiply(BigDecimal.valueOf(-1)));
        }
        return sumCosts;
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
        return sum.divide(quantity, RoundingMode.HALF_EVEN);
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
        return sum.divide(soldedQuantity, RoundingMode.HALF_EVEN);
    }

    private BigDecimal sumMonthlySoldValue(int month, int year) {
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

    private BigDecimal sumMonthlySoldQuantity(int month, int year) {
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

    private BigDecimal calculateProfits(int month, int year, BigDecimal sumCosts, BigDecimal boughtValue) {
        BigDecimal buyersUsedAmount = calculateBuyersUsedAmount(month, year);

        BigDecimal income = buyersUsedAmount.subtract(boughtValue);
        BigDecimal profits = income.add(sumCosts);

        return profits;
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
