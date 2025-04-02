package com.archisacademy.employee.repository;

import com.archisacademy.employee.entity.Goal;
import com.archisacademy.employee.enums.GoalStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface GoalRepository extends JpaRepository<Goal, Long> {

    List<Goal> findByEmployeeId(Long employeeId);
    List<Goal> findByEmployee_IdAndStatus(Long employeeId, GoalStatus status);
    List<Goal> findByStatus(GoalStatus status);
    List<Goal> findByTargetDate(LocalDate targetDate);

    List<Goal> findByEmployeeIdAndTargetDateBetween(Long employeeId, LocalDate startDate, LocalDate endDate);
}
