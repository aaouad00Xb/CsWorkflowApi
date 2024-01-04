package com.example.workflow.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Pole_creation_DTO {


    @NotEmpty(message = "poleName can not be a null or empty")
    @Schema(
            description = "poleName ", example = "pole SI"
    )
    private String poleName;
//
    @NotEmpty(message = "abreviation can not be a null or empty")
    @Schema(
            description = "abreviation ", example = "pole SI"
    )
    private String abreviation;



}
