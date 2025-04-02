package com.archisacademy.employee.controller;

import com.archisacademy.employee.dto.request.ReviewRequest;
import com.archisacademy.employee.dto.response.ReviewResponse;
import com.archisacademy.employee.service.ReviewService;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/{goalId}")
    public ResponseEntity<List<ReviewResponse>> getReviewsByGoalId(@PathVariable Long goalId){
        return ResponseEntity.ok(reviewService.getReviewsByGoalId(goalId));
    }

    @PostMapping("/submit/{employeeId}/{goalId}")
    public ResponseEntity<String> addReview(@PathVariable Long employeeId, @PathVariable Long goalId, @RequestBody ReviewRequest request){
        return ResponseEntity.ok(reviewService.addReviewToGoal(employeeId, goalId, request));
    }
}
