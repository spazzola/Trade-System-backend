package com.tradesystem.report;

import com.tradesystem.cost.Cost;
import com.tradesystem.invoice.Invoice;
import com.tradesystem.invoice.InvoiceDao;
import com.tradesystem.order.Order;
import com.tradesystem.order.OrderDao;
import com.tradesystem.orderdetails.OrderDetails;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class ReportYearService {

    @Autowired
    private InvoiceDao invoiceDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ReportService reportService;


    //TODO zrobic nowe selecty pod solo year
    public Report generateYearReport(int year, List<Cost> costs) {
        //1. Zsumować wartośc wysłanych zamówień - przewidywana kwota która powinna byc na koncie
        BigDecimal soldedValue = sumMonthlySoldedValue(year);

        //2. Zsumować wartość faktur zaliczkowych za dany miesiąc - sprawdzic roznice pomiedzy pkt 1 a 2
        // Porownac wartosc wysłanych zamowien z wartoscia fv które wpłyneły z uwzględnieniem deficytu/nadwyżki
        //2a. Uwzglednic wartosc nadwyzki/deficytu jaki zostal u supplierow
        //TODO incomes - uwzglednic + ktory jest przerzucany na next month
        BigDecimal monthlyIncomes = sumMonthlyIncomes(year);
        BigDecimal incomesDifference = soldedValue.subtract(monthlyIncomes);

        //3. Zsumować ilość wysłanych m3 = sumMonthlySoldedQuantity()
        BigDecimal soldedQuantity = sumMonthlySoldedQuantity(year);

        //4. Wyliczyc sredni zarobek na m3 (sprzedaz i kupno) =
        //   dodac kolumne isOplacone do orderDetails? zeby operowac na produktach ktore zostaly oplacone
        BigDecimal averageSold = calculateAverageSold(year, soldedQuantity);
        BigDecimal averagePurchase = calculateAvaragePurchase(year, soldedQuantity);
        BigDecimal averageEarningsPerM3 = averageSold.subtract(averagePurchase);

        //7. Wyliczyc zysk
        BigDecimal profit = reportService.calculateProfits(averageEarningsPerM3, soldedQuantity, costs);

        //8. Zapisac wszystko do obiektu

        return new Report.ReportBuilder()
                .setSoldedValue(soldedValue)
                .setSoldedQuantity(soldedQuantity)
                .setIncomes(monthlyIncomes)
                .setIncomesDifference(incomesDifference)
                .setAverageSold(averageSold)
                .setAveragePurchase(averagePurchase)
                .setAverageEarningsPerM3(averageEarningsPerM3)
                .setProfit(profit)
                .setType(String.valueOf(LocalDate.now().withYear(year).getYear()))
                .build();
    }

    private BigDecimal calculateAveragePurchase(int year, BigDecimal quantity) {
        List<Invoice> usedInvoices = invoiceDao.getSuppliersYearUsedInvoices(year);
        BigDecimal usedAmount = BigDecimal.valueOf(0);

        for (Invoice invoice : usedInvoices) {
            usedAmount = usedAmount.add(invoice.getValue());
        }

        List<Invoice> notUsedInvoices = invoiceDao.getSuppliersYearNotUsedInvoices(year);

        for (Invoice invoice : notUsedInvoices) {
            BigDecimal invoiceValue = invoice.getValue();
            BigDecimal notUsedValue = invoice.getAmountToUse();
            BigDecimal usedValue = invoiceValue.subtract(notUsedValue);

            usedAmount = usedAmount.add(usedValue);
        }

        return usedAmount.divide(quantity);
    }

    private BigDecimal calculateAverageSold(int year, BigDecimal quantity) {
        List<Invoice> usedInvoices = invoiceDao.getBuyersYearUsedInvoices(year);
        BigDecimal usedAmount = BigDecimal.valueOf(0);

        for (Invoice invoice : usedInvoices) {
            usedAmount = usedAmount.add(invoice.getValue());
        }

        List<Invoice> notUsedInvoices = invoiceDao.getBuyersYearNotUsedPositivesInvoices(year);

        for (Invoice invoice : notUsedInvoices) {
            BigDecimal invoiceValue = invoice.getValue();
            BigDecimal notUsedValue = invoice.getAmountToUse();
            BigDecimal usedValue = invoiceValue.subtract(notUsedValue);

            usedAmount = usedAmount.add(usedValue);
        }

        Optional<List<Invoice>> negativeInvoices = invoiceDao.getBuyersYearNotUsedNegativeInvoices(year);

        if (negativeInvoices.isPresent()) {
            List<Invoice> invoices = negativeInvoices.get();
            BigDecimal converter = BigDecimal.valueOf(-1);
            for (Invoice invoice : invoices) {
                usedAmount = usedAmount.add(invoice.getAmountToUse().multiply(converter));
            }
        }

        return usedAmount.divide(quantity);
    }

    private BigDecimal sumMonthlyIncomes(int year) {
        List<Invoice> invoices = invoiceDao.getBuyersYearIncomedInvoices(year);
        BigDecimal incomedValue = BigDecimal.valueOf(0);

        for (Invoice invoice : invoices) {
            incomedValue = incomedValue.add(invoice.getValue());
        }
        return incomedValue;
    }

    private BigDecimal calculateAvaragePurchase(int year, BigDecimal soldedQuantity) {
        BigDecimal usedAmount = calculateSuppliersMonthUsedValue(year);

        return usedAmount.divide(soldedQuantity);
    }

    private BigDecimal calculateSuppliersMonthUsedValue(int year) {
        //czemu nazwa metody monthusedvalue a wyciamy wszystkie fv? do sprawdzenia
        //jednak raczej pasuje, wyciaganie all fv, obliczenie total value a nastepnie odjecie tego co zostalo
        List<Invoice> invoices = invoiceDao.getSuppliersYearInvoices(year);
        BigDecimal totalValue = BigDecimal.valueOf(0);
        BigDecimal notUsedAmount = BigDecimal.valueOf(0);
        for (Invoice invoice : invoices) {
            totalValue = totalValue.add(invoice.getValue());
            notUsedAmount = notUsedAmount.add(invoice.getAmountToUse());
        }

        return totalValue.subtract(notUsedAmount);
    }

    private BigDecimal sumMonthlySoldedValue(int year) {
        List<Order> orders = orderDao.getYearOrders(year);
        BigDecimal sum = BigDecimal.valueOf(0);

        for (Order order : orders) {
            List<OrderDetails> orderDetails = order.getOrderDetails();

            for (OrderDetails orderDetail : orderDetails) {
                sum = sum.add(orderDetail.getBuyerSum());
            }
        }
        return sum;
    }

    private BigDecimal sumMonthlySoldedQuantity(int year) {
        List<Order> orders = orderDao.getYearOrders(year);
        BigDecimal totalQuantity = BigDecimal.valueOf(0);

        for (Order order : orders) {
            List<OrderDetails> orderDetails = order.getOrderDetails();

            for (OrderDetails orderDetail : orderDetails) {
                totalQuantity = totalQuantity.add(orderDetail.getQuantity());
            }
        }
        return totalQuantity;
    }
}
