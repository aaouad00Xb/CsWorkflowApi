package com.example.workflow.Repositories;

import com.example.workflow.Entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FactureStepFieldRepository extends JpaRepository<FactureStepField, Long> {
    // Define custom query methods if needed
    List<FactureStepField> findByFactureAndStep(Facture facture, StepFacture step);

    boolean existsByFactureAndStep(Facture contrat, StepFacture step);

    @Query("SELECT sf FROM StepFieldF_r  sf WHERE sf.step = :step")
    List<StepFieldF_r> findByStep(StepFacture step);


}