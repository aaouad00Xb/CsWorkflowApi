package com.example.workflow.Dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DivisionDtoResponse {

    private LocalDateTime createdAt;

    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;


    private Long divisionId;
    private String divisionName;
    private String abreviation;


}
