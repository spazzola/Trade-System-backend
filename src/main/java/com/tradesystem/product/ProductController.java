package com.tradesystem.product;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {

    private ProductService productService;
    private ProductMapper productMapper;

    public ProductController(ProductService productService, ProductMapper productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }


    @PostMapping("/create")
    public ProductDto create(@RequestBody ProductDto productDto) {
        final Product product = productService.createProduct(productDto);

        return productMapper.toDto(product);
    }


}
