package com.example.workflow.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FactureStepFieldRquest {

    private Long stepFieldID;
    private boolean isValid;  // Indicates whether the field is valid or not


}
