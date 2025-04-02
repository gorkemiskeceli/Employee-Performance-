package com.archisacademy.employee.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TeamReportResponse {

    private int totalGoals;

    private int completedGoals;

    private double goalCompletion;

    private int totalTasks;

    private int completedTasks;

    private double taskCompletion;

    private String message;

    public TeamReportResponse(int totalGoals, int completedGoals, double goalCompletion,
                                 int totalTasks, int completedTasks, double taskCompletion, String message) {
        this.totalGoals = totalGoals;
        this.completedGoals = completedGoals;
        this.goalCompletion = goalCompletion;
        this.totalTasks = totalTasks;
        this.completedTasks = completedTasks;
        this.taskCompletion = taskCompletion;
        this.message = message;
    }

    public TeamReportResponse(String message) {
        this.message = message;
    }

}
