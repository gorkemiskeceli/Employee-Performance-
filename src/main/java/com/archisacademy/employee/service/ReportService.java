package com.archisacademy.employee.service;

import com.archisacademy.employee.dto.response.EmployeePerformanceSummaryResponse;
import com.archisacademy.employee.dto.response.TeamPerformanceOverviewResponse;

public interface ReportService {

    EmployeePerformanceSummaryResponse getEmployeePerformanceSummary(Long employeeId);

    TeamPerformanceOverviewResponse getTeamPerformanceOverview(String teamName);

}
