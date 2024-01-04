package com.example.workflow.Services.impl;

import com.example.workflow.Entities.ContratSousTraitance;
import com.example.workflow.Entities.ContratStepField;
import com.example.workflow.Entities.Step;
import com.example.workflow.Enums.TransactionType;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ItransactionService {
    ContratSousTraitance initContratToStep(ContratSousTraitance facture);

    ResponseEntity<?> transitionContratToStep(Long factureId, Long newStepID);
    ResponseEntity<?> transitionContratToStep(Long factureId, Long newStepID,String commentaire);
    ResponseEntity<?> validateContrat(Long factureId,String commentaire);

    void recordContratEntryDateForStep(ContratSousTraitance facture, Step newStep, TransactionType type);

    void recordContratEntryDateForStep(ContratSousTraitance facture, Step newStep, String Commentaire, TransactionType type);

    boolean areAllStepFieldsValid(List<ContratStepField> factureStepFields);

    void createContratStepFieldsForNewStep(ContratSousTraitance facture, Step newStep);

    ResponseEntity<?> getEntryDateByContratAndStep(Long contratID, Long stepID);

    ResponseEntity<?> getEntryDateByContrat(Long contratID);
}
