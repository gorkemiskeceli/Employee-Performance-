package com.archisacademy.employee.service;

import com.archisacademy.employee.dto.request.EmployeeRequest;
import com.archisacademy.employee.dto.request.EmployeeUpdateRequest;
import com.archisacademy.employee.dto.response.EmployeeResponse;
import com.archisacademy.employee.dto.response.EmployeeResponseDto;
import com.archisacademy.employee.dto.response.EmployeeUpdateResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface EmployeeService {


    EmployeeResponse addEmployee(EmployeeRequest request);

    EmployeeResponse getEmployeeById(Long id);

    List<EmployeeResponse> getAllEmployee();

    EmployeeUpdateResponse updateEmployee(Long id, EmployeeUpdateRequest request);

    EmployeeResponse deleteEmployee(long id);
    List<EmployeeResponseDto> searchEmployees(Long employeeId, String name, Long departmentId);
    Map<String, Object> importEmployees(MultipartFile file);
    Map<String, String> exportEmployeesToFiles();
}
