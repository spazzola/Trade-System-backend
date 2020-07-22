package com.tradesystem.price;

import com.tradesystem.price.pricehistory.PriceHistory;
import com.tradesystem.price.pricehistory.PriceHistoryService;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/price")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PriceController {

    private PriceService priceService;
    private PriceMapper priceMapper;
    private PriceHistoryService priceHistoryService;

    private Logger logger = LogManager.getLogger(PriceController.class);


    public PriceController(PriceService priceService, PriceMapper priceMapper, PriceHistoryService priceHistoryService) {
        this.priceService = priceService;
        this.priceMapper = priceMapper;
        this.priceHistoryService = priceHistoryService;
    }

    @PostMapping("/createBuyerPrice")
    public PriceDto createBuyerPrice(@RequestBody PriceDto priceDto) {
        logger.info("Dodawanie ceny dla kupca: " + priceDto);

        final Price price = priceService.createBuyerPrice(priceDto);

        return priceMapper.toDto(price);
    }

    @PostMapping("/createSupplierPrice")
    public PriceDto createSupplierPrice(@RequestBody PriceDto priceDto) {
        logger.info("Dodawanie ceny dla sprzedawcy: " + priceDto);

        final Price price = priceService.createSupplierPrice(priceDto);

        return priceMapper.toDto(price);
    }

    @PutMapping("/editBuyerPrice")
    public void editBuyerPrice(@RequestParam(value = "buyerId", required = false) String buyerId,
                               @RequestParam(value = "productId", required = false) String productId,
                               @RequestParam(value = "value", required = false) String value) {

        logger.info("Edytowanie ceny dla kupca. buyer_id=" + buyerId + " product_id=" + productId + " wartość=" + value);

        BigDecimal newValue = new BigDecimal(value);
        priceService.editBuyerPrice(Long.valueOf(buyerId), Long.valueOf(productId), newValue);
    }

    @PutMapping("/editSupplierPrice")
    public void editSupplierPrice(@RequestParam(value = "supplierId") String supplierId,
                                  @RequestParam(value = "productId") String productId,
                                  @RequestParam(value = "value") String value) {

        logger.info("Edytowanie ceny dla sprzedawcy. supplier_id=" + supplierId + " product_id=" + productId + " wartość=" + value);

        BigDecimal newValue = new BigDecimal(value);
        priceService.editSupplierPrice(Long.valueOf(supplierId), Long.valueOf(productId), newValue);
    }

    @GetMapping("/getBuyerPriceHistory")
    public List<PriceDto> getBuyerPriceHistory(@RequestParam(value = "buyerId") String buyerId) {
        List<Price> pricesHistory =  priceHistoryService.getBuyerPriceHistory(Long.valueOf(buyerId));

        return priceMapper.toDto(pricesHistory);
    }
}
