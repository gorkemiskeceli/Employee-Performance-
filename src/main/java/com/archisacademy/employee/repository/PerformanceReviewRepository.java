package com.archisacademy.employee.repository;

import com.archisacademy.employee.entity.PerformanceReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PerformanceReviewRepository extends JpaRepository<PerformanceReview, Long> {

    List<PerformanceReview> findByEmployee_Id(Long employeeId);

    List<PerformanceReview> findByEmployeeIdOrderByReviewDateDesc(Long employeeId);

}
