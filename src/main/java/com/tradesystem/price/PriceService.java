package com.tradesystem.price;

import com.tradesystem.buyer.Buyer;
import com.tradesystem.buyer.BuyerDao;
import com.tradesystem.buyer.BuyerDto;
import com.tradesystem.product.Product;
import com.tradesystem.product.ProductDao;
import com.tradesystem.supplier.Supplier;
import com.tradesystem.supplier.SupplierDao;
import com.tradesystem.supplier.SupplierDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PriceService {

    private PriceDao priceDao;
    private BuyerDao buyerDao;
    private SupplierDao supplierDao;
    private ProductDao productDao;

    public PriceService(PriceDao priceDao, BuyerDao buyerDao, SupplierDao supplierDao, ProductDao productDao) {
        this.priceDao = priceDao;
        this.buyerDao = buyerDao;
        this.supplierDao = supplierDao;
        this.productDao = productDao;
    }

    @Transactional
    public Price createBuyerPrice(PriceDto priceDto) {
        BuyerDto buyerDto = priceDto.getBuyer();
        Price price;

        if (buyerDto == null) {
            throw new RuntimeException("Nie mozna stworzyc ceny dla buyera bez buyera");
        } else {

            Buyer buyer = Buyer.builder()
                    .id(buyerDto.getId())
                    .build();

            Optional<Product> product = productDao.findById(priceDto.getProductType().getProductId());

            price = Price.builder()
                    .price(priceDto.getPrice())
                    .product(product.get())
                    .buyer(buyer)
                    .build();
        }
        return priceDao.save(price);
    }

    @Transactional
    public Price createSupplierPrice(PriceDto priceDto) {
        SupplierDto supplierDto = priceDto.getSupplier();
        Price price;

        if (supplierDto == null) {
            throw new RuntimeException("Nie mozna stworzyc ceny dla suppliera bez suppliera");
        } else {

            Supplier supplier = Supplier.builder()
                    .id(supplierDto.getId())
                    .build();

            Optional<Product> product = productDao.findById(priceDto.getProductType().getProductId());

            price = Price.builder()
                    .price(priceDto.getPrice())
                    .product(product.get())
                    .supplier(supplier)
                    .build();
        }
        return priceDao.save(price);
    }
}
