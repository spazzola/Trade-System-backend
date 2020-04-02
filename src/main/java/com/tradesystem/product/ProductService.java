package com.tradesystem.product;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private ProductDao productDao;


    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }


    @Transactional
    public Product createProduct(ProductDto productDto) {
        final Product product = Product.builder()
                                        .product(productDto.getProduct())
                                        .build();

        return productDao.save(product);
    }
}
