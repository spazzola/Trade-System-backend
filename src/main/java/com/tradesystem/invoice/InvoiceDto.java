package com.tradesystem.invoice;

import com.tradesystem.buyer.BuyerDto;
import com.tradesystem.supplier.SupplierDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDto {

    private Long id;
    private String invoiceNumber;
    private LocalDate date;
    private BigDecimal value;
    private BigDecimal amountToUse;
    private boolean isUsed;
    private boolean isPaid;
    private String comment;
    private BuyerDto buyer;
    private SupplierDto supplier;

}
