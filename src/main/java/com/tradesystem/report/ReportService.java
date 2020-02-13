package com.tradesystem.report;

import com.tradesystem.invoice.Invoice;
import com.tradesystem.invoice.InvoiceDao;
import com.tradesystem.order.Order;
import com.tradesystem.order.OrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

@Service
public class ReportService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private InvoiceDao invoiceDao;



    public void generateRaport(int month, int year) {
        //1. Zsumować wartośc wysłanych zamówień = sumMonthlyOrderedValue()
        //2. Zsumować ilość wysłanych m3 = sumMonthlyOrderedQuantity()
        //3. Wyliczyc sredni zarobek na m3 (sprzedaz i kupno) =
        //   dodac kolumne isOplacone do orders? zeby operowac na produktach ktore zostaly oplacone
        //4. Zsumować wartość faktur zaliczkowych za dany miesiąc = sumMonthlyIncomes()

        //5. Porownac wartosc wysłanych zamowien z wartoscia fv które wpłyneły z uwzględnieniem deficytu/nadwyżki
        //6. Uwzglednic wartosc nadwyzki/deficytu jaki zostal u supplierow
        //7. Wyliczyc zysk (to co WPŁYNĘŁO - to co kupiliśmy - koszta)
    }



    private BigDecimal calcualteAvarageSold(int month, int year) {
        List<Order> orders = orderDao.getMonthlyOrders(month, year);

        BigDecimal totalSum = BigDecimal.valueOf(0);
        for (Order order : orders) {
            totalSum = totalSum.add(order.getSum());
        }

        BigDecimal totalQuantity = BigDecimal.valueOf(0);
        for (Order order : orders) {
            totalQuantity = totalQuantity.add(order.getQuantity());
        }
        return totalSum.divide(totalQuantity);
    }

    private BigDecimal calculateAvaragePurchase(int month, int year) {
        List<Order> orders = orderDao.getMonthlyOrders(month, year);

        BigDecimal totalQuantity = BigDecimal.valueOf(0);
        for (Order order : orders) {
            totalQuantity = totalQuantity.add(order.getQuantity());
        }

        //dokonczyc
        return null;
    }

    private BigDecimal sumMonthlyOrderedValue(int month, int year) {
        List<Order> orders = orderDao.getMonthlyOrders(month, year);
        BigDecimal sum = BigDecimal.valueOf(0);

        for (Order order : orders) {
            sum = sum.add(order.getSum());
        }
        return sum;
    }

    private BigDecimal sumMonthlyOrderedQuantity(int month, int year) {
        List<Order> orders = orderDao.getMonthlyOrders(month, year);
        BigDecimal sum = BigDecimal.valueOf(0);

        for (Order order : orders) {
            sum = sum.add(order.getQuantity());
        }
        return sum;
    }

    private BigDecimal sumMonthlyIncomes(int month, int year) {
        List<Invoice> invoices = invoiceDao.getBuyersIncomedInvoices(month, year);
        BigDecimal incomedValue = BigDecimal.valueOf(0);

        for (Invoice invoice : invoices) {
            incomedValue = incomedValue.add(invoice.getValue());
        }
        return null;
    }


}
