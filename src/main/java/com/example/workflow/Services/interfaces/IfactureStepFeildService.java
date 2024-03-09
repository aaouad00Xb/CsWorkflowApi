package com.example.workflow.Services.interfaces;

import com.example.workflow.Dto.FactureStepFieldRquest;
import com.example.workflow.Entities.FactureStepField;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IfactureStepFeildService {
    List<FactureStepField> getAllFactureStepFields();

    ResponseEntity<?> getFactureStepFieldById(Long id);

    FactureStepField createFactureStepField(FactureStepField factureStepField);

    ResponseEntity<?>  updateFactureStepField(Long id, FactureStepField updatedFactureStepField);

    List<FactureStepField> findByFactureAndStep(Long factureID, Long stepID);

    ResponseEntity<?> updateStepFields(List<FactureStepFieldRquest> facturesStepField);

    ResponseEntity<?> deleteFactureStepField(Long id);
}
