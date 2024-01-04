package com.example.workflow.Dto;

import com.example.workflow.Entities.Pole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DivisioncreationDto {

    @NotEmpty(message = "divisionName can not be a null or empty")
    @Schema(
            description = "divisionName ", example = "div ..............."
    )
    private String divisionName;

    @NotEmpty(message = "abreviation can not be a null or empty")
    @Schema(
            description = "abreviation ", example = "div ..............."
    )
    private String abreviation;

    @NotNull(message = "ple id can not be a null or empty")
    @Schema(
            description = "pole ", example = "div ..............."
    )
    private Long pole;

}
