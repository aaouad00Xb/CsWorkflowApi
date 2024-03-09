package com.example.workflow.Dto;

import com.example.workflow.Entities.*;
import com.example.workflow.Entities.User.User;
import com.example.workflow.Enums.TypeContrat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ContratSoutraitanceResponseDto {

    private Long contratID;
    private String contratNumber;
    private Date signatureDate;
    private double totalAmount;

    @Enumerated(EnumType.STRING)
    private TypeContrat typeContrat;
    private Date dateDebut_contrat;
    private Date dateFin_contrat;
    private boolean valid;
    private List<Document> uploadedFiles;


    private User projectManager;

    @JsonIgnoreProperties({"contratSousTraitances","division"})
    private Business business;

    private Soustraitant soustraitant;

    @JsonIgnoreProperties({"pole","contratSousTraitances"})
    private Division division;

    @JsonIgnoreProperties({"entryDates","stepFields"})
    private Step currentStep;  // The current step for the facture


    @JsonIgnoreProperties({"facture","step","type"})
    private ContratStepEntryDate contratStepEntryDate;


    private LocalDateTime createdAt;

    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;


}
