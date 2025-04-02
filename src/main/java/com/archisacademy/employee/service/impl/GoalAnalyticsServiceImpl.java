package com.archisacademy.employee.service.impl;

import com.archisacademy.employee.dto.response.GoalAnalyticsResponse;
import com.archisacademy.employee.entity.Employee;
import com.archisacademy.employee.entity.Goal;
import com.archisacademy.employee.enums.GoalStatus;
import com.archisacademy.employee.exception.EmployeeNotFound;
import com.archisacademy.employee.repository.EmployeeRepository;
import com.archisacademy.employee.repository.GoalRepository;
import com.archisacademy.employee.service.GoalAnalyticsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoalAnalyticsServiceImpl implements GoalAnalyticsService {

    private final GoalRepository goalRepository;
    private final EmployeeRepository employeeRepository;

    public GoalAnalyticsServiceImpl(GoalRepository goalRepository, EmployeeRepository employeeRepository) {
        this.goalRepository = goalRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public GoalAnalyticsResponse generateGoalAnalytics(Long employeeId) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFound("Employee not found with ID: " + employeeId));

        List<Goal> allGoals = goalRepository.findByEmployeeId(employeeId);
        List<Goal> completedGoals = goalRepository.findByEmployee_IdAndStatus(employeeId, GoalStatus.COMPLETED);
        List<Goal> inProgressGoals = goalRepository.findByEmployee_IdAndStatus(employeeId, GoalStatus.IN_PROGRESS);

        GoalAnalyticsResponse response = new GoalAnalyticsResponse();
        response.setEmployeeName(employee.getFirstName() + " " + employee.getLastName());

        if (allGoals.isEmpty()) {
            response.setTotalGoals(0);
            response.setCompletedGoals(0);
            response.setGoalCompletionRate(0);
            response.setInProgressGoals(0);
            response.setMessage("No goals assigned to this employee.");

            return response;
        }

        int totalGoals = allGoals.size();
        int completedGoalsCount = completedGoals.size();
        int inProgressGoalsCount = inProgressGoals.size();
        float goalCompletionRate = (float) (completedGoalsCount * 100) / totalGoals;

        response.setTotalGoals(totalGoals);
        response.setCompletedGoals(completedGoalsCount);
        response.setGoalCompletionRate(goalCompletionRate);
        response.setInProgressGoals(inProgressGoalsCount);
        response.setMessage("Analytics calculated for Employee!");

        return response;
    }

}
