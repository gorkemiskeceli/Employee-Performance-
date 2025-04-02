package com.archisacademy.employee.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeUpdateRequest {

    @NotBlank(message = "First Name cannot be null!!!")
    private String firstName;

    @NotBlank(message = "Last name cannot be null!!!")
    private String lastName;

    @NotNull(message = "Department id cannot be null!!")
    private Long departmentId;

}