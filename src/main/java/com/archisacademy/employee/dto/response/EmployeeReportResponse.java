package com.archisacademy.employee.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeReportResponse {

    private String employeeName;

    private int totalGoals;

    private int completedGoals;

    private double goalAchievement;

    private int totalTasks;

    private int completedTasks;

    private double taskCompletion;

    private double overallRating;

    private String message;

    public EmployeeReportResponse(String employeeName, int totalGoals, int totalTasks, String message) {
        this.employeeName = employeeName;
        this.totalGoals = totalGoals;
        this.totalTasks = totalTasks;
        this.message = message;
    }

}
