package com.example.workflow.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EntryDateResponseDto {

    private LocalDateTime entryDate;
    private LocalDateTime createdAt;

    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
}
