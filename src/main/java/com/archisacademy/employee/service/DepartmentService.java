package com.archisacademy.employee.service;

import com.archisacademy.employee.dto.request.DepartmentRequest;
import com.archisacademy.employee.dto.response.DepartmentResponse;
import jakarta.transaction.Transactional;

public interface DepartmentService {

    DepartmentResponse addDepartment(DepartmentRequest request);
}
