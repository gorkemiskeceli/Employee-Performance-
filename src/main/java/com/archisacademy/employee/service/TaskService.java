package com.archisacademy.employee.service;

import com.archisacademy.employee.dto.request.TaskRequest;
import com.archisacademy.employee.dto.request.TaskStatusUpdateRequest;
import com.archisacademy.employee.dto.response.*;
import com.archisacademy.employee.entity.Task;

import java.time.LocalDate;
import java.util.List;

public interface TaskService {
    TaskResponse createTask(TaskRequest taskRequest);
    TaskDetailsResponse getTaskById(Long taskId);
    TaskStatusUpdateResponse updateTaskStatus(Long taskId, TaskStatusUpdateRequest request);
    String deleteTaskById(Long taskId);
    TaskListResponse filterTasks(String status, Long employeeId, String deadline, String sortBy, String sortOrder);
    TaskReportResponse getEmployeeTaskReport(Long employeeId);
    TaskReportResponse getTaskReportByStatus(String status);
    List<Task> getTasksByDeadline(String deadline);
    TaskReportResponse getOverallTaskMetrics();

    String setOverdueTasks();

    float calculateTaskCompletionPercentage(Long employeeId);


    String getNotification(Long id);

    List<Task> getTasksForEmployeeInDateRange(Long employeeId, LocalDate startDate, LocalDate endDate);
}
