package com.example.workflow.Repositories;

import com.example.workflow.Entities.Facture;
import com.example.workflow.Entities.FactureStepEntryDate;
import com.example.workflow.Entities.StepFacture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FactureEntryDateRepository extends JpaRepository<FactureStepEntryDate, Long> {


        Optional<FactureStepEntryDate> findByFactureAndAndStep(Facture contratSousTraitance, StepFacture Step);
        List<FactureStepEntryDate> findByFactureAndStepOrderByCreatedAtDesc(
                Facture facture, StepFacture step);


        List<FactureStepEntryDate> findByFactureOrderByCreatedAtDesc(
                Facture facture);


@Query("SELECT new com.example.workflow.Entities.FactureStepEntryDate(c.id, c.entryDate) FROM FactureStepEntryDate c WHERE c.facture = :contrat AND c.facture.valid = true ORDER BY c.entryDate ASC")
    List<FactureStepEntryDate> findByFactureValidOrderByEntryDateAsc(@Param("contrat") Facture facture);


@Query("SELECT new com.example.workflow.Entities.FactureStepEntryDate(c.id, c.entryDate) FROM FactureStepEntryDate c WHERE c.facture = :contrat AND c.facture.valid = false ORDER BY c.entryDate ASC")
    List<FactureStepEntryDate> findByFactureEncoursOrderByEntryDateAsc(@Param("contrat") FactureStepEntryDate contrat);



        }
