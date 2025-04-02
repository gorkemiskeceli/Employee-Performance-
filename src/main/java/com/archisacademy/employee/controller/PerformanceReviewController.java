package com.archisacademy.employee.controller;

import com.archisacademy.employee.dto.request.PerformanceReviewRequest;
import com.archisacademy.employee.dto.request.PerformanceReviewUpdateRequest;
import com.archisacademy.employee.dto.response.PerformanceReviewResponse;
import com.archisacademy.employee.dto.response.PerformanceTrendResponse;
import com.archisacademy.employee.dto.response.UnderPerformanceResponse;
import com.archisacademy.employee.service.PerformanceReviewService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/performance")
public class PerformanceReviewController {

    private final PerformanceReviewService performanceReviewService;

    public PerformanceReviewController(PerformanceReviewService performanceReviewService) {
        this.performanceReviewService = performanceReviewService;
    }

    @PostMapping
    public ResponseEntity<PerformanceReviewResponse> savePerformanceReview(@RequestBody PerformanceReviewRequest performanceReviewRequest) {
        PerformanceReviewResponse performanceReviewResponse = performanceReviewService.save(performanceReviewRequest);
        return new ResponseEntity<>(performanceReviewResponse, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<PerformanceReviewResponse>> getAllPerformanceReviews() {
        List<PerformanceReviewResponse> reviewResponseList = performanceReviewService.findAll();
        return new ResponseEntity<>(reviewResponseList, HttpStatus.OK);
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<PerformanceReviewResponse>> findPerformanceReviewsByEmployeeId(@PathVariable Long employeeId) {
        List<PerformanceReviewResponse> reviewResponseList = performanceReviewService.findPerformanceReviewsByEmployeeId(employeeId);
        return new ResponseEntity<>(reviewResponseList, HttpStatus.OK);
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<PerformanceReviewResponse> updatePerformanceReviewByReviewId(
            @PathVariable Long reviewId,
            @Valid @RequestBody PerformanceReviewUpdateRequest performanceReviewUpdateRequest) {
        PerformanceReviewResponse reviewResponse = performanceReviewService.updatePerformanceReviewByReviewId(reviewId, performanceReviewUpdateRequest);
        return new ResponseEntity<>(reviewResponse, HttpStatus.OK);
    }

    @GetMapping("/trend/{employeeId}")
    public ResponseEntity<PerformanceTrendResponse> getPerformanceTrend(@PathVariable Long employeeId) {
        PerformanceTrendResponse performanceTrendResponse = performanceReviewService.getPerformanceTrend(employeeId);
        return new ResponseEntity<>(performanceTrendResponse, HttpStatus.OK);
    }

    @GetMapping("/underperformance-report")
    public ResponseEntity<List<UnderPerformanceResponse>> generateUnderPerformanceAnalysis() {
        List<UnderPerformanceResponse> underperformers = performanceReviewService.generateUnderPerformanceAnalysis();
        return ResponseEntity.ok(underperformers);
    }

}
