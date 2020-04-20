package com.tradesystem.price;

import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/price")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PriceController {

    private PriceService priceService;
    private PriceMapper priceMapper;

    public PriceController(PriceService priceService, PriceMapper priceMapper) {
        this.priceService = priceService;
        this.priceMapper = priceMapper;
    }

    @PostMapping("/createBuyerPrice")
    public PriceDto createBuyerPrice(@RequestBody PriceDto priceDto) {
        final Price price = priceService.createBuyerPrice(priceDto);

        return priceMapper.toDto(price);
    }

    @PostMapping("/createSupplierPrice")
    public PriceDto createSupplierPrice(@RequestBody PriceDto priceDto) {
        final Price price = priceService.createSupplierPrice(priceDto);

        return priceMapper.toDto(price);
    }

    @PutMapping("/editBuyerPrice")
    public void editBuyerPrice(@RequestParam(value = "buyerId", required = false) String buyerId,
                               @RequestParam(value = "productId", required = false) String productId,
                               @RequestParam(value = "value", required = false) String value) {

        BigDecimal newValue = new BigDecimal(value);
        priceService.editBuyerPrice(Long.valueOf(buyerId), Long.valueOf(productId), newValue);
    }

    @PutMapping("/editSupplierPrice")
    public void editSupplierPrice(@RequestParam(value = "supplierId") String supplierId,
                                  @RequestParam(value = "productId") String productId,
                                  @RequestParam(value = "value") String value) {

        BigDecimal newValue = new BigDecimal(value);
        priceService.editSupplierPrice(Long.valueOf(supplierId), Long.valueOf(productId), newValue);
    }
}
