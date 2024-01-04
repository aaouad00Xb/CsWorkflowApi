package com.example.workflow.Dto;


import com.example.workflow.Entities.Soustraitant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SousTraitanceDto {

    private Long id;
    private String libelle;
    private String mo;
    private String nStraitance;
    private Double montant;
    private Date dateSoutraitence;
    private Soustraitant titulaire;



}
