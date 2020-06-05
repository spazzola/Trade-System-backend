package com.tradesystem.supplier;

import com.tradesystem.price.Price;
import com.tradesystem.price.PriceDto;
import com.tradesystem.price.PriceMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/supplier")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class SupplierController {

    private SupplierService supplierService;
    private SupplierMapper supplierMapper;
    private PriceMapper priceMapper;

    public SupplierController(SupplierService supplierService, SupplierMapper supplierMapper, PriceMapper priceMapper) {
        this.supplierService = supplierService;
        this.supplierMapper = supplierMapper;
        this.priceMapper = priceMapper;
    }


    @PostMapping("/create")
    public SupplierDto createBuyer(@RequestBody SupplierDto supplierDto) {
        final Supplier supplier = supplierService.createSupplier(supplierDto);

        return supplierMapper.toDto(supplier);
    }

    @GetMapping("/getAll")
    public List<SupplierDto> getAll() {
        final List<Supplier> suppliers = supplierService.getAll();

        return supplierMapper.toDto(suppliers);
    }

    @GetMapping("/getAllWithBalances")
    public List<SupplierDto> getAllWithBalances(){
        final List<Supplier> suppliers = supplierService.getBalances();
        System.out.println(suppliers);
        return supplierMapper.toDto(suppliers);
    }

    @GetMapping("/getSupplierProducts")
    public List<PriceDto> getSupplierProducts(@RequestParam("id") String id) {
        final List<Price> prices = supplierService.getSupplierProducts(Long.valueOf(id));

        return priceMapper.toDto(prices);
    }
}
