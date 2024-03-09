package com.example.workflow.Services.interfaces;

import com.example.workflow.Dto.ContratSoutraitanceCreationDto;
import com.example.workflow.Dto.FactureResponseDto;
import com.example.workflow.Dto.FactureCreationDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IFactureService {

    ResponseEntity<?> getAllFactures();

    ResponseEntity<?> getFactureById(Long id);


    ResponseEntity<?>  createFacture(FactureCreationDto contrat);


    ResponseEntity<?> updateFacture(Long id, FactureCreationDto updatedFacture);

    ResponseEntity<?> deleteFacture(Long id);


    ResponseEntity<?> getFactureBySousTraitance(Long soustranceID);


    List<FactureResponseDto> getFacturesByUserRoleAndDivision();

        List<FactureResponseDto> getFacturesDivision();

    List<FactureResponseDto> getFacturesByUserRoleAndDivisionAilleur();

    List<FactureResponseDto> getFacturesByUserRoleAndDivisionValid();

    int getTotalActiveFactures();

    long countFacturesInStepOne();

    long countOngoingFactures();

    long countAll();
    
}
