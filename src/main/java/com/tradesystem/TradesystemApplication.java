package com.tradesystem;


import java.time.LocalDate;
import java.util.List;

import com.tradesystem.invoice.InvoiceDao;
import com.tradesystem.invoice.InvoiceService;
import com.tradesystem.order.Order;
import com.tradesystem.order.OrderDao;
import com.tradesystem.order.OrderService;
import com.tradesystem.product.ProductDao;
import com.tradesystem.price.PriceDao;
import com.tradesystem.report.ReportService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TradesystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(TradesystemApplication.class, args);

    }

    @Bean
    public CommandLineRunner bookDemo(OrderDao orderDao, PriceDao priceDao, ProductDao productDao,
                                      OrderService orderService, InvoiceDao invoiceDao, InvoiceService invoiceService,
                                      ReportService reportService) {
        return (args) -> {


            List<Order> orders = orderDao.findByBuyerId(1L);


            for (Order order : orders) {
                orderService.payForOrderMainMethod(order);
            }


            LocalDate localDate = LocalDate.now();
            System.out.println(localDate.getMonthValue());

            //reportService.sumMonthlyOrderedValue(2, 2020);
            //reportService.calculateSoldedOrdersMonth(2);
            /*
            Supplier supplier = orders.get(0).getSupplier();
            Invoice invoice = new Invoice();
            invoice.setAmountToUse(BigDecimal.valueOf(2000));
            invoice.setSupplier(supplier);
            invoiceService.addSupplierInvoice(invoice);

            Invoice invoice2 = new Invoice();
            invoice2.setAmountToUse(BigDecimal.valueOf(15000));
            invoice2.setSupplier(supplier);
            invoiceService.addSupplierInvoice(invoice2);

            System.out.println("Buyer:");
            //Buyer part
            for (Order order : orders) {
                BigDecimal buyerSum = orderService.calculateBuyerOrder(order);
                order.setSum(buyerSum);
                orderDao.save(order);
                System.out.println(buyerSum);
            }

            System.out.println("Supplier:");
            //Supplier part
            for (Order order : orders) {
                BigDecimal supplierSum = orderService.calculateSupplierOrder(order);
                System.out.println(supplierSum);
            }


            List<Order> calculatedOrders = orderDao.findByBuyerId(1L);

            for (Order order : calculatedOrders) {
                BigDecimal sum = order.getSum();
                orderService.payForOrder(order, sum);
            }
/*
            List<Invoice> result = invoiceDao.findAll();
            System.out.println("BuyerId" + "\t\tAmount" + "\t\t\tisUsed");
            //for (Invoice invoice : result) {
               // System.out.println(invoice.getBuyer().getId() + "\t\t\t" + invoice.getAmountToUse() + "\t\t" + invoice.isUsed());
           // }


            Invoice nI = new Invoice();
            nI.setAmountToUse(BigDecimal.valueOf(200));
            nI.setValue(BigDecimal.valueOf(200));
            nI.setBuyer(buyer);
            invoiceService.addInvoice(nI);


            Invoice n = new Invoice();
            n.setAmountToUse(BigDecimal.valueOf(80));
            n.setValue(BigDecimal.valueOf(80));
            n.setBuyer(buyer);
            invoiceService.addInvoice(n);
*/




















/*
            List<Order> orders1 = orderDao.findByBuyerId(1L);

            for (Order order : orders1) {
                BigDecimal sum = orderService.calculateBuyerOrder(order);
                order.setSum(sum);
                orderDao.save(order);
            }


            List<Order> orders2 = orderDao.findByBuyerId(2L);

            for (Order order : orders2) {
                BigDecimal sum = orderService.calculateBuyerOrder(order);
                order.setSum(sum);
                orderDao.save(order);
            }
*/
        };
    }


}



