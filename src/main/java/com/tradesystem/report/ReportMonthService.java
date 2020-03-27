package com.tradesystem.report;

import com.tradesystem.cost.Cost;
import com.tradesystem.invoice.Invoice;
import com.tradesystem.invoice.InvoiceDao;
import com.tradesystem.order.Order;
import com.tradesystem.order.OrderDao;
import com.tradesystem.orderdetails.OrderDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ReportMonthService {

    @Autowired
    private InvoiceDao invoiceDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ReportService reportService;



    public Report generateMonthReport(int month, int year, List<Cost> costs) {
        //1. Zsumować wartośc wysłanych zamówień - przewidywana kwota która powinna byc na koncie
        BigDecimal soldedValue = sumMonthlySoldedValue(month, year);

        //2. Zsumować wartość faktur zaliczkowych za dany miesiąc - sprawdzic roznice pomiedzy pkt 1 a 2
        // Porownac wartosc wysłanych zamowien z wartoscia fv które wpłyneły z uwzględnieniem deficytu/nadwyżki
        //2a. Uwzglednic wartosc nadwyzki/deficytu jaki zostal u supplierow
        //TODO incomes - uwzglednic + ktory jest przerzucany na next month
        BigDecimal monthlyIncomes = sumMonthlyIncomes(month, year);
        BigDecimal incomesDifference = monthlyIncomes.subtract(soldedValue);

        //3. Zsumować ilość wysłanych m3 = sumMonthlySoldedQuantity()
        BigDecimal soldedQuantity = sumMonthlySoldedQuantity(month, year);

        //4. Wyliczyc sredni zarobek na m3 (sprzedaz i kupno) =
        //   dodac kolumne isOplacone do orderDetails? zeby operowac na produktach ktore zostaly oplacone
        BigDecimal averageSold = calculateAverageSold(month, year, soldedQuantity);
        BigDecimal averagePurchase = calculateAvaragePurchase(month, year, soldedQuantity);
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
                .setType(LocalDate.now().withMonth(month).getMonth().toString())
                .build();
    }


    //TODO do naprawy sposob obliczania sredniej sprzedazy. nie jest uwzgledniony przypadek w ktorym
    // jest wykorzystana czesc danej faktury, w sensie jesli z fv o wartosci 10 000 jest wykorzystane 2000
    // to isUsed = false i fv nie jest wyciagana do obliczen a powinna (to samo spostrzezenie w InvoiceDao)
    //jednak juz chyba jest to zrobione w drugiej czesci metody, do przeanalizowania
    private BigDecimal calculateAveragePurchase(int month, int year, BigDecimal quantity) {
        List<Invoice> usedInvoices = invoiceDao.getSuppliersMonthUsedInvoices(month, year);
        BigDecimal usedAmount = BigDecimal.valueOf(0);

        for (Invoice invoice : usedInvoices) {
            usedAmount = usedAmount.add(invoice.getValue());
        }

        List<Invoice> notUsedInvoices = invoiceDao.getSuppliersMonthNotUsedInvoices(month, year);

        for (Invoice invoice : notUsedInvoices) {
            BigDecimal invoiceValue = invoice.getValue();
            BigDecimal notUsedValue = invoice.getAmountToUse();
            BigDecimal usedValue = invoiceValue.subtract(notUsedValue);

            usedAmount = usedAmount.add(usedValue);
        }

        return usedAmount.divide(quantity);
    }

    private BigDecimal calculateAverageSold(int month, int year, BigDecimal quantity) {
        List<Invoice> usedInvoices = invoiceDao.getBuyersMonthUsedInvoices(month, year);
        BigDecimal usedAmount = BigDecimal.valueOf(0);

        for (Invoice invoice : usedInvoices) {
            usedAmount = usedAmount.add(invoice.getValue());
        }

        List<Invoice> notUsedInvoices = invoiceDao.getBuyersMonthNotUsedPositivesInvoices(month, year);

        for (Invoice invoice : notUsedInvoices) {
            BigDecimal invoiceValue = invoice.getValue();
            BigDecimal notUsedValue = invoice.getAmountToUse();
            BigDecimal usedValue = invoiceValue.subtract(notUsedValue);

            usedAmount = usedAmount.add(usedValue);
        }

        Optional<List<Invoice>> negativeInvoices = invoiceDao.getBuyersMonthNotUsedNegativeInvoices(month, year);

        if (negativeInvoices.isPresent()) {
            List<Invoice> invoices = negativeInvoices.get();
            BigDecimal converter = BigDecimal.valueOf(-1);
            for (Invoice invoice : invoices) {
                usedAmount = usedAmount.add(invoice.getAmountToUse().multiply(converter));
            }
        }

        return usedAmount.divide(quantity, 2);
    }

    private BigDecimal sumMonthlyIncomes(int month, int year) {
        List<Invoice> invoices = invoiceDao.getBuyersMonthIncomedInvoices(month, year);
        BigDecimal incomedValue = BigDecimal.valueOf(0);

        for (Invoice invoice : invoices) {
            incomedValue = incomedValue.add(invoice.getValue());
        }
        return incomedValue;
    }

    private BigDecimal calculateAvaragePurchase(int month, int year, BigDecimal soldedQuantity) {
        BigDecimal usedAmount = calculateSuppliersMonthUsedValue(month, year);

        return usedAmount.divide(soldedQuantity, 2);
    }

    private BigDecimal calculateSuppliersMonthUsedValue(int month, int year) {
        List<Invoice> invoices = invoiceDao.getSuppliersMonthInvoices(month, year);
        BigDecimal totalValue = BigDecimal.valueOf(0);
        BigDecimal notUsedAmount = BigDecimal.valueOf(0);
        for (Invoice invoice : invoices) {
            totalValue = totalValue.add(invoice.getValue());
            notUsedAmount = notUsedAmount.add(invoice.getAmountToUse());
        }

        return totalValue.subtract(notUsedAmount);
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

}
