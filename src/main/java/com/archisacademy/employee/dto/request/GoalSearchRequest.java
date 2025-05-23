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
public class GoalSearchRequest {

    private Long employeeId;
    private String goalStatus;
    private LocalDate targetDate;
}
