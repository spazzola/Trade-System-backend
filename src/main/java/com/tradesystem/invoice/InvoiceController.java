package com.tradesystem.invoice;

import com.tradesystem.user.RoleSecurity;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.json.Json;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;

@RestController
@RequestMapping("/invoice")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class InvoiceController {

   //TODO Change params to LocalDate/Long

    private InvoiceService invoiceService;
    private InvoiceMapper invoiceMapper;
    private RoleSecurity roleSecurity;

    public InvoiceController(InvoiceService invoiceService, InvoiceMapper invoiceMapper, RoleSecurity roleSecurity) {
        this.invoiceService = invoiceService;
        this.invoiceMapper = invoiceMapper;
        this.roleSecurity = roleSecurity;
    }

    @PostMapping("/create")
    public InvoiceDto create(@RequestBody InvoiceDto invoiceDto) {
        //roleSecurity.checkUserRole(authentication);
        final Invoice invoice = invoiceService.createInvoice(invoiceDto);
        return invoiceMapper.toDto(invoice);
    }

    @PutMapping("/payForInvoice")
    public void payForInvoice(@RequestParam(value = "id") String id) {
        Long invoiceId = Long.valueOf(id);

        invoiceService.payForInvoice(invoiceId);
    }

    @GetMapping("/get")
    public InvoiceDto get(@RequestParam("id") Long id) {
        final Invoice invoice = invoiceService.getInvoice(id);
        return invoiceMapper.toDto(invoice);
    }

    @GetMapping("/getMonthInvoices")
    public List<InvoiceDto> getMonthInvoices(@RequestParam(value = "year") String year,
                                             @RequestParam(value = "month") String month) {

        int y = Integer.valueOf(year);
        int m = Integer.valueOf(month);

        final List<Invoice> invoices = invoiceService.getInvoicesByMonth(m, y);

        return invoiceMapper.toDto(invoices);
    }

    @GetMapping("/getAll")
    public List<InvoiceDto> getAll() {
        final List<Invoice> invoices = invoiceService.getAll();
        return invoiceMapper.toDto(invoices);
    }

    @PostMapping("/transfer")
    public void transferInvoicesToNextMonth(@RequestParam(value = "localDate", required = false)
                                                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String localDate) {
        int year = Integer.valueOf(localDate.substring(0, 4));
        int month = Integer.valueOf(localDate.substring(5, 7));

        LocalDate currentDate = LocalDate.of(year, month, 1);
        invoiceService.trasnferInvoicesToNextMonth(currentDate);
    }

}
