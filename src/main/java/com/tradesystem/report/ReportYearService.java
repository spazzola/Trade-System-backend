package com.tradesystem.report;

import com.tradesystem.cost.Cost;
import com.tradesystem.cost.CostDao;
import com.tradesystem.invoice.Invoice;
import com.tradesystem.invoice.InvoiceDao;
import com.tradesystem.order.Order;
import com.tradesystem.order.OrderDao;
import com.tradesystem.orderdetails.OrderDetails;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ReportYearService {


    private InvoiceDao invoiceDao;
    private OrderDao orderDao;
    private CostDao costDao;


    public ReportYearService(InvoiceDao invoiceDao, OrderDao orderDao, CostDao costDao) {
        this.invoiceDao = invoiceDao;
        this.orderDao = orderDao;
        this.costDao = costDao;
    }


    //TODO zrobic nowe selecty pod solo year
    public Report generateYearReport(int year) {
        List<Cost> costs = costDao.getYearCosts(year);

        //1. Zsumować wartośc wysłanych zamówień - przewidywana kwota która powinna byc na koncie
        BigDecimal soldedValue = sumYearSoldedValue(year);
        BigDecimal boughtValue = sumYearBoughtValue(year);

        BigDecimal suppliersNotUsedValue = calculateSuppliersNotUsedValue(year);
        BigDecimal buyersNotUsedValue = calculateBuyersNotUsedAmount(year);

        //2. Zsumować wartość faktur zaliczkowych za dany miesiąc - sprawdzic roznice pomiedzy pkt 1 a 2
        // Porownac wartosc wysłanych zamowien z wartoscia fv które wpłyneły z uwzględnieniem deficytu/nadwyżki
        //2a. Uwzglednic wartosc nadwyzki/deficytu jaki zostal u supplierow
        //TODO incomes - uwzglednic + ktory jest przerzucany na next month
        BigDecimal yearIncomes = sumYearIncomes(year);

        //3. Zsumować ilość wysłanych m3 = sumMonthlySoldedQuantity()
        BigDecimal soldedQuantity = sumMonthlySoldedQuantity(year);

        //4. Wyliczyc sredni zarobek na m3 (sprzedaz i kupno) =
        //   dodac kolumne isOplacone do orderDetails? zeby operowac na produktach ktore zostaly oplacone
        BigDecimal averageSold = calculateAverageSold(year, soldedQuantity);
        BigDecimal averagePurchase = calculateAvaragePurchase(year, soldedQuantity);
        BigDecimal averageEarningsPerM3 = averageSold.subtract(averagePurchase);

        //7. Wyliczyc zysk
        BigDecimal profit = calculateProfits(year, costs);

        //8. Zapisac wszystko do obiektu
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
                .type(String.valueOf(year))
                .build();
    }

    private BigDecimal calculateBuyersNotUsedAmount(int year) {
        Optional<List<Invoice>> notUsedInvoices = invoiceDao.getBuyersYearNotUsedPositivesInvoices(year);
        BigDecimal notUsedValue = BigDecimal.valueOf(0);

        if (notUsedInvoices.isPresent()) {
            for (Invoice invoice : notUsedInvoices.get()) {
                notUsedValue = notUsedValue.add(invoice.getAmountToUse());
            }
        }
        return notUsedValue;
    }

    private BigDecimal calculateSuppliersNotUsedValue(int year) {
        Optional<List<Invoice>> suppliersNotUsedInvoices = invoiceDao.getSuppliersYearNotUsedInvoices(year);
        BigDecimal notUsedValue = BigDecimal.valueOf(0);

        if (suppliersNotUsedInvoices.isPresent()) {
            for (Invoice invoice : suppliersNotUsedInvoices.get()) {
                notUsedValue = notUsedValue.add(invoice.getAmountToUse());
            }
        }
        return notUsedValue;
    }

    private BigDecimal sumYearSoldedValue(int year) {
        Set<Order> orders = orderDao.getYearOrders(year);
        BigDecimal sum = BigDecimal.valueOf(0);

        for (Order order : orders) {
            List<OrderDetails> orderDetails = order.getOrderDetails();

            for (OrderDetails orderDetail : orderDetails) {
                sum = sum.add(orderDetail.getBuyerSum());
            }
        }
        return sum;
    }

    private BigDecimal sumYearBoughtValue(int year) {
        Set<Order> orders = orderDao.getYearOrders(year);
        BigDecimal sum = BigDecimal.valueOf(0);

        for (Order order : orders) {
            List<OrderDetails> orderDetails = order.getOrderDetails();

            for (OrderDetails orderDetail : orderDetails) {
                sum = sum.add(orderDetail.getSupplierSum());
            }
        }
        return sum;
    }

    //mega podobna metoda to tej samej z month report, roznica w liscie orderdsow
    private BigDecimal calculateAverageSold(int year, BigDecimal quantity) {
        Set<Order> orders = orderDao.getYearOrders(year);
        BigDecimal sum = BigDecimal.valueOf(0);

        for (Order order : orders) {
            List<OrderDetails> orderDetails = order.getOrderDetails();

            for (OrderDetails orderDetail : orderDetails) {
                sum = sum.add(orderDetail.getBuyerSum());
            }
        }
        return sum.divide(quantity, 2);
    }

    private BigDecimal sumYearIncomes(int year) {
        List<Invoice> invoices = invoiceDao.getBuyersYearIncomedInvoices(year);
        BigDecimal incomedValue = BigDecimal.valueOf(0);

        for (Invoice invoice : invoices) {
            incomedValue = incomedValue.add(invoice.getValue());
        }
        return incomedValue;
    }

    private BigDecimal calculateAvaragePurchase(int year, BigDecimal soldedQuantity) {
        Set<Order> orders = orderDao.getYearOrders(year);
        BigDecimal sum = BigDecimal.valueOf(0);

        for (Order order : orders) {
            List<OrderDetails> orderDetails = order.getOrderDetails();

            for (OrderDetails orderDetail : orderDetails) {
                sum = sum.add(orderDetail.getSupplierSum());
            }
        }
        return sum.divide(soldedQuantity, 2);
    }

    private BigDecimal sumMonthlySoldedQuantity(int year) {
        Set<Order> orders = orderDao.getYearOrders(year);
        BigDecimal totalQuantity = BigDecimal.valueOf(0);

        for (Order order : orders) {
            List<OrderDetails> orderDetails = order.getOrderDetails();

            for (OrderDetails orderDetail : orderDetails) {
                totalQuantity = totalQuantity.add(orderDetail.getQuantity());
            }
        }
        return totalQuantity;
    }

    private BigDecimal calculateSuppliersUsedAmount(int year) {
        List<Invoice> invoices = invoiceDao.getSuppliersYearInvoices(year);

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

    private BigDecimal calculateProfits(int year, List<Cost> costs) {
        BigDecimal sumCosts = BigDecimal.valueOf(0);
        Optional<List<Invoice>> invoices = invoiceDao.getBuyersYearNegativeInvoices(year);

        if (invoices.isPresent()) {
            List<Invoice> negativeInvoices = invoices.get();
            for (Invoice invoice : negativeInvoices) {
                sumCosts = sumCosts.add(invoice.getAmountToUse());

            }
        }

        for (Cost cost : costs) {
            sumCosts = sumCosts.add(cost.getValue());
        }
        //to co my zaplacilismy
        BigDecimal suppliersUsedAmount = calculateSuppliersUsedAmount(year);
        BigDecimal paidOrders = calculateBuyersUsedAmount(year);


        BigDecimal income = paidOrders.subtract(suppliersUsedAmount);
        BigDecimal profits = income.add(sumCosts);

        return profits;
    }

    private BigDecimal calculateBuyersUsedAmount(int year) {
        List<Invoice> notUsedInvoices = invoiceDao.getBuyersYearIncomedInvoices(year);
        BigDecimal generalValue = BigDecimal.valueOf(0);
        BigDecimal notUsedAmount = BigDecimal.valueOf(0);

        for (Invoice invoice : notUsedInvoices) {
            generalValue = generalValue.add(invoice.getValue());
            notUsedAmount = notUsedAmount.add(invoice.getAmountToUse());
        }

        return generalValue.subtract(notUsedAmount);
    }

}
