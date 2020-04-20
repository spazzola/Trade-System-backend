package com.tradesystem.report;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReportService {

    private final ReportDao reportDao;

    public ReportService(ReportDao reportDao) {
        this.reportDao = reportDao;
    }

    @Transactional
    public List<Report> getAllReports() {
        return reportDao.findAll();
    }

    public boolean checkIfReportExist(String type) {
        return reportDao.findByType(type) == null;
    }

}
