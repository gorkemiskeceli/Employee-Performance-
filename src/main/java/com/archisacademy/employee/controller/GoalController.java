package com.archisacademy.employee.controller;


import com.archisacademy.employee.dto.request.GoalSearchRequest;
import com.archisacademy.employee.dto.request.GoalsRequest;
import com.archisacademy.employee.dto.response.*;
import com.archisacademy.employee.service.GoalAnalyticsService;
import com.archisacademy.employee.service.GoalsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/goals")
public class GoalController {

    private final GoalsService goalsService;
    private final GoalAnalyticsService goalAnalyticsService;

    public GoalController(GoalsService goalsService, GoalAnalyticsService goalAnalyticsService) {
        this.goalsService = goalsService;
        this.goalAnalyticsService = goalAnalyticsService;
    }


    @PostMapping("/create")
    public ResponseEntity<GoalsResponse> create(@RequestBody GoalsRequest goalsRequest){
        GoalsResponse response = goalsService.create(goalsRequest);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<GoalsUpdateResponse> update(@PathVariable Long id, @RequestBody GoalsRequest request){
        GoalsUpdateResponse response = goalsService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GoalsResponse> delete(@PathVariable Long id){
        GoalsResponse response = goalsService.delete(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/achievement/{employeeId}")
    public GoalAchievementResponse getGoalAchievement(@PathVariable Long employeeId){
        return goalsService.calculateGoalAchievemnt(employeeId);
    }

    @GetMapping("/analytic/{employeeId}")
    public ResponseEntity<GoalAnalyticsResponse> generateGoalAnalytics(@PathVariable Long employeeId) {
        GoalAnalyticsResponse response = goalAnalyticsService.generateGoalAnalytics(employeeId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/search")
    public List<GoalSearchResponse> searchGoals(@RequestBody GoalSearchRequest request){
        return goalsService.searchGoals(request);
    }
}
