package com.example.workflow.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
public class BuisnessCreationDto {
    @NotNull(message = "businessName id can not be a null or empty")
    @Schema(
            description = "businessName ", example = "div ..............."
    )
    private String objet;
    @NotNull(message = "mo can not be a null or empty")
    @Schema(
            description = "mo ", example = "div ..............."
    )
    private String mo;
    //    private String businessAddress;


    @NotNull(message = "codeAffaire id can not be a null or empty")
    @Schema(
            description = "codeAffaire ", example = "div ..............."
    )
    private String codeAffaire;


//    @NotNull(message = "contactPerson id can not be a null or empty")
//    @Schema(
//            description = "contactPerson ", example = "div ..............."
//    )
//    private String contactPerson;
//    @NotNull(message = "ple id can not be a null or empty")
//    @Schema(
//            description = "contactEmail ", example = "div ..............."
//    )
//    @Email(message = "Respect email format")
//    private String contactEmail;
//    @NotNull(message = "contactPhone id can not be a null or empty")
//    @Schema(
//            description = "contactPhone ", example = "div ..............."
//    )
//    private String contactPhone;
    @NotNull(message = "BudgetTotal id can not be a null or empty")
    @Schema(
            description = "BudgetTotal ", example = "div ..............."
    )
    private Double BudgetTotal;


    @NotNull(message = "dateBuisnes id can not be a null or empty")
    @Schema(
            description = "dateBuisnes ", example = "div ..............."
    )
    private Date dateBuisnes;  // Date when the business record was created

    @NotNull(message = "divisionID id can not be a null or empty")
    @Schema(
            description = "divisionID ", example = "div ..............."
    )
    private Long divisionID;  // Many businesses are associated with one pole


}
