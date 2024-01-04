package com.example.workflow.Services.impl;

import com.example.workflow.Dto.FactureStepFieldRquest;
import com.example.workflow.Entities.ContratStepField;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IcontratStepFieldService {
    List<ContratStepField> getAllFactureStepFields();

    ResponseEntity<?> getFactureStepFieldById(Long id);

    ContratStepField createFactureStepField(ContratStepField factureStepField);

    ResponseEntity<?>  updateFactureStepField(Long id, ContratStepField updatedFactureStepField);

    List<ContratStepField> findByContratAndStep(Long factureID, Long stepID);

    ResponseEntity<?> updateStepFields(List<FactureStepFieldRquest> facturesStepField);

    ResponseEntity<?> deleteFactureStepField(Long id);
}
