package com.tradesystem.price;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/price")
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
}
