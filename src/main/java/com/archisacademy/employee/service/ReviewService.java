package com.archisacademy.employee.service;

import com.archisacademy.employee.dto.request.ReviewRequest;
import com.archisacademy.employee.dto.response.ReviewResponse;

import java.util.List;

public interface ReviewService {
    List<ReviewResponse> getReviewsByGoalId(Long goalId);

    String addReviewToGoal(Long employeeId, long goalId, ReviewRequest request);
}
