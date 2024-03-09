package com.example.workflow.Dto;

import com.example.workflow.Entities.ContratSousTraitance;
import com.example.workflow.Entities.Document;
import com.example.workflow.Entities.FactureStepEntryDate;
import com.example.workflow.Entities.StepFacture;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.Date;
import java.util.List;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data

public class FactureResponseDto {

    private Long factureID;
    private String factureNumber;
    private Date factureDate;
    private double totalAmount;
    @JsonIgnoreProperties({"createdAt","currentStep","createdBy"})
    private ContratSoutraitanceResponseDto contratSousTraitance;
    @JsonIgnoreProperties({"entryDates","stepFields"})
    private StepFacture currentStep;  // The current step for the facture
    private boolean valid;
    private List<Document> uploadedFiles;
    @JsonIgnoreProperties({"facture","step","type"})
    private FactureStepEntryDate factureStepEntryDate;

}
