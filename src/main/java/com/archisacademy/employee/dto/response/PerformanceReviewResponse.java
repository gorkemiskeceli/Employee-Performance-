package com.archisacademy.employee.dto.response;

import com.archisacademy.employee.enums.ReviewFrequency;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PerformanceReviewResponse {

    private Long employeeId;

    private LocalDateTime reviewDate;

    private Float goalAchievement;

    private String feedback;

    private BigDecimal rating;

    private ReviewFrequency frequency;

}
