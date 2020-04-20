package com.tradesystem.report;

import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

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
                .sumCosts(report.getSumCosts())
                .type(report.getType())
                .build();
    }

    public List<ReportDto> toDto(List<Report> invoices) {
        return invoices.stream()
                .map(this::toDto)
                .collect(Collectors.toList());

    }

}
