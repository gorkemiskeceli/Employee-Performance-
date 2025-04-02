package com.archisacademy.employee.controller;

import com.archisacademy.employee.dto.request.CustomPerformanceRequest;
import com.archisacademy.employee.dto.response.CustomPerformanceResponse;
import com.archisacademy.employee.dto.response.EmployeePerformanceResponse;
import com.archisacademy.employee.dto.response.PerformanceReportPdfResponse;
import com.archisacademy.employee.dto.response.VisualizationResponse;
import com.archisacademy.employee.service.PerformanceReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/report")
public class PerformanceReportController {

    private final PerformanceReportService reportService;

    public PerformanceReportController(PerformanceReportService reportService) {
        this.reportService = reportService;
    }
    @GetMapping("/employee/{employeeId}/pdf")
    public ResponseEntity<PerformanceReportPdfResponse> generateReport(@PathVariable Long employeeId) {
        return ResponseEntity.ok(reportService.generatePerformanceReport(employeeId));
    }
    @GetMapping("/employee/{employeeId}/visualization")
    public ResponseEntity<VisualizationResponse> generateVisualization(@PathVariable Long employeeId) {
        return ResponseEntity.ok(reportService.generateVisualization(employeeId));
    }

    @GetMapping("/top-performers")
    public ResponseEntity<List<EmployeePerformanceResponse>> getTopPerformers(){
        List<EmployeePerformanceResponse> topPerformers = reportService.getTopPerformers();
        if (topPerformers.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(topPerformers);
    }

    @PostMapping("/custom")
    public ResponseEntity<CustomPerformanceResponse> customPerformanceReport(@RequestBody CustomPerformanceRequest request){
        try {
            CustomPerformanceResponse response = reportService.customReport(request);
            return ResponseEntity.ok(response);
        }catch (RuntimeException e){
            return ResponseEntity.status(404).body(null);
        }
    }
}
