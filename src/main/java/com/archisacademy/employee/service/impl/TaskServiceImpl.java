package com.archisacademy.employee.service.impl;

import com.archisacademy.employee.dto.request.TaskRequest;
import com.archisacademy.employee.dto.request.TaskStatusUpdateRequest;
import com.archisacademy.employee.dto.response.*;
import com.archisacademy.employee.entity.Employee;
import com.archisacademy.employee.entity.Task;
import com.archisacademy.employee.enums.Status;
import com.archisacademy.employee.repository.EmployeeRepository;
import com.archisacademy.employee.repository.TaskRepository;
import com.archisacademy.employee.service.TaskService;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;
    private final Map<Long, String> notificationCache = new ConcurrentHashMap<>();

    public TaskServiceImpl(TaskRepository taskRepository, EmployeeRepository employeeRepository, ModelMapper modelMapper) {
        this.taskRepository = taskRepository;
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public TaskResponse createTask(TaskRequest taskRequest) {
        if (taskRequest == null) {
            throw new IllegalArgumentException("Task request cannot be null.");
        }
        Task task = modelMapper.map(taskRequest, Task.class);
        task.setTaskId(null);

        if (taskRequest.getEmployeeId() != null) {
            Employee employee = employeeRepository.findById(taskRequest.getEmployeeId())
                    .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + taskRequest.getEmployeeId()));

            task.setEmployee(employee);
            employee.getTasks().add(task);
        }

        Task savedTask = taskRepository.save(task);

        return new TaskResponse("Task created successfully with ID: " + savedTask.getTaskId());
    }
    @Override
    @Transactional(readOnly = true)
    public TaskDetailsResponse getTaskById(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + taskId));

        return modelMapper.map(task, TaskDetailsResponse.class);
    }


    @Override
    @Transactional
    public TaskStatusUpdateResponse updateTaskStatus(Long taskId, TaskStatusUpdateRequest request) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + taskId));

        if (request.getNewStatus() == null) {
            throw new IllegalArgumentException("New status cannot be null.");
        }

        Status oldStatus = task.getStatus();

        modelMapper.map(request, task);

        Task updatedTask = taskRepository.save(task);

        return new TaskStatusUpdateResponse(
                updatedTask.getTaskId(),
                updatedTask.getTaskName(),
                oldStatus,
                updatedTask.getStatus(),
                "Task status successfully updated."
        );
    }
    @Override
    @Transactional
    public String deleteTaskById(Long taskId) {

        if (!taskRepository.existsById(taskId)) {
            throw new RuntimeException("Task not found with ID: " + taskId);
        }

        try {
            taskRepository.deleteById(taskId);
            taskRepository.flush();

            return "Task with ID " + taskId + " successfully deleted.";
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete Task with ID: " + taskId + ". " + e.getMessage());
        }
    }


    @Override
    @Transactional(readOnly = true)
    public TaskListResponse filterTasks(String statusStr, Long employeeId, String deadlineStr, String sortBy, String sortOrder) {


        List<Task> tasks = taskRepository.findAll();


        if (statusStr != null && !statusStr.trim().isEmpty()) {
            try {
                Status status = Status.valueOf(statusStr.trim().toUpperCase());
                tasks = tasks.stream()
                        .filter(task -> task.getStatus().equals(status))
                        .collect(Collectors.toList());
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid status value: " + statusStr);
            }
        }


        if (employeeId != null) {
            tasks = tasks.stream()
                    .filter(task -> task.getEmployee() != null && task.getEmployee().getId().equals(employeeId))
                    .collect(Collectors.toList());
        }

        if (deadlineStr != null && !deadlineStr.trim().isEmpty()) {
            try {
                Date deadline = new SimpleDateFormat("yyyy-MM-dd").parse(deadlineStr.trim());
                Calendar cal = Calendar.getInstance();
                cal.setTime(deadline);
                cal.add(Calendar.DAY_OF_MONTH, 7);
                Date sevenDaysLater = cal.getTime();

                tasks = tasks.stream()
                        .filter(task -> !task.getDeadline().before(deadline) && !task.getDeadline().after(sevenDaysLater))
                        .collect(Collectors.toList());
            } catch (ParseException e) {
                throw new RuntimeException("Invalid deadline format. Please use yyyy-MM-dd.");
            }
        }


        if ("deadline".equalsIgnoreCase(sortBy)) {
            if ("desc".equalsIgnoreCase(sortOrder)) {
                tasks.sort(Comparator.comparing(Task::getDeadline, Comparator.nullsLast(Comparator.reverseOrder())));
            } else {
                tasks.sort(Comparator.comparing(Task::getDeadline, Comparator.nullsLast(Comparator.naturalOrder())));
            }
        } else if ("status".equalsIgnoreCase(sortBy)) {
            if ("desc".equalsIgnoreCase(sortOrder)) {
                tasks.sort(Comparator.comparing(task -> task.getStatus().name(), Comparator.reverseOrder()));
            } else {
                tasks.sort(Comparator.comparing(task -> task.getStatus().name()));
            }
        }


        List<TaskDetailsResponse> taskResponses = tasks.stream()
                .map(task -> modelMapper.map(task, TaskDetailsResponse.class))
                .collect(Collectors.toList());

        return new TaskListResponse(taskResponses);
    }

    @Override
    @Transactional(readOnly = true)
    public TaskReportResponse getEmployeeTaskReport(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + employeeId));

        List<Task> tasks = taskRepository.findAll().stream()
                .filter(task -> task.getEmployee() != null && task.getEmployee().getId().equals(employeeId))
                .collect(Collectors.toList());

        int totalTasks = tasks.size();
        int completedTasks = (int) tasks.stream().filter(task -> task.getStatus() == Status.COMPLETED).count();
        double completionRate = totalTasks == 0 ? 0 : ((double) completedTasks / totalTasks) * 100;

        return new TaskReportResponse(
                totalTasks,
                completedTasks,
                completionRate,
                0.0,
                0,
                totalTasks == 0 ? "No tasks assigned to this employee." :
                        "Employee " + employee.getId() + " has completed " + String.format("%.2f", completionRate) + "% of their tasks."
        );
    }


    @Override
    @Transactional(readOnly = true)
    public TaskReportResponse getTaskReportByStatus(String statusStr) {
        Status status;
        try {
            status = Status.valueOf(statusStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid status value: " + statusStr);
        }

        int totalTasks = (int) taskRepository.findAll().stream()
                .filter(task -> task.getStatus() == status)
                .count();

        return new TaskReportResponse(totalTasks, 0, 0, 0, 0, "Total tasks with status " + status + ": " + totalTasks);
    }


    @Override
    @Transactional(readOnly = true)
    public List<Task> getTasksByDeadline(String deadlineStr) {
        try {
            Date deadline = new SimpleDateFormat("yyyy-MM-dd").parse(deadlineStr);
            return taskRepository.findAll().stream()
                    .filter(task -> task.getDeadline().equals(deadline))
                    .collect(Collectors.toList());
        } catch (ParseException e) {
            throw new RuntimeException("Invalid date format. Please use yyyy-MM-dd.");
        }
    }


    @Override
    @Transactional(readOnly = true)
    public TaskReportResponse getOverallTaskMetrics() {
        List<Task> tasks = taskRepository.findAll();

        int totalTasks = tasks.size();
        int completedTasks = (int) tasks.stream().filter(task -> task.getStatus() == Status.COMPLETED).count();
        int overdueTasks = (int) tasks.stream().filter(task -> task.getDeadline().before(new Date())).count();
        double completionRate = totalTasks == 0 ? 0 : ((double) completedTasks / totalTasks) * 100;

        return new TaskReportResponse(totalTasks, completedTasks, completionRate, 0.0, overdueTasks,
                "Overall Task Metrics");
    }

    @Override
    @Scheduled(fixedRate = 3600000)
    public String setOverdueTasks() {

        Date currentDate = new Date();

        List<Task> tasks = taskRepository.findByStatusNotAndDeadlineBefore(Status.COMPLETED, currentDate);

        for (Task task : tasks) {
            task.setStatus(Status.OVERDUE);
        }

        taskRepository.saveAll(tasks);

        return "Overdue tasks updated";
    }

    @Override
    public float calculateTaskCompletionPercentage(Long employeeId) {

        List<Task> tasks = taskRepository.findByEmployeeId(employeeId);
        if (tasks.isEmpty()) return 100f;

        List<Task> completedTasks = taskRepository.findByStatusAndEmployeeId(Status.COMPLETED, employeeId);

        long completedCount = completedTasks.size();
        long totalCount = tasks.size();

        return (float) (completedCount * 100) / totalCount;
    }


    @Scheduled(fixedRate = 60000)
    public void checkUpcomingTasks(){
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date threshold = calendar.getTime();

        List<Task> upcomingTasks = taskRepository.findByDeadlineBetween(now, threshold);
        for (Task task : upcomingTasks){
            if (task.getEmployee() != null){
                notificationCache.put(task.getEmployee().getId(), "görev süresi yaklaşıyor:" + task.getTaskName());
            }
        }
    }

    @Override
    public String getNotification(Long id){
        return notificationCache.getOrDefault(id, "Yeni bildiriminiz yok");
    }

    @Override
    public List<Task> getTasksForEmployeeInDateRange(Long employeeId, LocalDate startDate, LocalDate endDate) {
        return taskRepository.findByEmployeeIdAndDeadlineBetween(employeeId, startDate, endDate);
    }
}

