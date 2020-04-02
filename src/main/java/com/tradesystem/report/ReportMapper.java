package com.tradesystem.report;

import org.springframework.stereotype.Component;

@Component
public class ReportMapper {


    public ReportDto toDto(Report report) {
        return ReportDto.builder()
                .soldedValue(report.getSoldedValue())
                .boughtValue(report.getBoughtValue())
                .buyersNotUsedValue(report.getBuyersNotUsedValue())
                .suppliersNotUsedValue(report.getSuppliersNotUsedValue())
                .soldedQuantity(report.getSoldedQuantity())
                .averageSold(report.getAverageSold())
                .averagePurchase(report.getAveragePurchase())
                .averageEarningsPerM3(report.getAverageEarningsPerM3())
                .profit(report.getProfit())
                .type(report.getType())
                .build();
    }

}
