package com.tradesystem.report;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/report")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ReportController {

    //TODO change String params to LocalDate (at front)

    private ReportService reportService;
    private ReportYearService reportYearService;
    private ReportMonthService reportMonthService;
    private ReportMapper reportMapper;

    public ReportController(ReportService reportService, ReportYearService reportYearService,
                            ReportMonthService reportMonthService, ReportMapper reportMapper) {
        this.reportService = reportService;
        this.reportYearService = reportYearService;
        this.reportMonthService = reportMonthService;
        this.reportMapper = reportMapper;
    }

    @PostMapping("/generateMonthReport")
    public ReportDto generateMonthReport(@RequestParam("localDate")
                                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String localDate) {
        int year = Integer.valueOf(localDate.substring(0, 4));
        int month = Integer.valueOf(localDate.substring(5, 7));
        Report report = reportMonthService.generateMonthReport(month, year);

        return reportMapper.toDto(report);
    }

    @PostMapping("/generateYearReport")
    public ReportDto generateYearReport(@RequestParam("localDate")
                                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String localDate) {

        int year = Integer.valueOf(localDate.substring(0, 4));
        Report report = reportYearService.generateYearReport(year);

        return reportMapper.toDto(report);
    }

    @GetMapping("getAll")
    public List<ReportDto> getAllReports() {
        List<Report> reports = reportService.getAllReports();

        return reportMapper.toDto(reports);
    }

}
