package com.tradesystem.price;

import com.tradesystem.buyer.Buyer;
import com.tradesystem.buyer.BuyerDto;
import com.tradesystem.buyer.BuyerMapper;
import com.tradesystem.product.Product;
import com.tradesystem.product.ProductDto;
import com.tradesystem.product.ProductMapper;
import com.tradesystem.supplier.Supplier;
import com.tradesystem.supplier.SupplierDto;
import com.tradesystem.supplier.SupplierMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PriceMapper {

    private BuyerMapper buyerMapper;
    private SupplierMapper supplierMapper;
    private ProductMapper productMapper;


    public PriceMapper(BuyerMapper buyerMapper, SupplierMapper supplierMapper, ProductMapper productMapper) {
        this.buyerMapper = buyerMapper;
        this.supplierMapper = supplierMapper;
        this.productMapper = productMapper;
    }


    public PriceDto toDto(Price price) {
        final Buyer buyer = price.getBuyer();
        final Supplier supplier = price.getSupplier();
        final Product product = price.getProduct();

        BuyerDto buyerDto = null;
        if (buyer != null) {
            buyerDto = buyerMapper.toDto(buyer);
        }

        SupplierDto supplierDto = null;
        if (supplier != null) {
            supplierDto = supplierMapper.toDto(supplier);
        }

        final ProductDto productDto = productMapper.toDto(product);

        return PriceDto.builder()
                .price(price.getPrice())
                .product(productDto)
                .buyer(buyerDto)
                .supplier(supplierDto)
                .build();
    }

    public List<PriceDto> toDto(List<Price> invoices) {
        return invoices.stream()
                .map(this::toDto)
                .collect(Collectors.toList());

    }

}
