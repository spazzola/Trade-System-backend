package com.tradesystem.invoice;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateInvoiceRequest {

    private InvoiceDto invoice;

}
