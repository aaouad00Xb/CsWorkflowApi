package com.example.workflow.Services.interfaces;

import com.example.workflow.Dto.*;
import com.example.workflow.Entities.ContratSousTraitance;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IcontratService {
    ResponseEntity<?> getAllContrats();

    ResponseEntity<?> getContratById(Long id);

//    ResponseEntity<?> createFacture(FactureCreationDto facture);

    ResponseEntity<?>  createContrat(ContratSoutraitanceCreationDto contrat);

//    ResponseEntity<?> updateContrat(Long id, ContratSousTraitance updatedFacture);

    ResponseEntity<?> updateContrat(Long id, ContratSoutraitanceCreationDto updatedFacture);

    ResponseEntity<?> deleteContrat(Long id);

    ResponseEntity<?> getContratByBusinessAndSousTraitant(Long businessId, Long soustraitantId);

    ResponseEntity<?> getContratBySousTraitance(Long soustranceID);


    List<ContratSoutraitanceResponseDto> getContractsByUserRoleAndDivision();

    List<ContratSoutraitanceResponseDto> getContracts();
    List<ContratSoutraitanceResponseDto> findAll();

    List<ContratSoutraitanceResponseDto> getContractsByUserRoleAndDivisionAilleur();

    List<ContratSoutraitanceResponseDto> getContractsByUserRoleAndDivisionValid();

    int getTotalActiveContracts();

    long countContractsInStepOne();

    long countOngoingContracts();

    long countAll();

    double calculateTotalContractValue();

    long calculateContractTraceabilityDuration(ContratSousTraitance contratSousTraitance);

    double calculateAverageContractTraceabilityDuration();

    long findLongestContractDuration();

    long findShortestContractDuration();

    DashboardBarDto populateLists();

    DashboardPoleBarDto populatePoleList();

    List<EvolutionChartDto> populateEvolution();
}
