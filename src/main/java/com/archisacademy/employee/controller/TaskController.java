package com.archisacademy.employee.controller;

import com.archisacademy.employee.dto.request.TaskRequest;
import com.archisacademy.employee.dto.request.TaskStatusUpdateRequest;
import com.archisacademy.employee.dto.response.*;
import com.archisacademy.employee.entity.Task;
import com.archisacademy.employee.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/create")
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskRequest taskRequest) {
        TaskResponse response = taskService.createTask(taskRequest);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{id}")
    public ResponseEntity<TaskDetailsResponse> getTaskById(@PathVariable Long id) {
        TaskDetailsResponse response = taskService.getTaskById(id);
        return ResponseEntity.ok(response);
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<TaskStatusUpdateResponse> updateTaskStatus(
            @PathVariable Long id,
            @Valid @RequestBody TaskStatusUpdateRequest request) {
        TaskStatusUpdateResponse response = taskService.updateTaskStatus(id, request);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTaskById(@PathVariable Long id) {
        String responseMessage = taskService.deleteTaskById(id);
        return ResponseEntity.ok(responseMessage);
    }

    @GetMapping("/filter")
    public ResponseEntity<TaskListResponse> filterTasks(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long employeeId,
            @RequestParam(required = false) String deadline,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder) {

        TaskListResponse response = taskService.filterTasks(status, employeeId, deadline, sortBy, sortOrder);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/reports/overall")
    public ResponseEntity<TaskReportResponse> getOverallTaskMetrics() {
        return ResponseEntity.ok(taskService.getOverallTaskMetrics());
    }

    @GetMapping("/reports/employee/{employeeId}")
    public ResponseEntity<TaskReportResponse> getEmployeeTaskReport(@PathVariable Long employeeId) {
        return ResponseEntity.ok(taskService.getEmployeeTaskReport(employeeId));
    }

    @GetMapping("/reports/status/{status}")
    public ResponseEntity<TaskReportResponse> getTaskReportByStatus(@PathVariable String status) {
        return ResponseEntity.ok(taskService.getTaskReportByStatus(status));
    }

    @GetMapping("/reports/deadline/{deadline}")
    public ResponseEntity<List<Task>> getTasksByDeadline(@PathVariable String deadline) {
        return ResponseEntity.ok(taskService.getTasksByDeadline(deadline));
    }

    @GetMapping("/notifications/{id}")
    public ResponseEntity<String> getNotification(@PathVariable Long id){
        String notifaction = taskService.getNotification(id);
        return ResponseEntity.ok(notifaction);
    }
}
