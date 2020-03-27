package com.tradesystem.report;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "reports")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    private BigDecimal soldedValue;

    private BigDecimal soldedQuantity;

    private BigDecimal incomes;

    private BigDecimal incomesDifference;

    private BigDecimal averageSold;

    private BigDecimal averagePurchase;

    private BigDecimal averageEarningsPerM3;

    private BigDecimal profit;

    private String type;

    private Report() {

    }

    public static class ReportBuilder {

        private BigDecimal soldedValue;
        private BigDecimal soldedQuantity;
        private BigDecimal incomes;
        private BigDecimal incomesDifference;
        private BigDecimal averageSold;
        private BigDecimal averagePurchase;
        private BigDecimal averageEarningsPerM3;
        private BigDecimal profit;
        private String type;


        public ReportBuilder setSoldedValue(BigDecimal soldedValue) {
            this.soldedValue = soldedValue;
            return this;
        }

        public ReportBuilder setSoldedQuantity(BigDecimal soldedQuantity) {
            this.soldedQuantity = soldedQuantity;
            return this;
        }

        public ReportBuilder setIncomes(BigDecimal incomes) {
            this.incomes = incomes;
            return this;
        }

        public ReportBuilder setIncomesDifference(BigDecimal incomesDifference) {
            this.incomesDifference = incomesDifference;
            return this;
        }

        public ReportBuilder setAverageSold(BigDecimal averageSold) {
            this.averageSold = averageSold;
            return this;
        }

        public ReportBuilder setAveragePurchase(BigDecimal averagePurchase) {
            this.averagePurchase = averagePurchase;
            return this;
        }

        public ReportBuilder setAverageEarningsPerM3(BigDecimal averageEarningsPerM3) {
            this.averageEarningsPerM3 = averageEarningsPerM3;
            return this;
        }

        public ReportBuilder setProfit(BigDecimal profit) {
            this.profit = profit;
            return this;
        }

        public ReportBuilder setType(String type) {
            this.type = type;
            return this;
        }

        public Report build() {
            Report report = new Report();
            report.soldedValue = this.soldedValue;
            report.soldedQuantity = this.soldedQuantity;
            report.incomes = this.incomes;
            report.incomesDifference = this.incomesDifference;
            report.averageSold = this.averageSold;
            report.averagePurchase = this.averagePurchase;
            report.averageEarningsPerM3 = this.averageEarningsPerM3;
            report.profit = this.profit;
            report.type = this.type;

            return report;
        }
    }

    @Override
    public String toString() {
        return "Report{" +
                "\nreportId=" + reportId +
                "\nsoldedValue=" + soldedValue +
                "\nsoldedQuantity=" + soldedQuantity +
                "\nincomes=" + incomes +
                "\nincomesDifference=" + incomesDifference +
                "\naverageSold=" + averageSold +
                "\naveragePurchase=" + averagePurchase +
                "\naverageEarningsPerM3=" + averageEarningsPerM3 +
                "\nprofit=" + profit +
                "\ntype='" + type + '\'' +
                '}';
    }
}
