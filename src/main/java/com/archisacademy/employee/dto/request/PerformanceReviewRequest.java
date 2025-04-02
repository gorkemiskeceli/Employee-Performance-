package com.archisacademy.employee.dto.request;

import com.archisacademy.employee.enums.ReviewFrequency;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PerformanceReviewRequest {

    private Long employeeId;

    private Float goalAchievement;

    private String feedback;

    private BigDecimal rating;

    private ReviewFrequency frequency;

}
