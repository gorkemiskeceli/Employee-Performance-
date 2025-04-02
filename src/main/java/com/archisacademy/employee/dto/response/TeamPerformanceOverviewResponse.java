package com.archisacademy.employee.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeamPerformanceOverviewResponse {

    private String teamName;

    private Float avgGoalAchievement;

    private Double avgTaskCompletionRate;

    private Long teamMemberCount;

}
