package com.archisacademy.employee.service;

import com.archisacademy.employee.dto.request.PerformanceReviewRequest;
import com.archisacademy.employee.dto.request.PerformanceReviewUpdateRequest;
import com.archisacademy.employee.dto.response.PerformanceReviewResponse;
import com.archisacademy.employee.dto.response.PerformanceTrendResponse;
import com.archisacademy.employee.dto.response.UnderPerformanceResponse;

import java.util.List;

public interface PerformanceReviewService {

    PerformanceReviewResponse save(PerformanceReviewRequest performanceReviewRequest);

    List<PerformanceReviewResponse> findAll();

    List<PerformanceReviewResponse> findPerformanceReviewsByEmployeeId(Long employeeId);

    PerformanceReviewResponse updatePerformanceReviewByReviewId(Long reviewId, PerformanceReviewUpdateRequest performanceReviewUpdateRequest);

    PerformanceTrendResponse getPerformanceTrend(Long employeeId);

    void scheduleReviews();

    List<UnderPerformanceResponse> generateUnderPerformanceAnalysis();

}
