package com.example.workflow.Dto;

import com.example.workflow.Entities.Division;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BuisnessResponseDto {

    private Long businessID;
    private String objet;
    private String codeAffaire;
    private String businessAddress;
    private String contactPerson;
    private String contactEmail;
    private String contactPhone;
    private Double BudgetTotal;
    private Date dateBuisnes;  // Date when the business record was created

    @JsonIgnoreProperties({"pole","contratSousTraitances"})
    private Division division;

    private LocalDateTime createdAt;

    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
}
