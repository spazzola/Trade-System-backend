package com.tradesystem.supplier;

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
@RequestMapping("/supplier")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class SupplierController {

    private SupplierService supplierService;
    private SupplierMapper supplierMapper;
    private PriceMapper priceMapper;
    private OrderService orderService;
    private OrderMapper orderMapper;

    public SupplierController(SupplierService supplierService, SupplierMapper supplierMapper,
                              PriceMapper priceMapper, OrderService orderService, OrderMapper orderMapper) {
        this.supplierService = supplierService;
        this.supplierMapper = supplierMapper;
        this.priceMapper = priceMapper;
        this.orderService = orderService;
        this.orderMapper = orderMapper;
    }


    @PostMapping("/create")
    public SupplierDto createBuyer(@RequestBody SupplierDto supplierDto) {
        final Supplier supplier = supplierService.createSupplier(supplierDto);

        return supplierMapper.toDto(supplier);
    }

    @GetMapping("/getAll")
    public List<SupplierDto> getAll() {
        final List<Supplier> suppliers = supplierService.getAll();

        return supplierMapper.toDto(suppliers);
    }

    @GetMapping("/getAllWithBalances")
    public List<SupplierDto> getAllWithBalances(){
        final List<Supplier> suppliers = supplierService.getBalances();
        return supplierMapper.toDto(suppliers);
    }

    @GetMapping("/getSupplierProducts")
    public List<PriceDto> getSupplierProducts(@RequestParam("id") String id) {
        final List<Price> prices = supplierService.getSupplierProducts(Long.valueOf(id));

        return priceMapper.toDto(prices);
    }

    @GetMapping("/getSupplierMonthOrders")
    public List<OrderDto> getSupplierMonthOrders(@RequestParam("supplierId") String supplierId,
                                                 @RequestParam("month") String month,
                                                 @RequestParam("year") String year) {

        Long id = Long.valueOf(supplierId);
        int m = Integer.valueOf(month);
        int y = Integer.valueOf(year);

        List<Order> orders = orderService.getSupplierMonthOrders(id, m, y);
        return orderMapper.toDto(orders);
    }

}
