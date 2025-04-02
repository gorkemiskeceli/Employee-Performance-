package com.archisacademy.employee.dto.response;

import com.archisacademy.employee.entity.Employee;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GoalAchievementResponse {

    private Long employeeId;
    private int achievementPercentage;

}
