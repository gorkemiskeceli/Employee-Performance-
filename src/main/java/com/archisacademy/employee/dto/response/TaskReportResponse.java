package com.archisacademy.employee.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskReportResponse {
    private int totalTasks;
    private int completedTasks;
    private double completionRate;
    private double averageCompletionTime;
    private int overdueTasks;
    private String message;
}
