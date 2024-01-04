package com.example.workflow.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class StepDTO {
    private Long stepID;
    private String stepName;
    private String stepDescription;
    private Long nextStepID;  // The next step
    private Long previousStepID;  // The previous step
    private List<Long> stepFieldsIds;
}
