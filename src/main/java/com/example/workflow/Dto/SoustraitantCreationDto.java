package com.example.workflow.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SoustraitantCreationDto {

    @NotNull(message = "name can not be a null or empty")
    @Schema(
            description = "name ", example = "pole SI"
    )
    private String name;

//    @NotNull(message = "rc can not be a null or empty")
    @Schema(
            description = "rc ", example = "pole SI"
    )
    private String rc;

    @Schema(
            description = "cin ", example = "AA44566"
    )
    private String cin;

    @NotNull(message = "address can not be a null or empty")
    @Schema(description = "address ", example = "pole SI")
        private String address;


    @NotEmpty(message = "tele can not be a null or empty")
    @Pattern(regexp = "^[0-9]{10}$", message = "tele should contain exactly 10 digits")
    @Schema(description = "tele", example = "1234567890")
    private String tele;

    @Email(message = "invalid email")
    @NotNull(message = "email can not be a null or empty")
    @Schema(description = "email ", example = "pole SI")
    private String email;


    @NotNull(message = "domainActivity can not be a null or empty")
    @Schema(description = "domainActivity ", example = "pole SI")
    private String domainActivity;


    @NotNull(message = "dateAncient can not be a null or empty")
    @Schema(
            description = "dateAncient ", example = "pole SI"
    )
    private Date dateAncient;


    @NotNull(message = "patente can not be a null or empty")
    @Schema(description = "patente ", example = "pole SI")
    private boolean patente;


    @NotNull(message = "commentaire can not be a null or empty")
    @Schema(description = "commentaire ", example = "pole SI")
    private String commentaire;

}
