package com.archisacademy.employee.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeePerformanceResponse {

    private Long id;
    private String name;
    private Double goalAchievement;
    private Double taskCompletion;
    private Double overallRating;
}
