package com.archisacademy.employee.service.impl;

import com.archisacademy.employee.dto.request.EmployeeImportRequest;
import com.archisacademy.employee.dto.request.EmployeeRequest;
import com.archisacademy.employee.dto.request.EmployeeUpdateRequest;
import com.archisacademy.employee.dto.response.*;
import com.archisacademy.employee.entity.Department;
import com.archisacademy.employee.entity.Employee;
import com.archisacademy.employee.helpers.CsvHelper;
import com.archisacademy.employee.helpers.ExcelHelper;
import com.archisacademy.employee.repository.DepartmentRepository;
import com.archisacademy.employee.repository.EmployeeRepository;
import com.archisacademy.employee.service.EmployeeService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    public EmployeeServiceImpl(DepartmentRepository departmentRepository, EmployeeRepository employeeRepository, ModelMapper modelMapper) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public EmployeeResponse addEmployee(EmployeeRequest request){
        Employee employee = modelMapper.map(request, Employee.class);
        employee.setId(null);
        if (request.getDepartmentId() != null) {
            Department department = departmentRepository.findById(request.getDepartmentId()).orElse(null);
            employee.setDepartment(department);
        }
        Employee saved = employeeRepository.save(employee);
        EmployeeResponse response = modelMapper.map(saved, EmployeeResponse.class);
        return response;
    }

    @Override
    public EmployeeResponse getEmployeeById(Long id){
        Employee employee = employeeRepository.findById(id).orElseThrow(()-> new RuntimeException("Employee cannot be found!!!"));
        return modelMapper.map(employee, EmployeeResponse.class);
    }

    @Override
    public List<EmployeeResponse> getAllEmployee(){
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream().map(employee -> modelMapper.map(employee, EmployeeResponse.class)).collect(Collectors.toList());
    }

    @Override
    public EmployeeUpdateResponse updateEmployee(Long id, EmployeeUpdateRequest request){
        Employee employee = employeeRepository.findById(id).orElseThrow(()-> new RuntimeException("Employee cannot be found"));
        modelMapper.map(request, employee);
        employeeRepository.save(employee);
        return new EmployeeUpdateResponse("employee updated successfully");
    }

    @Override
    public EmployeeResponse deleteEmployee(long id){
        Employee employee = employeeRepository.findById(id).orElseThrow(()-> new RuntimeException("Employee cannot be found"));
        employeeRepository.delete(employee);
        EmployeeResponse response = new EmployeeResponse();
        return response;
    }


    @Override
    public List<EmployeeResponseDto> searchEmployees(Long employeeId, String name, Long departmentId) {
        List<Employee> employees = employeeRepository.findAll(); // Tüm çalışanları getir

        return employees.stream()
                .filter(employee -> (employeeId == null || employee.getId().equals(employeeId))) // ID filtresi
                .filter(employee -> (name == null || employee.getFirstName().toLowerCase().contains(name.toLowerCase()))) // İsim filtresi
                .filter(employee -> (departmentId == null || (employee.getDepartment() != null && employee.getDepartment().getId().equals(departmentId)))) // Departman filtresi
                .map(employee -> {
                    // Employee'yi DTO'ya dönüştür
                    EmployeeResponseDto dto = modelMapper.map(employee, EmployeeResponseDto.class);

                    // Departman bilgisi varsa DTO'ya ekle
                    if (employee.getDepartment() != null) {
                        DepartmentResponse departmentDto = modelMapper.map(employee.getDepartment(), DepartmentResponse.class);
                        dto.setDepartmentResponse(departmentDto); // Departman bilgisi DTO'ya ekleniyor
                    }

                    return dto;
                })
                .collect(Collectors.toList()); // Liste olarak döndür
    }

    @Override
    public Map<String, Object> importEmployees(MultipartFile file) {
        List<EmployeeImportRequest> employees;
        List<String> errorMessages = new ArrayList<>();
        int successCount = 0;
        int failedCount = 0;

        try {
            InputStream is = file.getInputStream();
            String fileName = file.getOriginalFilename();

            if (fileName == null || fileName.isEmpty()) {
                throw new RuntimeException("Dosya adı alınamadı!");
            }

            if (fileName.endsWith(".csv")) {
                employees = CsvHelper.csvToEmployees(is);
            } else if (fileName.endsWith(".xls") || fileName.endsWith(".xlsx")) {
                employees = ExcelHelper.excelToEmployees(is);
            } else {
                throw new RuntimeException("Desteklenmeyen dosya formatı!");
            }

            for (EmployeeImportRequest request : employees) {
                try {

                    if (request.getEmail() == null || !request.getEmail().contains("@")) {
                        errorMessages.add("Geçersiz e-posta adresi: " + request.getEmail());
                        failedCount++;
                        continue;
                    }
                    if (request.getPhoneNumber() == null || request.getPhoneNumber().trim().isEmpty()) {
                        errorMessages.add("Telefon numarası boş olamaz.");
                        failedCount++;
                        continue;
                    }
                    Optional<Department> departmentOpt = departmentRepository.findById(request.getDepartmentId());
                    if (request.getDepartmentId() != null && departmentOpt.isEmpty()) {
                        errorMessages.add("Geçersiz Departman ID: " + request.getDepartmentId());
                        failedCount++;
                        continue;
                    }
                    LocalDate dateOfJoining = null;
                    if (request.getDateOfJoining() != null && !request.getDateOfJoining().isEmpty()) {
                        try {
                            dateOfJoining = LocalDate.parse(request.getDateOfJoining(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        } catch (Exception e) {
                            errorMessages.add("Hatalı tarih formatı (YYYY-MM-DD olmalı): " + request.getDateOfJoining());
                            failedCount++;
                            continue;
                        }
                    }
                    Optional<Employee> existingEmployee = employeeRepository.findByEmail(request.getEmail());
                    if (existingEmployee.isEmpty()) {
                        existingEmployee = employeeRepository.findByPhoneNumber(request.getPhoneNumber());
                    }

                    Employee employee;
                    if (existingEmployee.isPresent()) {
                        employee = existingEmployee.get();
                    } else {
                        employee = new Employee();
                        employee.setStatus("ACTIVE");
                    }

                    employee.setFirstName(request.getFirstName());
                    employee.setLastName(request.getLastName());
                    employee.setEmail(request.getEmail());
                    employee.setPhoneNumber(request.getPhoneNumber());
                    employee.setPosition(request.getPosition());
                    employee.setDateOfJoining(dateOfJoining);
                    departmentOpt.ifPresent(employee::setDepartment);

                    employeeRepository.save(employee);
                    successCount++;
                } catch (Exception e) {
                    errorMessages.add("Çalışan işlenirken hata oluştu: " + e.getMessage());
                    failedCount++;
                }
            }
            Map<String, Object> response = new HashMap<>();
            response.put("Başarıyla eklenen/güncellenen", successCount);
            response.put("Başarısız işlemler", failedCount);
            response.put("Hatalar", errorMessages);
            return response;

        } catch (Exception e) {
            throw new RuntimeException("Dosya işlenirken hata oluştu: " + e.getMessage());
        }
    }
    @Override
    public Map<String, String> exportEmployeesToFiles() {
        List<Employee> employees = employeeRepository.findAll();
        List<EmployeeExportDto> employeeDtos = employees.stream().map(emp -> new EmployeeExportDto(
                emp.getId(),
                emp.getFirstName(),
                emp.getLastName(),
                emp.getEmail(),
                emp.getPhoneNumber(),
                emp.getDepartment() != null ? emp.getDepartment().getDepartmentName(): "N/A",
                emp.getPosition(),
                emp.getDateOfJoining()
        )).collect(Collectors.toList());

        String csvFilePath = "employee_data_export.csv";
        String excelFilePath = "employee_data_export.xlsx";

        CsvHelper.writeEmployeesToCsv(employeeDtos, csvFilePath);
        ExcelHelper.writeEmployeesToExcel(employeeDtos, excelFilePath);

        Map<String, String> response = new HashMap<>();
        response.put("CSV File", csvFilePath);
        response.put("Excel File", excelFilePath);
        return response;
    }

}
