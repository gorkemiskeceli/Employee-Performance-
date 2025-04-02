package com.archisacademy.employee.dto.response;


import com.archisacademy.employee.enums.GoalStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GoalsUpdateResponse {


    private Long id;
    private String goalDescription;
    private BigDecimal progress;
    private GoalStatus status;
    private String message;
}
