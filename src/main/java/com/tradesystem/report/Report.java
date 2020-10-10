package com.tradesystem.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reports")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    private BigDecimal soldValue;

    private BigDecimal boughtValue;

    private BigDecimal soldQuantity;

    private BigDecimal buyersNotUsedValue;

    private BigDecimal suppliersNotUsedValue;

    private BigDecimal averageSold;

    private BigDecimal averagePurchase;

    private BigDecimal averageEarningsPerM3;

    private BigDecimal profit;

    private BigDecimal sumCosts;

    private String type;


    @Override
    public String toString() {
        return "Report{" +
                "\nreportId=" + reportId +
                "\nsoldValue=" + soldValue +
                "\nsoldQuantity=" + soldQuantity +
                "\naverageSold=" + averageSold +
                "\naveragePurchase=" + averagePurchase +
                "\naverageEarningsPerM3=" + averageEarningsPerM3 +
                "\nprofit=" + profit +
                "\ntype='" + type + '\'' +
                '}';
    }
}
