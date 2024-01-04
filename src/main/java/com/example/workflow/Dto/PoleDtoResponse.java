package com.example.workflow.Dto;

import com.example.workflow.Entities.Division;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PoleDtoResponse {

    private LocalDateTime createdAt;

    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
    private Long poleID;
    private String poleName;
    private String abreviation;
@JsonIgnoreProperties({"pole","contratSousTraitances"})
    private List<Division> divisions;

}
