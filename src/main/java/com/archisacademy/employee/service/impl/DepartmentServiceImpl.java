package com.archisacademy.employee.service.impl;

import com.archisacademy.employee.dto.request.DepartmentRequest;
import com.archisacademy.employee.dto.response.DepartmentResponse;
import com.archisacademy.employee.entity.Department;
import com.archisacademy.employee.repository.DepartmentRepository;
import com.archisacademy.employee.repository.EmployeeRepository;
import com.archisacademy.employee.service.DepartmentService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;


    public DepartmentServiceImpl(DepartmentRepository departmentRepository, EmployeeRepository employeeRepository, ModelMapper modelMapper) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public DepartmentResponse addDepartment(DepartmentRequest request) {
        Department department = new Department();
        department.setDepartmentName(request.getDepartmentName());
        Department saved = departmentRepository.save(department);
        DepartmentResponse response = new DepartmentResponse();
        response.setId(saved.getId());
        response.setDepartmentName(saved.getDepartmentName());
        response.setMessage("Department has been saved successfully!!!");
        return response;
    }
}
