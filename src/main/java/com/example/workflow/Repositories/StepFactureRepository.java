package com.example.workflow.Repositories;

import com.example.workflow.Entities.Step;
import com.example.workflow.Entities.StepFacture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository

public interface StepFactureRepository extends JpaRepository<StepFacture, Long> {
    @Query("SELECT new com.example.workflow.Entities.StepFacture(c.stepID, c.stepName) FROM Step c")
    List<StepFacture> findSteps();
}
