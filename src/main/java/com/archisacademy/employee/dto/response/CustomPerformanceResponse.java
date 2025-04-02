package com.archisacademy.employee.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomPerformanceResponse {

    private Long employeeId;
    private String firstName;
    private String lastName;
    private double taskCompletionRate;
    private double goalAchievement;
    private double overallRating;
}
