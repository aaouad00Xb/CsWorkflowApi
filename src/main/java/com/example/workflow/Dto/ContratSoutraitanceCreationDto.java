package com.example.workflow.Dto;

import com.example.workflow.Entities.*;
import com.example.workflow.Enums.TypeContrat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ContratSoutraitanceCreationDto {

    @NotEmpty(message = "contratNumber can not be a null or empty")
    @Schema(
            description = "contratNumber ", example = "div ..............."
    )
    private String contratNumber;
    @NotNull(message = "signatureDate can not be a null or empty")
    @Schema(
            description = "signatureDate ", example = "div ..............."
    )
    private Date signatureDate;
    @NotNull(message = "totalAmount can not be a null or empty")
    @Schema(
            description = "totalAmount ", example = "div ..............."
    )
    private double totalAmount;
    @NotNull(message = "typeContrat can not be a null or empty")
    @Schema(
            description = "typeContrat ", example = "div ..............."
    )
    @Enumerated(EnumType.STRING)
    private TypeContrat typeContrat;

    @NotNull(message = "dateDebut_contrat can not be a null or empty")
    @Schema(description = "dateDebut_contrat ", example = "div ...............")
    private Date dateDebut_contrat;

    @NotNull(message = "dateFin_contrat can not be a null or empty")
    @Schema(description = "dateFin_contrat ", example = "div ...............")
    private Date dateFin_contrat;

    private List<Document> uploadedFiles;

    @NotNull(message = "businessID can not be a null or empty")
    @Schema(description = "businessID ", example = "div ...............")
    private Long businessID;

    @NotNull(message = "soustraitantID can not be a null or empty")
    @Schema(description = "soustraitantID ", example = "div ...............")
    private Long soustraitantID;


    @NotNull(message = "divisionID can not be a null or empty")
    @Schema(
            description = "divisionID ", example = "div ..............."
    )
    private Long divisionID;



}
