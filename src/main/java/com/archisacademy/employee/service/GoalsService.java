package com.archisacademy.employee.service;


import com.archisacademy.employee.dto.request.GoalSearchRequest;
import com.archisacademy.employee.dto.request.GoalsRequest;
import com.archisacademy.employee.dto.response.GoalAchievementResponse;
import com.archisacademy.employee.dto.response.GoalSearchResponse;
import com.archisacademy.employee.dto.response.GoalsResponse;
import com.archisacademy.employee.dto.response.GoalsUpdateResponse;
import com.archisacademy.employee.entity.Goal;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface GoalsService {
    @Transactional
    GoalsResponse create(GoalsRequest request);

    @Transactional
    GoalsUpdateResponse update(Long id, GoalsRequest request);

    GoalsResponse delete(Long id);

    GoalAchievementResponse calculateGoalAchievemnt(Long employeeId);

    List<GoalSearchResponse> searchGoals(GoalSearchRequest request);

    List<Goal> getGaolsForEmployeeInDateRange(Long employeeId, LocalDate startDate, LocalDate endDate);
}
