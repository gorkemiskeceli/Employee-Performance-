package com.archisacademy.employee.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GoalSearchResponse {

    private Long goalId;
    private String description;
    private String goalStatus;
    private LocalDate targetDate;
    private Long employeeId;
}
