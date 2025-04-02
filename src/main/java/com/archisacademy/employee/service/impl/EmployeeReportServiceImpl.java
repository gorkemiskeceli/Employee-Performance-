package com.archisacademy.employee.service.impl;

import com.archisacademy.employee.dto.response.EmployeeReportResponse;
import com.archisacademy.employee.dto.response.TeamReportResponse;
import com.archisacademy.employee.entity.Employee;
import com.archisacademy.employee.entity.Goal;
import com.archisacademy.employee.entity.Task;
import com.archisacademy.employee.enums.GoalStatus;
import com.archisacademy.employee.enums.Status;
import com.archisacademy.employee.exception.EmployeeNotFound;
import com.archisacademy.employee.repository.EmployeeRepository;
import com.archisacademy.employee.repository.GoalRepository;
import com.archisacademy.employee.repository.TaskRepository;
import com.archisacademy.employee.service.EmployeeReportService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeReportServiceImpl implements EmployeeReportService {

    private final EmployeeRepository employeeRepository;
    private final GoalRepository goalRepository;
    private final TaskRepository taskRepository;

    public EmployeeReportServiceImpl(EmployeeRepository employeeRepository, GoalRepository goalRepository, TaskRepository taskRepository) {
        this.employeeRepository = employeeRepository;
        this.goalRepository = goalRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public EmployeeReportResponse getEmployeePerformance(Long employeeId) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFound("Employee not found with id: " + employeeId));

        List<Task> tasks = taskRepository.findByEmployeeId(employeeId);
        int totalTasks = tasks.size();

        int completedTasks = (int) tasks.stream()
                .filter(task -> task.getStatus() == Status.COMPLETED)
                .count();

        List<Goal> goals = goalRepository.findByEmployeeId(employeeId);
        int totalGoals = goals.size();

        int completedGoals = (int) goals.stream()
                .filter(goal -> goal.getStatus() == GoalStatus.COMPLETED)
                .count();

        if (totalGoals == 0 && totalTasks == 0) {
            return new EmployeeReportResponse(
                    employee.getFirstName() + " " + employee.getLastName(), totalGoals, totalTasks,
                    "No goals or tasks assigned to this employee."
            );
        }

        double goalAchievement = 0;
        double taskCompletion = 0;

        if (totalGoals > 0) {
            goalAchievement = (completedGoals * 100.0 / totalGoals);
        }

        if (totalTasks > 0) {
            taskCompletion = (completedTasks * 100.0 / totalTasks);
        }

        double overallRating = (goalAchievement + taskCompletion) / 2;

        return new EmployeeReportResponse(
                employee.getFirstName() + " " + employee.getLastName(),
                totalGoals,
                completedGoals,
                goalAchievement,
                totalTasks,
                completedTasks,
                taskCompletion,
                overallRating,
                "Employee report has been created successfully for employee id: " + employeeId
        );

    }

    @Override
    public TeamReportResponse getTeamPerformance(Long departmentId) {

        List<Employee> employees = employeeRepository.findByDepartmentId(departmentId);

        if (employees == null || employees.isEmpty()) {
            return new TeamReportResponse("No data available for the department.");
        }

        int totalGoals = 0;
        int completedGoals = 0;
        int totalTasks = 0;
        int completedTasks = 0;

        for (Employee employee : employees) {

            Long employeeId = employee.getId();

            List<Goal> goals = goalRepository.findByEmployeeId(employeeId);
            totalGoals += goals.size();
            completedGoals += (int) goals.stream()
                    .filter(goal -> goal.getStatus() == GoalStatus.COMPLETED)
                    .count();

            List<Task> tasks = taskRepository.findByEmployeeId(employeeId);
            totalTasks += tasks.size();
            completedTasks += (int) tasks.stream()
                    .filter(task -> task.getStatus() == Status.COMPLETED)
                    .count();

        }

        if (totalGoals == 0 && totalTasks == 0) {
            return new TeamReportResponse("No data available for the department.");
        }

        double goalCompletion = 0;
        if (totalGoals > 0) {
            goalCompletion = (completedGoals * 100.0 / totalGoals);
        }

        double taskCompletion = 0;
        if (totalTasks > 0) {
            taskCompletion = (completedTasks * 100.0 / totalTasks);
        }

        return new TeamReportResponse(
                totalGoals,
                completedGoals,
                goalCompletion,
                totalTasks,
                completedTasks,
                taskCompletion,
                "Department report has been created successfully for department id: " + departmentId
        );

    }

}
