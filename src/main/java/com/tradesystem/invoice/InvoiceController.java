package com.tradesystem.invoice;

import com.tradesystem.user.RoleSecurity;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/invoice")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class InvoiceController {

   //TODO Change params to LocalDate/Long

    private InvoiceService invoiceService;
    private InvoiceMapper invoiceMapper;
    private RoleSecurity roleSecurity;
    private UpdateInvoiceService updateInvoiceService;

    private Logger logger = LogManager.getLogger(InvoiceController.class);



    public InvoiceController(InvoiceService invoiceService, InvoiceMapper invoiceMapper,
                             RoleSecurity roleSecurity, UpdateInvoiceService updateInvoiceService) {
        this.invoiceService = invoiceService;
        this.invoiceMapper = invoiceMapper;
        this.roleSecurity = roleSecurity;
        this.updateInvoiceService = updateInvoiceService;
    }

    @PostMapping("/create")
    public InvoiceDto create(@RequestBody InvoiceDto invoiceDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        roleSecurity.checkUserRole(authentication);

        logger.info("Dodawanie faktury: " + invoiceDto);

        final Invoice invoice = invoiceService.createInvoice(invoiceDto);
        return invoiceMapper.toDto(invoice);
    }

    @PutMapping("/payForInvoice")
    public void payForInvoice(@RequestParam(value = "id") String id) {
        logger.info("Płacenie za fakturę o id: " + id);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        roleSecurity.checkUserRole(authentication);

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

        logger.info("Przenoszenie faktury na następny miesiąc. Otrzymana data w requescie: " + localDate);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        roleSecurity.checkUserRole(authentication);

        int year = Integer.valueOf(localDate.substring(0, 4));
        int month = Integer.valueOf(localDate.substring(5, 7));

        LocalDate currentDate = LocalDate.of(year, month, 15);

        invoiceService.transferInvoicesToNextMonth(currentDate);
    }

    @GetMapping("/getBuyersPositiveBalance")
    public BigDecimal getBuyersPositiveBalance() {
        return invoiceService.getBuyersPositiveBalance();
    }

    @GetMapping("/getBuyersNegativeBalance")
    public BigDecimal getBuyersNegativeBalance() {
        return invoiceService.getBuyersNegativeBalance();
    }

    @GetMapping("/getSuppliersPositiveBalance")
    public BigDecimal getSuppliersPositiveBalance() {
        return invoiceService.getSuppliersPositiveBalance();
    }

    @GetMapping("/getSuppliersNegativeBalance")
    public BigDecimal getSuppliersNegativeBalance() {
        return invoiceService.getSuppliersNegativeBalance();
    }

    @GetMapping("/getInvoiceByInvoiceNumber")
    public InvoiceDto getInvoiceByInvoiceNumber(@RequestParam(value = "invoiceNumber") String invoiceNumber) {
        Invoice invoice = invoiceService.getInvoiceByInvoiceNumber(invoiceNumber);
        return invoiceMapper.toDto(invoice);
    }

    @PutMapping("/updateInvoice")
    public InvoiceDto updateInvoice(@RequestBody UpdateInvoiceRequest updateInvoiceRequest) {
        logger.info("Aktualizacja faktury: " + updateInvoiceRequest);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        roleSecurity.checkUserRole(authentication);

        Invoice invoice = updateInvoiceService.updateInvoice(updateInvoiceRequest);

        return invoiceMapper.toDto(invoice);
    }
}
