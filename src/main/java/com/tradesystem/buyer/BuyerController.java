package com.tradesystem.buyer;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/buyer")
public class BuyerController {

    private BuyerService buyerService;
    private BuyerMapper buyerMapper;

    public BuyerController(BuyerService buyerService, BuyerMapper buyerMapper) {
        this.buyerService = buyerService;
        this.buyerMapper = buyerMapper;
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
}
