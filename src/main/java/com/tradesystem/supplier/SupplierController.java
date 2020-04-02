package com.tradesystem.supplier;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/supplier")
public class SupplierController {

    private SupplierService supplierService;
    private SupplierMapper supplierMapper;

    public SupplierController(SupplierService supplierService, SupplierMapper supplierMapper) {
        this.supplierService = supplierService;
        this.supplierMapper = supplierMapper;
    }


    @PostMapping("/create")
    public SupplierDto createBuyer(@RequestBody SupplierDto supplierDto) {
        final Supplier supplier = supplierService.createBuyer(supplierDto);

        return supplierMapper.toDto(supplier);
    }

    @GetMapping("/getAll")
    public List<SupplierDto> getAll() {
        final List<Supplier> suppliers = supplierService.getAll();

        return supplierMapper.toDto(suppliers);
    }

    @GetMapping("/getAllWithBalances")
    public List<SupplierDto> getAllWithBalances(){
        final List<Supplier> buyers = supplierService.getBalances();

        return supplierMapper.toDto(buyers);
    }
}
