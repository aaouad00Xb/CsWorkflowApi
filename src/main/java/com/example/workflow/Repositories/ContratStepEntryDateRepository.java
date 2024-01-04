package com.example.workflow.Repositories;

import com.example.workflow.Entities.ContratSousTraitance;
import com.example.workflow.Entities.ContratStepEntryDate;
import com.example.workflow.Entities.Step;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ContratStepEntryDateRepository extends JpaRepository<ContratStepEntryDate, Long> {


    Optional<ContratStepEntryDate> findByFactureAndAndStep(ContratSousTraitance contratSousTraitance, Step Step);
    List<ContratStepEntryDate> findByFactureAndStepOrderByCreatedAtDesc(
            ContratSousTraitance contratSousTraitance, Step step);


    List<ContratStepEntryDate> findByFactureOrderByCreatedAtDesc(
            ContratSousTraitance contratSousTraitance);


    @Query("SELECT new com.example.workflow.Entities.ContratStepEntryDate(c.id, c.entryDate) FROM ContratStepEntryDate c WHERE c.facture = :contrat AND c.facture.valid = true ORDER BY c.entryDate ASC")
    List<ContratStepEntryDate> findByFactureValidOrderByEntryDateAsc(@Param("contrat") ContratSousTraitance contrat);


    @Query("SELECT new com.example.workflow.Entities.ContratStepEntryDate(c.id, c.entryDate) FROM ContratStepEntryDate c WHERE c.facture = :contrat AND c.facture.valid = false ORDER BY c.entryDate ASC")
    List<ContratStepEntryDate> findByFactureEncoursOrderByEntryDateAsc(@Param("contrat") ContratSousTraitance contrat);



}
