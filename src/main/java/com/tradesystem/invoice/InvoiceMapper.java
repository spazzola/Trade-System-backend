package com.tradesystem.invoice;

import com.tradesystem.buyer.Buyer;
import com.tradesystem.buyer.BuyerDto;
import com.tradesystem.buyer.BuyerMapper;
import com.tradesystem.supplier.Supplier;
import com.tradesystem.supplier.SupplierDto;
import com.tradesystem.supplier.SupplierMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class InvoiceMapper {

    private BuyerMapper buyerMapper;
    private SupplierMapper supplierMapper;

    public InvoiceMapper(BuyerMapper buyerMapper, SupplierMapper supplierMapper) {
        this.buyerMapper = buyerMapper;
        this.supplierMapper = supplierMapper;
    }

    public InvoiceDto toDto(Invoice invoice) {
        final Buyer buyer = invoice.getBuyer();
        final Supplier supplier = invoice.getSupplier();

        final BuyerDto buyerDto = buyer != null ? buyerMapper.toDto(invoice.getBuyer()) : null;
        final SupplierDto supplierDto = supplier != null ? supplierMapper.toDto(invoice.getSupplier()) : null;

        return InvoiceDto.builder()
                .id(invoice.getId())
                .invoiceNumber(invoice.getInvoiceNumber())
                .date(invoice.getDate())
                .amountToUse(invoice.getAmountToUse())
                .value(invoice.getValue())
                .isUsed(invoice.isUsed())
                .isPaid(invoice.isPaid())
                .comment(invoice.getComment())
                .toEqualizeNegativeInvoice(invoice.isToEqualizeNegativeInvoice())
                .buyer(buyerDto)
                .supplier(supplierDto)
                .build();
    }

    public List<InvoiceDto> toDto(List<Invoice> invoices) {
        return invoices.stream()
                .map(this::toDto)
                .collect(Collectors.toList());

    }

}
