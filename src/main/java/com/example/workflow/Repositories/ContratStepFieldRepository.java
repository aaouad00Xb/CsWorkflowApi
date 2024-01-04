package com.example.workflow.Repositories;

import com.example.workflow.Entities.ContratSousTraitance;
import com.example.workflow.Entities.ContratStepField;
import com.example.workflow.Entities.Step;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContratStepFieldRepository extends JpaRepository<ContratStepField, Long> {
    // Define custom query methods if needed
    List<ContratStepField> findByContratAndStep(ContratSousTraitance facture, Step step);

    boolean existsByContratAndStep(ContratSousTraitance contrat, Step step);


}