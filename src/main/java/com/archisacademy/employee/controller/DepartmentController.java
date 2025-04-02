package com.archisacademy.employee.controller;


import com.archisacademy.employee.dto.request.DepartmentRequest;
import com.archisacademy.employee.dto.response.DepartmentResponse;
import com.archisacademy.employee.service.DepartmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/department")
public class DepartmentController {
    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping("/save")
    public ResponseEntity<DepartmentResponse> save(@Validated @RequestBody DepartmentRequest request){
        DepartmentResponse response = departmentService.addDepartment(request);
        return ResponseEntity.ok(response);    }
}
