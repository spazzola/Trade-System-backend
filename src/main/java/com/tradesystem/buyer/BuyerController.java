package com.tradesystem.buyer;

import com.tradesystem.order.Order;
import com.tradesystem.order.OrderDto;
import com.tradesystem.order.OrderMapper;
import com.tradesystem.order.OrderService;
import com.tradesystem.price.Price;
import com.tradesystem.price.PriceDto;
import com.tradesystem.price.PriceMapper;
import com.tradesystem.product.Product;
import com.tradesystem.product.ProductDto;
import com.tradesystem.product.ProductMapper;
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

    public BuyerController(BuyerService buyerService, BuyerMapper buyerMapper,
                           PriceMapper priceMapper, OrderService orderService, OrderMapper orderMapper) {
        this.buyerService = buyerService;
        this.buyerMapper = buyerMapper;
        this.priceMapper = priceMapper;
        this.orderService = orderService;
        this.orderMapper = orderMapper;
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

        List<Order> orders = orderService.getSupplierMonthOrders(id, m, y);
        return orderMapper.toDto(orders);
    }
}
