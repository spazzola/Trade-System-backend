package com.tradesystem.product;

import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductDto toDto(Product product) {
        return ProductDto.builder()
                .product(product.getProduct())
                .build();
    }
}
