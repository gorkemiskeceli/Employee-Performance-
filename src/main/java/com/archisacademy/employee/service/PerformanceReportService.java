package com.archisacademy.employee.service;

import com.archisacademy.employee.dto.request.CustomPerformanceRequest;
import com.archisacademy.employee.dto.response.CustomPerformanceResponse;
import com.archisacademy.employee.dto.response.EmployeePerformanceResponse;
import com.archisacademy.employee.dto.response.PerformanceReportPdfResponse;
import com.archisacademy.employee.dto.response.VisualizationResponse;

import java.util.List;

public interface PerformanceReportService {
    VisualizationResponse generateVisualization(Long employeeId);

    PerformanceReportPdfResponse generatePerformanceReport(Long employeeId);

    List<EmployeePerformanceResponse> getTopPerformers();

    CustomPerformanceResponse customReport(CustomPerformanceRequest request);
}
