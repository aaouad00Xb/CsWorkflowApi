package com.example.workflow.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SousTraitenceCreationDto {
    private String libelle;
    private Double montant;
    private String mo;
    private String nStraitance;
    private Date dateSoutraitence;
    private Long titulaire;

    private Long Buisness;

}
