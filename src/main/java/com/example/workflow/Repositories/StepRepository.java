package com.example.workflow.Repositories;

import com.example.workflow.Entities.ContratSousTraitance;
import com.example.workflow.Entities.ContratStepEntryDate;
import com.example.workflow.Entities.Step;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StepRepository extends JpaRepository<Step, Long> {

    @Query("SELECT new com.example.workflow.Entities.Step(c.stepID, c.stepName) FROM Step c")
    List<Step> findSteps();



}
