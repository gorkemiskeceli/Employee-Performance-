package com.archisacademy.employee.service.impl;

import com.archisacademy.employee.dto.response.EmployeePerformanceSummaryResponse;
import com.archisacademy.employee.dto.response.TeamPerformanceOverviewResponse;
import com.archisacademy.employee.entity.Department;
import com.archisacademy.employee.entity.Employee;
import com.archisacademy.employee.enums.Status;
import com.archisacademy.employee.repository.DepartmentRepository;
import com.archisacademy.employee.repository.EmployeeRepository;
import com.archisacademy.employee.service.ReportService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    public ReportServiceImpl(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public EmployeePerformanceSummaryResponse getEmployeePerformanceSummary(Long employeeId) {

        Employee employee = employeeRepository.findEmployeeById(employeeId);
        if (employee == null) return null;


        float goalAchievement = employee.getPerformanceReviews().stream()
                .findFirst()
                .map(pr -> pr.getGoalAchievement())
                .orElse(0f);

        long totalTaskCount = employee.getTasks().size();

        long completedTaskCount = employee.getTasks().stream()
                .filter(task -> Status.COMPLETED.equals(task.getStatus()))
                .count();

        // if ile yaptığımda değişken dışarda kullanılmadığından ternary operator kullandım.
        double taskCompletionRate = totalTaskCount > 0 ? (completedTaskCount * 100.0 / totalTaskCount) : 0.0;

        return new EmployeePerformanceSummaryResponse(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                goalAchievement,
                taskCompletionRate
        );
    }

    @Override
    public TeamPerformanceOverviewResponse getTeamPerformanceOverview(String teamName) {

        Department department = departmentRepository.findDepartmentByDepartmentName(teamName);
        if (department == null) return null;

        List<Employee> employees = department.getManagerId();
        if (employees.isEmpty()) return new TeamPerformanceOverviewResponse(teamName, 0f, 0.0, 0L);

        // Burada ilk employee listesi üzerinde stream başlatıyorum.
        float avgGoalAchievement = (float) employees.stream()
                .mapToDouble(e -> e.getPerformanceReviews().stream()
                        // Her bir çalışanın başarı oranını çekmek için tekrar stream başlatıyorum
                        .findFirst()
                        .map(pr -> pr.getGoalAchievement())
                        .orElse(0f))
                .average()
                .orElse(0.0);

        long totalTaskCount = employees.stream()
                .mapToLong(e -> e.getTasks().size())
                .sum();

        long completedTaskCount = employees.stream()
                .mapToLong(e -> e.getTasks().stream()
                        .filter(task -> Status.COMPLETED.equals(task.getStatus()))
                        .count())
                .sum();

        double avgTaskCompletionRate = totalTaskCount > 0 ? (completedTaskCount * 100.0 / totalTaskCount) : 0.0;

        return new TeamPerformanceOverviewResponse(
                teamName,
                avgGoalAchievement,
                avgTaskCompletionRate,
                (long) employees.size()
        );
    }

}