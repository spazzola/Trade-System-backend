package com.tradesystem;

import com.tradesystem.buyer.Buyer;
import com.tradesystem.buyer.BuyerService;
import com.tradesystem.cost.Cost;
import com.tradesystem.invoice.Invoice;
import com.tradesystem.invoice.InvoiceDao;
import com.tradesystem.invoice.InvoiceService;
import com.tradesystem.order.Order;
import com.tradesystem.order.OrderDao;
import com.tradesystem.order.OrderService;
import com.tradesystem.orderdetails.OrderDetails;
import com.tradesystem.report.Report;
import com.tradesystem.report.ReportDao;
import com.tradesystem.report.ReportMonthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@SpringBootTest
class TradesystemApplicationTests {

	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderDao orderDao;

	@Autowired
	private ReportMonthService reportMonthService;

	@Autowired
	private ReportDao reportDao;

	@Autowired
	private BuyerService buyerService;

	@Autowired
	private InvoiceDao invoiceDao;

	@Autowired
	private InvoiceService invoiceService;


	@Test
	void contextLoads() {
	}


}
