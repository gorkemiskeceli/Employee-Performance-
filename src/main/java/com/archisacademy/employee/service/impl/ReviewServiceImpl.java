package com.archisacademy.employee.service.impl;

import com.archisacademy.employee.dto.request.ReviewRequest;
import com.archisacademy.employee.dto.response.ReviewResponse;
import com.archisacademy.employee.entity.Employee;
import com.archisacademy.employee.entity.Goal;
import com.archisacademy.employee.entity.Review;
import com.archisacademy.employee.repository.EmployeeRepository;
import com.archisacademy.employee.repository.GoalRepository;
import com.archisacademy.employee.repository.ReviewRepository;
import com.archisacademy.employee.service.ReviewService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class ReviewServiceImpl implements ReviewService {

    private final ModelMapper modelMapper;
    private final EmployeeRepository employeeRepository;
    private final GoalRepository goalRepository;
    private final ReviewRepository reviewRepository;

    public ReviewServiceImpl(ModelMapper modelMapper, EmployeeRepository employeeRepository, GoalRepository goalRepository, ReviewRepository reviewRepository) {
        this.modelMapper = modelMapper;
        this.employeeRepository = employeeRepository;
        this.goalRepository = goalRepository;
        this.reviewRepository = reviewRepository;
    }

    @Override
    public List<ReviewResponse> getReviewsByGoalId(Long goalId){
        Goal goal = goalRepository.findById(goalId).orElseThrow(()-> new RuntimeException("Goal not found!!!"));

        List<Review> reviews = goal.getReviews();
        if (reviews.isEmpty()){
            throw new RuntimeException("There is no reviews!!");
        }

        return goal.getReviews().stream().map(review -> modelMapper.map(review, ReviewResponse.class)).collect(Collectors.toList());
    }

    @Override
    public String addReviewToGoal(Long employeeId, long goalId, ReviewRequest request){
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(()-> new RuntimeException("Goal not found!!!"));

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(()-> new RuntimeException("Employee not found!!!"));

        Review review = new Review();
        review.setGoal(goal);
        review.setEmployee(employee);
        review.setReviewText(request.getReviewText());
        reviewRepository.save(review);

        return "Review successfully added!";

    }
}
