package com.example.workflow.Repositories;

import com.example.workflow.Entities.Step;
import com.example.workflow.Entities.StepFacture;
import com.example.workflow.Entities.StepFieldF_r;
import com.example.workflow.Entities.StepField_r;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface StepFieldfRepository extends JpaRepository<StepFieldF_r, Long> {
    // Define custom query methods if needed
    @Query("SELECT sf FROM StepFieldF_r sf WHERE sf.step = :step")
    List<StepField_r> findByStep(StepFacture step);
}