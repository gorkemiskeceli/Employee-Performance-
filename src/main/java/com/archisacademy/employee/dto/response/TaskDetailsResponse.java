package com.archisacademy.employee.dto.response;

import com.archisacademy.employee.enums.Priority;
import com.archisacademy.employee.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskDetailsResponse {

    private Long taskId;
    private String taskName;
    private String description;
    private Date deadline;
    private Status status;
    private Priority priority;
    private Long employeeId;
}
