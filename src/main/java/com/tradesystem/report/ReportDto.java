package com.tradesystem.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportDto {

    private BigDecimal soldedValue;
    private BigDecimal boughtValue;
    private BigDecimal soldedQuantity;
    private BigDecimal buyersNotUsedValue;
    private BigDecimal suppliersNotUsedValue;
    private BigDecimal averageSold;
    private BigDecimal averagePurchase;
    private BigDecimal averageEarningsPerM3;
    private BigDecimal profit;
    private String type;
}
