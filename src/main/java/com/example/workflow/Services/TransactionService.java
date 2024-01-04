package com.example.workflow.Services;

import com.example.workflow.Entities.*;
import com.example.workflow.Enums.TransactionType;
import com.example.workflow.Repositories.*;
import com.example.workflow.Services.impl.ItransactionService;
import com.example.workflow.exception.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService implements ItransactionService {


    @Autowired
    private ContratStepFieldRepository factureStepFieldRepository;

    @Autowired
    private StepFieldRepository stepFieldRepository;


   @Autowired
    private NotificationService notificationService;


    @Autowired
    private ContratStepEntryDateRepository contratStepEntryDateRepository;


    @Autowired
    private StepRepository stepRepository;

    @Autowired
    private ContratSoustraitanceRepo contratSoustraitanceRepo;

    @Autowired
    private ContratStepFieldRepository contratStepFieldRepository;

    @Autowired
    private ObjectMapper objectMapper;





    @Override
    public ContratSousTraitance initContratToStep(ContratSousTraitance facture) {

         Step newStep = stepRepository.findById(1L).get();
         //todo
         createContratStepFieldsForNewStep(facture, newStep);

            // Update the currentStep of the Facture to the new Step
            facture.setCurrentStep(newStep);
        recordContratEntryDateForStep(facture, newStep, TransactionType.init);

        //notification
        this.notificationService.notifyInitContrat(facture);

           return contratSoustraitanceRepo.save(facture);

    }
    @Override
    public ResponseEntity<?> transitionContratToStep(Long factureId, Long newStepID) {

        Step newStep = stepRepository.findById(newStepID).get();
        ContratSousTraitance facture = contratSoustraitanceRepo.findById(factureId).get();

        // Retrieve relevant StepField entries for the current Step
        List<ContratStepField> factureStepFields = factureStepFieldRepository.findByContratAndStep(facture,facture.getCurrentStep() );


        // Check if all StepFields of the current step are valid
        boolean allStepFieldsValid = areAllStepFieldsValid(factureStepFields);

        if (allStepFieldsValid) {
            // Create FactureStepField entries for the new step
            createContratStepFieldsForNewStep(facture, newStep);

            // Record entry date for the new step
            recordContratEntryDateForStep(facture, newStep,TransactionType.prove);

            //notification
            this.notificationService.notifyProveContrat(facture);

            // Update the currentStep of the Facture to the new Step
            facture.setCurrentStep(newStep);
           ContratSousTraitance contratSousTraitance =  contratSoustraitanceRepo.save(facture);
           return ResponseEntity.status(200).body(contratSousTraitance);
        } else {
            throw new RuntimeException("something goes wrong");            // Handle the case where not all StepFie
            // You can add custom logic for handling this situation, such as error handling or notifications
        }
    }





    @Override
    public ResponseEntity<?> transitionContratToStep(Long factureId, Long newStepID,String Commentaire) {


        System.out.println("hello");
        Step newStep = stepRepository.findById(newStepID).get();
        ContratSousTraitance contrat = contratSoustraitanceRepo.findById(factureId).get();

        // Retrieve relevant StepField entries for the current Step
        List<ContratStepField> factureStepFields = factureStepFieldRepository.findByContratAndStep(contrat,contrat.getCurrentStep() );


        // Check if all StepFields of the current step are valid
        boolean allStepFieldsValid = areAllStepFieldsValid(factureStepFields);

        boolean forward = true;

        if(contrat.getCurrentStep()!=null){
            if (contrat.getCurrentStep().getStepID()>newStepID){
                forward = false;
            }
        }

        boolean passedBythisStepBefore = this.existsByContratAndStep(contrat,newStep);
        System.out.println("achraf");
        System.out.println(passedBythisStepBefore);


if(forward){
    if (allStepFieldsValid) {
        // Create FactureStepField entries for the new step


       if(!passedBythisStepBefore) createContratStepFieldsForNewStep(contrat, newStep);

        // Record entry date for the new step
        recordContratEntryDateForStep(contrat, newStep,Commentaire,TransactionType.prove);


        // Update the currentStep of the Facture to the new Step
        contrat.setCurrentStep(newStep);
        ContratSousTraitance contratSousTraitance =  contratSoustraitanceRepo.save(contrat);
        //notification
        this.notificationService.notifyProveContrat(contratSousTraitance);

        return ResponseEntity.status(200).body(contratSousTraitance);
    } else {
        throw new RuntimeException("merci de valider toutes les elements de l'etape précedente");            // Handle the case where not all StepFie
        // You can add custom logic for handling this situation, such as error handling or notifications
    }
}else {
    // Record entry date for the new step
    recordContratEntryDateForStep(contrat, newStep,Commentaire,TransactionType.disprove);
    // Update the currentStep of the Facture to the new Step
    contrat.setCurrentStep(newStep);
    ContratSousTraitance contratSousTraitance =  contratSoustraitanceRepo.save(contrat);
    this.notificationService.notifydisProveContrat(contratSousTraitance);

    return ResponseEntity.status(200).body(contratSousTraitance);


}

    }



    @Override
    public ResponseEntity<?> validateContrat(Long factureId,String Commentaire) {


        System.out.println("hello");
        Step newStep = stepRepository.findById(6L).get();
        ContratSousTraitance contrat = contratSoustraitanceRepo.findById(factureId).get();

        // Retrieve relevant StepField entries for the current Step
        List<ContratStepField> factureStepFields = factureStepFieldRepository.findByContratAndStep(contrat,contrat.getCurrentStep() );


        // Check if all StepFields of the current step are valid
        boolean allStepFieldsValid = areAllStepFieldsValid(factureStepFields);

            if (allStepFieldsValid) {

                // Record entry date for the new step
                recordContratEntryDateForStep(contrat, newStep,Commentaire,TransactionType.validation);

                this.notificationService.notifyValidContrat(contrat);
                contrat.setValid(true);
                ContratSousTraitance contratSousTraitance =  contratSoustraitanceRepo.save(contrat);

                return ResponseEntity.status(200).body(contratSousTraitance);
            } else {
                throw new RuntimeException("merci de valider toutes les elements de l'etape précedente");            // Handle the case where not all StepFie
                // You can add custom logic for handling this situation, such as error handling or notifications
            }

    }



    @Override
    public void recordContratEntryDateForStep(ContratSousTraitance facture, Step newStep,TransactionType type) {
        ContratStepEntryDate factureStepEntryDate = new ContratStepEntryDate();
        factureStepEntryDate.setFacture(facture);
        factureStepEntryDate.setStep(newStep);
        factureStepEntryDate.setType(type);

        factureStepEntryDate.setEntryDate(LocalDateTime.now()); // Set the entry date to the current timestamp or relevant date/time

        contratStepEntryDateRepository.save(factureStepEntryDate); // Save the entry date
    }


    public boolean existsByContratAndStep(ContratSousTraitance contratSousTraitance , Step step){
    return contratStepFieldRepository.existsByContratAndStep(contratSousTraitance,step);

    }


    @Override
    public void recordContratEntryDateForStep(ContratSousTraitance facture, Step newStep, String Commentaire,TransactionType type) {
        ContratStepEntryDate factureStepEntryDate = new ContratStepEntryDate();
        factureStepEntryDate.setFacture(facture);
        factureStepEntryDate.setStep(newStep);
        factureStepEntryDate.setCommentaire(Commentaire);
        factureStepEntryDate.setType(type);


        factureStepEntryDate.setEntryDate(LocalDateTime.now()); // Set the entry date to the current timestamp or relevant date/time

        contratStepEntryDateRepository.save(factureStepEntryDate); // Save the entry date
    }


    @Override
    public boolean areAllStepFieldsValid(List<ContratStepField> factureStepFields) {
        // Validate each StepField, return false if any are not valid
        for (ContratStepField stepField : factureStepFields) {
            if (!stepField.isValid()) {
                return false; // At least one StepField is not valid
            }
        }
        return true; // All StepFields are valid
    }



    @Override
    public void createContratStepFieldsForNewStep(ContratSousTraitance facture, Step newStep) {
        // Retrieve relevant StepField entries for the new Step
        List<StepField_r> newStepFields = stepFieldRepository.findByStep(newStep);

        for (StepField_r newStepField : newStepFields) {
            // Create corresponding FactureStepField entries for the new Step
            ContratStepField factureStepField = new ContratStepField();
            factureStepField.setFieldName(newStepField.getFieldName());
            factureStepField.setValid(false); // Assume new StepFields are valid by default

            // Associate FactureStepField with the Facture and the new Step
            factureStepField.setContrat(facture);
            factureStepField.setStep(newStep);

            // Save the FactureStepField entry
            factureStepFieldRepository.save(factureStepField);
        }
    }



    @Override
    public ResponseEntity<?> getEntryDateByContratAndStep(Long contratID, Long stepID) {
        // Retrieve relevant StepField entries for the new Step
        ContratSousTraitance existingcontrat =  contratSoustraitanceRepo.findById(contratID).orElseThrow(()->new ResourceNotFoundException("contrat ","contrat id",""+contratID));
        Step s =  stepRepository.findById(stepID).orElseThrow(()->new ResourceNotFoundException("step ","step id",""+stepID));

//        Optional<ContratStepEntryDate> contratStepEntryDate = contratStepEntryDateRepository.findByFactureAndAndStep(existingcontrat,s);
//        if (contratStepEntryDate.isPresent()){
//            EntryDateResponseDto response =   objectMapper.convertValue(contratStepEntryDate, EntryDateResponseDto.class);
//            return ResponseEntity.status(200).body(response);
//        }

        List<ContratStepEntryDate> entryList = contratStepEntryDateRepository
                .findByFactureAndStepOrderByCreatedAtDesc(existingcontrat, s);
        if (!entryList.isEmpty()) {
            ContratStepEntryDate latestEntry = entryList.get(0);
            return ResponseEntity.ok(latestEntry);
        }

        throw new RuntimeException("No entry date with the given step and cotnrat");
    }




    @Override
    public ResponseEntity<?> getEntryDateByContrat(Long contratID) {
        // Retrieve relevant StepField entries for the new Step
        ContratSousTraitance existingcontrat =  contratSoustraitanceRepo.findById(contratID).orElseThrow(()->new ResourceNotFoundException("contrat ","contrat id",""+contratID));


        List<ContratStepEntryDate> entryList = contratStepEntryDateRepository
                .findByFactureOrderByCreatedAtDesc(existingcontrat);
        if (!entryList.isEmpty()) {
            return ResponseEntity.ok(entryList);
        }

        throw new RuntimeException("No entry date with the given step and cotnrat");
    }




}
