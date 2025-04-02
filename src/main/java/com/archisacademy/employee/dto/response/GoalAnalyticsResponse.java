package com.archisacademy.employee.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GoalAnalyticsResponse {

    private String employeeName;

    private int totalGoals;

    private int completedGoals;

    private float goalCompletionRate;

    private int inProgressGoals;

    private String message;

}
