package com.archisacademy.employee.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomPerformanceRequest {

    private Long employeeId;
    private String department;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean includeGoalAchievement;
    private boolean includeTaskCompletion;
}
