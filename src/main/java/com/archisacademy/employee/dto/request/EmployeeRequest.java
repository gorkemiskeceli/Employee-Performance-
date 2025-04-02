package com.archisacademy.employee.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRequest {

    @NotBlank(message = "First name cannot be null!!!")
    private String firstName;

    @NotBlank(message = "Last name cannot be null!!!")
    private String lastName;

    @NotBlank(message = "E-mail cannot be null!!!")
    private String email;

    @NotBlank(message = "Phone number cannot be null!!!")
    private String phoneNumber;

    private Long departmentId;

    @NotBlank(message = "Position cannot be null!!!")
    private String position;

    @NotBlank(message = "Status should be entered!!!")
    private String status;


}
