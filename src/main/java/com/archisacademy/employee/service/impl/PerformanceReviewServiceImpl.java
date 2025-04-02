package com.archisacademy.employee.service.impl;

import com.archisacademy.employee.dto.request.PerformanceReviewRequest;
import com.archisacademy.employee.dto.request.PerformanceReviewUpdateRequest;
import com.archisacademy.employee.dto.response.PerformanceReviewResponse;
import com.archisacademy.employee.dto.response.PerformanceTrendReport;
import com.archisacademy.employee.dto.response.PerformanceTrendResponse;
import com.archisacademy.employee.dto.response.UnderPerformanceResponse;
import com.archisacademy.employee.entity.Employee;
import com.archisacademy.employee.entity.PerformanceReview;
import com.archisacademy.employee.enums.ReviewFrequency;
import com.archisacademy.employee.exception.EmployeeNotFound;
import com.archisacademy.employee.exception.PerformanceReviewNotFound;
import com.archisacademy.employee.repository.EmployeeRepository;
import com.archisacademy.employee.repository.PerformanceReviewRepository;
import com.archisacademy.employee.service.PerformanceReviewService;
import com.archisacademy.employee.service.TaskService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PerformanceReviewServiceImpl implements PerformanceReviewService {

    private final PerformanceReviewRepository performanceReviewRepository;
    private final ModelMapper modelMapper;
    private final EmployeeRepository employeeRepository;
    private final TaskService taskService;

    public PerformanceReviewServiceImpl(PerformanceReviewRepository performanceReviewRepository, ModelMapper modelMapper, EmployeeRepository employeeRepository, TaskService taskService) {
        this.performanceReviewRepository = performanceReviewRepository;
        this.modelMapper = modelMapper;
        this.employeeRepository = employeeRepository;
        this.taskService = taskService;
    }

    @Override
    @Transactional()
    public PerformanceReviewResponse save(PerformanceReviewRequest performanceReviewRequest) {

        try {
            Employee employee = employeeRepository.findById(performanceReviewRequest.getEmployeeId())
                    .orElseThrow(() -> new EmployeeNotFound("Employee not found"));

            PerformanceReview performanceReview = modelMapper.map(performanceReviewRequest, PerformanceReview.class);
            performanceReview.setId(null);
            performanceReview.setEmployee(employee);

            PerformanceReview savedPerformanceReview = performanceReviewRepository.save(performanceReview);

            PerformanceReviewResponse reviewResponse = modelMapper.map(savedPerformanceReview, PerformanceReviewResponse.class);
            reviewResponse.setEmployeeId(employee.getId());

            return reviewResponse;

        } catch (RuntimeException e) {
            throw new RuntimeException("Error saving performance review: ", e);
        }

    }

    @Override
    public List<PerformanceReviewResponse> findAll() {

        try {

            List<PerformanceReview> performanceReviewList = performanceReviewRepository.findAll();

            return performanceReviewList.stream()
                    .map(performanceReview -> modelMapper.map(performanceReview, PerformanceReviewResponse.class))
                    .toList();

        } catch (RuntimeException e) {
            throw new RuntimeException("Error finding performance review: ", e);
        }

    }

    @Override
    public List<PerformanceReviewResponse> findPerformanceReviewsByEmployeeId(Long employeeId) {

        try {

            List<PerformanceReview> performanceReviewList = performanceReviewRepository.findByEmployee_Id(employeeId);

            return performanceReviewList.stream()
                    .map(performanceReview -> modelMapper.map(performanceReview, PerformanceReviewResponse.class))
                    .toList();

        } catch (RuntimeException e) {
            throw new RuntimeException("Error finding performance review by employeeId " + employeeId + " :", e);
        }

    }

    @Override
    @Transactional
    public PerformanceReviewResponse updatePerformanceReviewByReviewId(Long reviewId, PerformanceReviewUpdateRequest performanceReviewUpdateRequest) {

        try {

            PerformanceReview performanceReview = performanceReviewRepository.findById(reviewId)
                    .orElseThrow(() -> new PerformanceReviewNotFound("Performance review not found for reviewId: " + reviewId));

            modelMapper.map(performanceReviewUpdateRequest, performanceReview);

            PerformanceReview savedPerformanceReview = performanceReviewRepository.save(performanceReview);

            return modelMapper.map(savedPerformanceReview, PerformanceReviewResponse.class);

        } catch (RuntimeException e) {
            throw new RuntimeException("Error updating performance review by reviewId " + reviewId + " :", e);
        }

    }

    @Override
    public PerformanceTrendResponse getPerformanceTrend(Long employeeId) {
        List<PerformanceReview> reviews = performanceReviewRepository.findByEmployeeIdOrderByReviewDateDesc(employeeId);

        if (reviews == null || reviews.isEmpty()) {
            return new PerformanceTrendResponse("No review data available for trend analysis.");
        }

        // Son 2 review dönemini alıyorum
        int limit = Math.min(2, reviews.size());
        List<PerformanceTrendReport> trendReports = new ArrayList<>();

        for (int i = 0; i < limit; i++) {
            PerformanceReview review = reviews.get(i);
            PerformanceTrendReport report = new PerformanceTrendReport();

            report.setReviewDate(review.getReviewDate());

            if (review.getGoalAchievement() != null) {
                report.setGoalAchievement(review.getGoalAchievement());
            } else {
                report.setGoalAchievement(0);
            }

            if (review.getRating() != null) {
                float taskCompletion = review.getRating().floatValue();
                report.setTaskCompletion(taskCompletion);
            } else {
                report.setTaskCompletion(0);
            }

            if (review.getRating() != null) {
                float overallRating = review.getRating().floatValue();
                report.setOverallRating(overallRating);
            } else {
                report.setOverallRating(0);
            }

            trendReports.add(report);
        }

        return new PerformanceTrendResponse(trendReports, "Performance Trend Analytics was created.");
    }

    @Override
    @Scheduled(fixedRate = 3600000, initialDelay = 5000)
    @Transactional
    public void scheduleReviews() {
        List<Employee> employeeList = employeeRepository.findAll();
        LocalDateTime today = LocalDateTime.now();

        for (Employee employee : employeeList) {
            List<PerformanceReview> reviews = employee.getPerformanceReviews();

            if (reviews.isEmpty()) {

                PerformanceReview firstReview = new PerformanceReview();
                firstReview.setEmployee(employee);
                firstReview.setReviewDate(today);
                firstReview.setFrequency(ReviewFrequency.QUARTERLY);
                firstReview.setNextReviewDate(calculateNextReviewDate(today, ReviewFrequency.QUARTERLY));

                performanceReviewRepository.save(firstReview);

                System.out.println("First review added for: " + employee.getFirstName());
            } else {
                PerformanceReview lastReview = reviews.getLast();
                LocalDateTime nextReviewDate = lastReview.getNextReviewDate();

                if (nextReviewDate == null || !nextReviewDate.isAfter(today)) {

                    PerformanceReview newReview = new PerformanceReview();

                    newReview.setEmployee(employee);
                    newReview.setReviewDate(today);
                    newReview.setFrequency(lastReview.getFrequency());
                    newReview.setNextReviewDate(calculateNextReviewDate(today, lastReview.getFrequency()));

                    performanceReviewRepository.save(newReview);

                    System.out.println("Reminder: Next review for " + employee.getFirstName() + " " + employee.getLastName() +
                            " is on " + newReview.getNextReviewDate());
                }
            }
        }
    }

    private LocalDateTime calculateNextReviewDate(LocalDateTime lastDate, ReviewFrequency frequency) {

        if (frequency == ReviewFrequency.QUARTERLY) {
            return lastDate.plusMonths(3);
        } else if (frequency == ReviewFrequency.ANNUALLY) {
            return lastDate.plusYears(1);
        }

        return lastDate.plusMonths(3);
    }

    @Override
    public List<UnderPerformanceResponse> generateUnderPerformanceAnalysis() {
        List<PerformanceReview> allReviews = performanceReviewRepository.findAll();

        if (allReviews.isEmpty()) {
            throw new RuntimeException("No underperformers found");
        }

        List<PerformanceReview> latestReviews = allReviews.stream()
                .collect(Collectors.groupingBy(
                        pr -> pr.getEmployee().getId(),
                        Collectors.maxBy(Comparator.comparing(PerformanceReview::getReviewDate))
                ))
                .values().stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        List<UnderPerformanceResponse> underperformers = new ArrayList<>();

        for (PerformanceReview review : latestReviews) {
            Employee employee = review.getEmployee();
            float goalAchievement = review.getGoalAchievement() != null ? review.getGoalAchievement() : 0f;
            float taskCompletion = taskService.calculateTaskCompletionPercentage(employee.getId());

            if (goalAchievement < 50 && taskCompletion < 50) {
                UnderPerformanceResponse response = new UnderPerformanceResponse();
                response.setEmployeeName(employee.getFirstName() + " " + employee.getLastName());
                response.setGoalAchievement(goalAchievement);
                response.setTaskCompletion(taskCompletion);

                underperformers.add(response);
            }
        }

        if (underperformers.isEmpty()) {
            throw new RuntimeException("No underperformers found.");
        }

        return underperformers;
    }

}
