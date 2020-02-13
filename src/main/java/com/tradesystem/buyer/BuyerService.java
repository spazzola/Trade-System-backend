package com.tradesystem.buyer;

import com.tradesystem.price.PriceDao;

public class BuyerService {


    private PriceDao priceDao;

    public BuyerService(PriceDao priceDao) {
        this.priceDao = priceDao;
    }


}
