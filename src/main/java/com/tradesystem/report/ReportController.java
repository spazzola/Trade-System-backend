package com.tradesystem.report;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/report")
public class ReportController {

    private ReportYearService reportYearService;
    private ReportMonthService reportMonthService;
    private ReportMapper reportMapper;

    public ReportController(ReportYearService reportYearService, ReportMonthService reportMonthService, ReportMapper reportMapper) {
        this.reportYearService = reportYearService;
        this.reportMonthService = reportMonthService;
        this.reportMapper = reportMapper;
    }

    @PostMapping("/generateMonthReport")
    public ReportDto generateMonthReport(@RequestParam("localDate")
                                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate localDate) {
        Report report = reportMonthService.generateMonthReport(localDate.getMonthValue(), localDate.getYear());

        return reportMapper.toDto(report);
    }

    @PostMapping("/generateYearReport")
    public ReportDto generateYearReport(@RequestParam("localDate")
                                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate localDate) {

        Report report = reportYearService.generateYearReport(localDate.getYear());

        return reportMapper.toDto(report);
    }


}
