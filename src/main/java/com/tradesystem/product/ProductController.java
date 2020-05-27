package com.tradesystem.product;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@CrossOrigin(origins = "*", allowedHeaders = "*")
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

    @GetMapping("/getProduct")
    public ProductDto getProduct(@RequestParam(value = "productName") String productName) {
        Product product = productService.getProduct(productName);
        return productMapper.toDto(product);
    }

    @GetMapping("/getAll")
    public List<ProductDto> getAllProducts() {
        List<Product> products =  productService.getAllProducts();

        return productMapper.toDto(products);
    }
}
