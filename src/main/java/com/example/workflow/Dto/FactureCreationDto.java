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
public class FactureCreationDto {

    private String factureNumber;
    private Date factureDate;
    private double totalAmount;

    private Long business;
    private Long sousTraitant;

//    private Long projectManager;  // The current step for the facture

}
