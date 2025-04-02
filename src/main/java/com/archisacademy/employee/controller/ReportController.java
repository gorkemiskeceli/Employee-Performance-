package com.archisacademy.employee.controller;

import com.archisacademy.employee.dto.response.EmployeePerformanceSummaryResponse;
import com.archisacademy.employee.dto.response.TeamPerformanceOverviewResponse;
import com.archisacademy.employee.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/employee/{employeeId}")
    ResponseEntity<EmployeePerformanceSummaryResponse> getEmployeePerformanceSummary(@PathVariable Long employeeId) {
        EmployeePerformanceSummaryResponse summaryResponse = reportService.getEmployeePerformanceSummary(employeeId);
        return ResponseEntity.ok(summaryResponse);
    }

    @GetMapping("/team/{teamName}")
    ResponseEntity<TeamPerformanceOverviewResponse> getTeamPerformanceOverview(@PathVariable String teamName) {
        TeamPerformanceOverviewResponse teamPerformanceOverviewResponse = reportService.getTeamPerformanceOverview(teamName);
        return ResponseEntity.ok(teamPerformanceOverviewResponse);
    }

}
