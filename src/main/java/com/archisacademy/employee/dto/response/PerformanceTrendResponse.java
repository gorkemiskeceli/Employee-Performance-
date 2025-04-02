package com.archisacademy.employee.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PerformanceTrendResponse {

    private List<PerformanceTrendReport> reviews;

    private String message;

    public PerformanceTrendResponse(String message) {
        this.message = message;
    }

    public PerformanceTrendResponse(List<PerformanceTrendReport> reviews, String message) {
        this.reviews = reviews;
        this.message = message;
    }

}
