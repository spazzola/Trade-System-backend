package com.tradesystem.buyer;

import com.tradesystem.invoice.*;
import com.tradesystem.order.Order;
import com.tradesystem.order.OrderDto;
import com.tradesystem.order.OrderMapper;
import com.tradesystem.order.OrderService;
import com.tradesystem.price.Price;
import com.tradesystem.price.PriceDto;
import com.tradesystem.price.PriceMapper;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/buyer")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class BuyerController {

    private BuyerService buyerService;
    private BuyerMapper buyerMapper;
    private PriceMapper priceMapper;
    private OrderService orderService;
    private OrderMapper orderMapper;
    private InvoiceService invoiceService;
    private InvoiceMapper invoiceMapper;

    public BuyerController(BuyerService buyerService, BuyerMapper buyerMapper,
                           PriceMapper priceMapper, OrderService orderService,
                           OrderMapper orderMapper, InvoiceService invoiceService, InvoiceMapper invoiceMapper) {
        this.buyerService = buyerService;
        this.buyerMapper = buyerMapper;
        this.priceMapper = priceMapper;
        this.orderService = orderService;
        this.orderMapper = orderMapper;
        this.invoiceService = invoiceService;
        this.invoiceMapper = invoiceMapper;
    }

    @PostMapping("/create")
    public BuyerDto createBuyer(@RequestBody BuyerDto buyerDto) {
        final Buyer buyer = buyerService.createBuyer(buyerDto);

        return buyerMapper.toDto(buyer);
    }

    @GetMapping("/getAll")
    public List<BuyerDto> getAll() {
        final List<Buyer> buyers = buyerService.getAll();

        return buyerMapper.toDto(buyers);
    }

    @GetMapping("/getAllWithBalances")
    public List<BuyerDto> getAllWithBalances(){
        final List<Buyer> buyers = buyerService.getBalances();

        return buyerMapper.toDto(buyers);
    }

    @GetMapping("/getAllWithAverageEarnings")
    public List<BuyerDto> getAllWithAverageEarnings() {
        final List<Buyer> buyers = buyerService.getAllWithAverageEarning();
        return buyerMapper.toDto(buyers);
    }

    @GetMapping("/getBuyerProducts")
    public List<PriceDto> getBuyerProducts(@RequestParam("id") String id) {
        List<Price> prices = buyerService.getBuyerProducts(Long.valueOf(id));

        return priceMapper.toDto(prices);
    }

    @GetMapping("/getBuyerMonthOrders")
    public List<OrderDto> getBuyerMonthOrders(@RequestParam("buyerId") String buyerId,
                                                 @RequestParam("month") String month,
                                                 @RequestParam("year") String year) {

        Long id = Long.valueOf(buyerId);
        int m = Integer.valueOf(month);
        int y = Integer.valueOf(year);

        List<Order> orders = orderService.getBuyerMonthOrders(id, m, y);
        return orderMapper.toDto(orders);
    }

    @PutMapping("/updateBuyerName")
    public BuyerDto updateBuyerName(@RequestParam("oldBuyerName") String oldBuyerName,
                                    @RequestParam("newBuyerName") String newBuyerName) {

        Buyer buyer = buyerService.updateBuyerName(oldBuyerName, newBuyerName);
        return buyerMapper.toDto(buyer);
    }

    @GetMapping("/getBuyerMonthInvoices")
    public List<InvoiceDto> getBuyerMonthInvoices(@RequestParam("buyerId") String buyerId,
                                                  @RequestParam("month") String month,
                                                  @RequestParam("year") String year) {

        Long id = Long.valueOf(buyerId);
        int m = Integer.valueOf(month);
        int y = Integer.valueOf(year);

        List<Invoice> invoices = invoiceService.getBuyerMonthInvoices(id, m, y);
        return invoiceMapper.toDto(invoices);
    }

    @GetMapping("/getBuyersMonthInvoices")
    public List<InvoiceDto> getBuyersMonthInvoices(@RequestParam("month") String month,
                                                   @RequestParam("year") String year) {
        int m = Integer.valueOf(month);
        int y = Integer.valueOf(year);

        List<Invoice> invoices = invoiceService.getBuyersMonthInvoices(m, y);
        return invoiceMapper.toDto(invoices);
    }
}
