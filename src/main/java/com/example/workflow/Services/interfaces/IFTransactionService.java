package com.example.workflow.Services.interfaces;

import com.example.workflow.Entities.*;
import com.example.workflow.Entities.Facture;
import com.example.workflow.Enums.TransactionType;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IFTransactionService {

    Facture initFactureToStep(Facture facture);

    ResponseEntity<?> transitionFactureToStep(Long factureId, Long newStepID);
    ResponseEntity<?> transitionFactureToStep(Long factureId, Long newStepID,String commentaire);



    ResponseEntity<?> validateFacture(Long factureId, String commentaire);

    void recordFactureEntryDateForStep(Facture facture, StepFacture newStep, TransactionType type);

    void recordFactureEntryDateForStep(Facture facture, StepFacture newStep, String Commentaire, TransactionType type);

    boolean areAllStepFieldsValid(List<FactureStepField> factureStepFields);

    void createContratStepFieldsForNewStep(Facture facture, StepFacture newStep);

    ResponseEntity<?> getEntryDateByFactureAndStep(Long FactureID, Long stepID);

    ResponseEntity<?> getEntryDateByFacture(Long FactureID);
}
