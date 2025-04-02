package com.archisacademy.employee.controller;

import com.archisacademy.employee.dto.request.EmployeeRequest;
import com.archisacademy.employee.dto.request.EmployeeUpdateRequest;
import com.archisacademy.employee.dto.response.*;
import com.archisacademy.employee.service.EmployeeReportService;
import com.archisacademy.employee.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeReportService employeeReportService;

    public EmployeeController(EmployeeService employeeService, EmployeeReportService employeeReportService) {
        this.employeeService = employeeService;
        this.employeeReportService = employeeReportService;
    }


    @PostMapping("/add")
    public ResponseEntity<EmployeeResponse> addEmployee(@RequestBody EmployeeRequest employeeRequest) {
        EmployeeResponse response = employeeService.addEmployee(employeeRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/get")
    public ResponseEntity<EmployeeResponse> getEmployeeById(@PathVariable Long id) {
        EmployeeResponse response = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all-employees")
    public ResponseEntity<List<EmployeeResponse>> getAllEmployee() {
        List<EmployeeResponse> responseList = employeeService.getAllEmployee();
        return ResponseEntity.ok(responseList);
    }

    @PostMapping("/{id}/update")
    public ResponseEntity<EmployeeUpdateResponse> update(@PathVariable Long id, @RequestBody EmployeeUpdateRequest request) {
        EmployeeUpdateResponse response = employeeService.updateEmployee(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}/delete-employee")
    public ResponseEntity<EmployeeResponse> delete(@PathVariable Long id) {
        EmployeeResponse response = employeeService.deleteEmployee(id);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/search")
    public ResponseEntity<List<EmployeeResponseDto>> searchEmployees(
            @RequestParam(required = false) Long employeeId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long departmentId) {

        // Arama işlemi için servisi çağırıyoruz, ve EmployeeResponseDto döndürüyoruz
        List<EmployeeResponseDto> response = employeeService.searchEmployees(employeeId, name, departmentId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/generate-report/{employeeId}")
    public ResponseEntity<EmployeeReportResponse> getEmployeePerformance(@PathVariable Long employeeId) {
        EmployeeReportResponse response = employeeReportService.getEmployeePerformance(employeeId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/generate-team-report/{departmentId}")
    public ResponseEntity<TeamReportResponse> getTeamPerformance(@PathVariable Long departmentId) {
        TeamReportResponse response = employeeReportService.getTeamPerformance(departmentId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadEmployees(@RequestPart("file") MultipartFile file) {
        Map<String, Object> result = employeeService.importEmployees(file);
        return ResponseEntity.ok(result);
    }
    @GetMapping("/export")
    public ResponseEntity<Map<String, String>> exportEmployees() {
        Map<String, String> result = employeeService.exportEmployeesToFiles();
        return ResponseEntity.ok(result);
    }
}

