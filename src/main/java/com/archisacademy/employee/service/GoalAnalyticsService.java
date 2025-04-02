package com.archisacademy.employee.service;

import com.archisacademy.employee.dto.response.GoalAnalyticsResponse;

public interface GoalAnalyticsService {

    GoalAnalyticsResponse generateGoalAnalytics(Long employeeId);

}
