package com.archisacademy.employee.dto.request;

import com.archisacademy.employee.enums.Priority;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequest {

    @NotBlank(message = "Task name is required.")
    private String taskName;

    private String description;

    private Long employeeId;

    @NotNull(message = "Deadline is required.")
    @Future(message = "Deadline must be a future date.")
    private Date deadline;

    @NotNull(message = "Priority is required.")
    private Priority priority;
}
