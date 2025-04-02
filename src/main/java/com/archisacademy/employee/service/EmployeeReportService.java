package com.archisacademy.employee.service;

import com.archisacademy.employee.dto.response.EmployeeReportResponse;
import com.archisacademy.employee.dto.response.TeamReportResponse;

public interface EmployeeReportService {

    EmployeeReportResponse getEmployeePerformance(Long employeeId);

    TeamReportResponse getTeamPerformance(Long departmentId);

}
