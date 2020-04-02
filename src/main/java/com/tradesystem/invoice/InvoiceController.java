package com.tradesystem.invoice;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/invoice")
public class InvoiceController {


    private InvoiceService invoiceService;
    private InvoiceMapper invoiceMapper;

    public InvoiceController(InvoiceService invoiceService, InvoiceMapper invoiceMapper) {
        this.invoiceService = invoiceService;
        this.invoiceMapper = invoiceMapper;
    }

    @PostMapping("/create")
    public InvoiceDto create(@RequestBody InvoiceDto invoiceDto) {
        final Invoice invoice = invoiceService.createInvoice(invoiceDto);
        return invoiceMapper.toDto(invoice);
    }

    @PutMapping("/payForBuyerInvoice")
    public void payForBuyerInvoice(@RequestParam("id") Long id) {
        invoiceService.payForBuyerInvoice(id);
    }

    @PutMapping("/payForSupplierInvoice")
    public void payForSupplierInvoice(@RequestParam("id") Long id) {
        invoiceService.payForSupplierInvoice(id);
    }

    @GetMapping("/get")
    public InvoiceDto get(@RequestParam("id") Long id) {
        final Invoice invoice = invoiceService.getInvoice(id);
        return invoiceMapper.toDto(invoice);
    }

    @GetMapping("/getAll")
    public List<InvoiceDto> getAll() {
        final List<Invoice> invoices = invoiceService.getAll();
        return invoiceMapper.toDto(invoices);
    }

    @PostMapping("/transfer")
    public void transferInvoicesToNextMonth(@RequestParam("localDate")
                                                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate localDate) {

        invoiceService.trasnferInvoicesToNextMonth(localDate);
    }

}
