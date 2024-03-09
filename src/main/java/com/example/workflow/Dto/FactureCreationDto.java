package com.example.workflow.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FactureCreationDto {

    @NotNull(message = "factureNumber can not be a null or empty")
    @Schema(
            description = "factureNumber ", example = "div ..............."
    )
    private String factureNumber;
    @NotNull(message = "factureDate can not be a null or empty")
    @Schema(
            description = "factureDate ", example = "div ..............."
    )
    private Date factureDate;
    @NotNull(message = "totalAmount can not be a null or empty")
    @Schema(
            description = "totalAmount ", example = "div ..............."
    )
    private double totalAmount;
    @NotNull(message = "contratSousTraitance can not be a null or empty")
    @Schema(
            description = "contratSousTraitance ", example = "div ..............."
    )
    private Long contratSousTraitanceID;


}
