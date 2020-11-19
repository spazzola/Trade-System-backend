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
import java.util.NoSuchElementException;
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
        BigDecimal soldQuantity = sumMonthlySoldQuantity(month, year);
        BigDecimal averageSold = calculateAverageSold(month, year, soldQuantity);
        BigDecimal averagePurchase = calculateAveragePurchase(month, year, soldQuantity);
        BigDecimal averageEarningsPerM3 = averageSold.subtract(averagePurchase);
        BigDecimal income = soldValue.subtract(boughtValue);
        BigDecimal buyersNotPaidInvoices = calculateBuyersNotPaidInvoices(month, year);

        String reportType = LocalDate.now().withMonth(month).getMonth().toString();

        Report report = Report.builder()
                .soldValue(soldValue)
                .boughtValue(boughtValue)
                .soldQuantity(soldQuantity)
                .averageEarningsPerM3(averageEarningsPerM3)
                .income(income)
                .buyersNotPaidInvoices(buyersNotPaidInvoices)
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
            previousReport.setSoldQuantity(soldQuantity);
            previousReport.setAverageEarningsPerM3(averageEarningsPerM3);
            previousReport.setIncome(income);
            previousReport.setSumCosts(sumCosts);
            previousReport.setBuyersNotPaidInvoices(buyersNotPaidInvoices);
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

    private BigDecimal calculateBuyersNotPaidInvoices(int month, int year) {
        BigDecimal result = new BigDecimal(0);

        Optional<List<Invoice>> notPaidInvoicesNotCreatedToOrder = invoiceDao.getBuyersMonthNotPaidInvoicesNotCreatedToOrder(month, year);

        if (notPaidInvoicesNotCreatedToOrder.isPresent()) {
            for (Invoice invoice : notPaidInvoicesNotCreatedToOrder.get()) {
                result = result.add(invoice.getAmountToUse());
            }
        }


        Optional<List<Invoice>> notPaidInvoicesCreatedToOrder = invoiceDao.getBuyersMonthNotPaidInvoicesCreatedToOrder(month, year);

        if (notPaidInvoicesCreatedToOrder.isPresent()) {
            for (Invoice invoice : notPaidInvoicesCreatedToOrder.get()) {
                result = result.add(invoice.getValue());
            }
        }
        return result;
    }

}
