package com.tradesystem.invoice;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class UpdateInvoiceRequest {

    private String oldInvoiceNumber;
    private String newInvoiceNumber;
    private LocalDate newDate;
    private BigDecimal newValue;

}
