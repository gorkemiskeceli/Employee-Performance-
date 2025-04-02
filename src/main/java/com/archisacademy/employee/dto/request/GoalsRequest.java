package com.archisacademy.employee.dto.request;

import com.archisacademy.employee.entity.Employee;
import com.archisacademy.employee.enums.GoalStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GoalsRequest {

    @NotBlank(message = "Goal descriptipn must be entered!!!")
    private String goalDescription;

    @NotNull(message = "Target date must be entered")
    private LocalDate targetDate;

    @NotNull(message = "Progress should be entered")
    private BigDecimal progress;

    @NotBlank(message = "Please enter status of the goal")
    private String status;

    private Long employee;

}
