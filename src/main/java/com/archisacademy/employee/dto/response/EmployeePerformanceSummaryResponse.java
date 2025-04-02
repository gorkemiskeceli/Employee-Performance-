package com.archisacademy.employee.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeePerformanceSummaryResponse {

    private Long employeeId;

    private String firstName;

    private String lastName;

    private Float goalAchievement;

    private Double taskCompletionRate;

}
