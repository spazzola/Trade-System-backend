package com.tradesystem.report;

import com.tradesystem.cost.Cost;
import com.tradesystem.invoice.InvoiceDao;
import com.tradesystem.order.OrderDao;
import com.tradesystem.orderdetails.OrderDetailsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ReportService {

    @Autowired
    private OrderDetailsDao orderDetailsDao;

    @Autowired
    private InvoiceDao invoiceDao;

    @Autowired
    private OrderDao orderDao;



    public BigDecimal calculateProfits(BigDecimal averageEarningsPerM3, BigDecimal soldedQuantity, List<Cost> costs) {
        BigDecimal sumCosts = BigDecimal.valueOf(0);

        for (Cost cost : costs) {
            sumCosts = sumCosts.add(cost.getValue());
        }

        BigDecimal income = averageEarningsPerM3.multiply(soldedQuantity);

        return income.subtract(sumCosts);
    }


}
