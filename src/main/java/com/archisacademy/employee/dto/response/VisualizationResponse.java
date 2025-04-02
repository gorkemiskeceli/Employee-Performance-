package com.archisacademy.employee.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VisualizationResponse {
    private String employeeName;
    private String goalCompletionGraph;
    private String taskCompletionGraph;
}
