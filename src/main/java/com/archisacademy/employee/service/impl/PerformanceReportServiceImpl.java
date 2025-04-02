package com.archisacademy.employee.service.impl;

import com.archisacademy.employee.dto.request.CustomPerformanceRequest;
import com.archisacademy.employee.dto.response.*;
import com.archisacademy.employee.entity.Employee;
import com.archisacademy.employee.entity.Goal;
import com.archisacademy.employee.entity.Task;
import com.archisacademy.employee.helpers.PdfHelper;
import com.archisacademy.employee.repository.EmployeeRepository;
import com.archisacademy.employee.service.PerformanceReportService;
import com.archisacademy.employee.service.GoalsService;
import com.archisacademy.employee.service.TaskService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PerformanceReportServiceImpl implements PerformanceReportService {

    private final EmployeeRepository employeeRepository;
    private final GoalsService goalsService;
    private final TaskService taskService;

    public PerformanceReportServiceImpl(EmployeeRepository employeeRepository, GoalsService goalsService, TaskService taskService) {
        this.employeeRepository = employeeRepository;
        this.goalsService = goalsService;
        this.taskService = taskService;
    }
    @Override
    public VisualizationResponse generateVisualization(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        GoalAchievementResponse goalResponse = goalsService.calculateGoalAchievemnt(employeeId);
        TaskReportResponse taskResponse = taskService.getEmployeeTaskReport(employeeId);

        String goalGraph = generateGraph(goalResponse.getAchievementPercentage());
        String taskGraph = generateGraph(taskResponse.getCompletionRate());

        return new VisualizationResponse(employee.getFirstName() + " " + employee.getLastName(), goalGraph, taskGraph);
    }

    @Override
    public PerformanceReportPdfResponse generatePerformanceReport(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        String filePath = "performance_report_" + employeeId + ".pdf";

        GoalAchievementResponse goalResponse = goalsService.calculateGoalAchievemnt(employeeId);
        TaskReportResponse taskResponse = taskService.getEmployeeTaskReport(employeeId);

        String goalGraph = generateGraph(goalResponse.getAchievementPercentage());
        String taskGraph = generateGraph(taskResponse.getCompletionRate());

        PdfHelper.generatePerformanceReport(filePath, employee.getFirstName() + " " + employee.getLastName(),
                goalResponse.getAchievementPercentage(), taskResponse.getCompletionRate(),
                goalGraph, taskGraph);

        return new PerformanceReportPdfResponse(filePath, "Performance report generated successfully.");
    }
    private String generateGraph(double percentage) {
        int filledBars = (int) (percentage / 10);
        return "[" + "*".repeat(filledBars) + "-".repeat(10 - filledBars) + "] " + percentage + "%";
    }

    @Override
    public List<EmployeePerformanceResponse> getTopPerformers(){
        List<Employee> employees = employeeRepository.findAll();

        List<EmployeePerformanceResponse> performanceList = employees.stream().map(employee -> {
            GoalAchievementResponse goalAchievementResponse = goalsService.calculateGoalAchievemnt(employee.getId());
            Double goalAchievement = (double) goalAchievementResponse.getAchievementPercentage();

            Double taskCompletion = (double) taskService.calculateTaskCompletionPercentage(employee.getId());

            Double overallRating = (goalAchievement + taskCompletion) / 2;

            return new EmployeePerformanceResponse(
                    employee.getId(),
                    employee.getFirstName(),
                    goalAchievement,
                    taskCompletion,
                    overallRating
            );
        }).sorted(Comparator.comparingDouble(EmployeePerformanceResponse:: getOverallRating).reversed())
                .collect(Collectors.toList());

        return performanceList;
    }

    @Override
    public CustomPerformanceResponse customReport(CustomPerformanceRequest request){
        Employee employee = employeeRepository.findById(request.getEmployeeId()).orElseThrow(() -> new RuntimeException("Employee not found!!!"));

        List<Task> tasks = taskService.getTasksForEmployeeInDateRange(request.getEmployeeId(), request.getStartDate(), request.getEndDate());
        List<Goal> goals = goalsService.getGaolsForEmployeeInDateRange(request.getEmployeeId(), request.getStartDate(), request.getEndDate());

        if (tasks.isEmpty() && goals.isEmpty()){
            throw new RuntimeException("No data matches the provided criteria!!!");
        }

        GoalAchievementResponse goalResponse = goalsService.calculateGoalAchievemnt(request.getEmployeeId());
        TaskReportResponse taskResponse = taskService.getEmployeeTaskReport(request.getEmployeeId());

        double goalAchievement = goalResponse.getAchievementPercentage();
        double taskCompletion = taskResponse.getCompletionRate();

        double overallRating = (goalAchievement + taskCompletion) / 2;

        return new CustomPerformanceResponse(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                taskResponse.getCompletionRate(),
                goalAchievement,
                overallRating
        );
    }
}
