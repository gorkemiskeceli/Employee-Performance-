package com.archisacademy.employee.dto.response;

import com.archisacademy.employee.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskStatusUpdateResponse {

    private Long taskId;
    private String taskName;
    private Status oldStatus;
    private Status newStatus;
    private String message;
}
