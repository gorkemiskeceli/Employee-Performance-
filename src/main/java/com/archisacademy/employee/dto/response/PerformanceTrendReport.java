package com.archisacademy.employee.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PerformanceTrendReport {

    private LocalDateTime reviewDate;

    private float goalAchievement;

    private float taskCompletion;

    private float overallRating;

    private String message;

}
