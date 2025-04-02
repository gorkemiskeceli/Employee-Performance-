package com.archisacademy.employee.service.impl;

import com.archisacademy.employee.dto.request.GoalSearchRequest;
import com.archisacademy.employee.dto.request.GoalsRequest;
import com.archisacademy.employee.dto.response.GoalAchievementResponse;
import com.archisacademy.employee.dto.response.GoalSearchResponse;
import com.archisacademy.employee.dto.response.GoalsResponse;
import com.archisacademy.employee.dto.response.GoalsUpdateResponse;
import com.archisacademy.employee.entity.Employee;
import com.archisacademy.employee.entity.Goal;
import com.archisacademy.employee.enums.GoalStatus;
import com.archisacademy.employee.repository.EmployeeRepository;
import com.archisacademy.employee.repository.GoalRepository;
import com.archisacademy.employee.service.GoalsService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoalsServiceImpl implements GoalsService {

    private final EmployeeRepository employeeRepository;
    private final GoalRepository goalRepository;
    private final ModelMapper modelMapper;

    public GoalsServiceImpl(EmployeeRepository employeeRepository, GoalRepository goalRepository, ModelMapper modelMapper) {
        this.employeeRepository = employeeRepository;
        this.goalRepository = goalRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    @Override
    public GoalsResponse create(GoalsRequest request){

        Employee employee = employeeRepository.findById(request.getEmployee()).orElseThrow(()-> new RuntimeException("Employee cannot be found!!!"));

        Goal goal = modelMapper.map(request, Goal.class);
        goal.setEmployee(employee);
        Goal save = goalRepository.save(goal);
        GoalsResponse response = modelMapper.map(save, GoalsResponse.class);
        response.setMessage("Goal has been created");

        return response;
    }

    @Transactional
    @Override
    public GoalsUpdateResponse update(Long id, GoalsRequest request){
        Employee employee = employeeRepository.findById(request.getEmployee()).orElseThrow(()-> new RuntimeException("employee cannot be found"));
        Goal goal = goalRepository.findById(id).orElseThrow(()-> new RuntimeException("Goal cannot be found"));
        modelMapper.map(request, goal);
        goal.setEmployee(employee);
        Goal updated = goalRepository.save(goal);
        GoalsUpdateResponse response = modelMapper.map(updated, GoalsUpdateResponse.class);
        response.setMessage("Goal has been updated successfully");

        return response;
    }

    @Override
    public GoalsResponse delete(Long id){
        Goal goal = goalRepository.findById(id).orElseThrow(()-> new RuntimeException("Goal with " + id + "cannot be found!!!"));
        goalRepository.delete(goal);
        GoalsResponse response = new GoalsResponse();
        response.setId(id);
        response.setMessage("Goal with " + id + "has been deleted!!!");
        return response;
    }

    @Override
    public GoalAchievementResponse calculateGoalAchievemnt(Long employeeId){
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(()-> new RuntimeException("Employee not found!!"));

        List<Goal> allGoals = goalRepository.findByEmployeeId(employeeId);
        if (allGoals.isEmpty()){
            return new GoalAchievementResponse(employeeId, 0);
        }

        List<Goal> completedGoals = goalRepository.findByEmployee_IdAndStatus(employeeId, GoalStatus.COMPLETED);

        double completionPercentage = ((double) completedGoals.size() / allGoals.size() * 100);

        return new GoalAchievementResponse(employeeId, (int) completionPercentage);
    }

    @Override
    public List<GoalSearchResponse> searchGoals(GoalSearchRequest request){
        List<Goal> goals;

        if (request.getEmployeeId() != null){
            goals = goalRepository.findByEmployeeId(request.getEmployeeId());
        } else if (request.getGoalStatus() != null) {
            goals = goalRepository.findByStatus(GoalStatus.valueOf(request.getGoalStatus()));
        } else if (request.getTargetDate() != null) {
            goals = goalRepository.findByTargetDate(request.getTargetDate());
        }else {
            goals = goalRepository.findAll();
        }
        return goals.stream().map(goal -> new GoalSearchResponse(
                goal.getId(),
                goal.getGoalDescription(),
                goal.getStatus().name(),
                goal.getTargetDate(),
                goal.getEmployee().getId()
        )).collect(Collectors.toList());
    }

    @Override
    public List<Goal> getGaolsForEmployeeInDateRange(Long employeeId, LocalDate startDate, LocalDate endDate){
        return goalRepository.findByEmployeeIdAndTargetDateBetween(employeeId, startDate, endDate);
    }
}
