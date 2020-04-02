package com.tradesystem.report;

import com.tradesystem.cost.Cost;
import com.tradesystem.invoice.Invoice;
import com.tradesystem.invoice.InvoiceDao;
import com.tradesystem.order.OrderDao;
import com.tradesystem.orderdetails.OrderDetailsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ReportService {

    @Autowired
    private OrderDetailsDao orderDetailsDao;

    @Autowired
    private InvoiceDao invoiceDao;

    @Autowired
    private OrderDao orderDao;



}
