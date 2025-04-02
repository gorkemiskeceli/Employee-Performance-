package com.archisacademy.employee.repository;

import com.archisacademy.employee.entity.Task;
import com.archisacademy.employee.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByEmployeeId(Long employeeId);

    List<Task> findByStatusNotAndDeadlineBefore(Status status, Date deadline);

    List<Task> findByStatusAndEmployeeId(Status status, Long id);

    List<Task> findByDeadlineBetween(Date start, Date end);

    List<Task> findByEmployeeIdAndDeadlineBetween(Long employeeId, LocalDate startDate, LocalDate endDate);
}
