package com.example.workflow.Services;

import com.example.workflow.Entities.*;
import com.example.workflow.Enums.TransactionType;
import com.example.workflow.Repositories.*;
import com.example.workflow.Services.interfaces.IFTransactionService;
import com.example.workflow.exception.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class TransactionFService implements IFTransactionService {


    @Autowired
    private FactureRepository factureRepository;


    @Autowired
    private FactureStepFieldRepository factureStepFieldRepository;


    @Autowired
    private FactureStepFieldRepository stepFieldRepository;

    @Autowired
    private FactureEntryDateRepository factureEntryDateRepository;




    @Autowired
    @Lazy
    private NotificationService notificationService;





    @Autowired
    private FactureStepRepository stepRepository;

    @Autowired
    private ContratSoustraitanceRepo contratSoustraitanceRepo;



    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Facture initFactureToStep(Facture facture) {
        StepFacture newStep = stepRepository.findById(1L).get();
        //todo
        createContratStepFieldsForNewStep(facture, newStep);

        // Update the currentStep of the Facture to the new Step
        facture.setCurrentStep(newStep);
        recordFactureEntryDateForStep(facture, newStep, TransactionType.init);

        //notification
        //todo
        this.notificationService.notifyInitFacture(facture);

        return factureRepository.save(facture);
    }

    @Override
    public ResponseEntity<?> transitionFactureToStep(Long factureId, Long newStepID) {

        StepFacture newStep = stepRepository.findById(newStepID).get();
        Facture facture = factureRepository.findById(factureId).get();

        // Retrieve relevant StepField entries for the current Step
        List<FactureStepField> factureStepFields = factureStepFieldRepository.findByFactureAndStep(facture,facture.getCurrentStep() );


        // Check if all StepFields of the current step are valid
        boolean allStepFieldsValid = areAllStepFieldsValid(factureStepFields);

        if (allStepFieldsValid) {
            // Create FactureStepField entries for the new step
            createContratStepFieldsForNewStep(facture, newStep);

            // Record entry date for the new step
            recordFactureEntryDateForStep(facture, newStep,TransactionType.prove);

            //notification
            // todo
            this.notificationService.notifyProveFacture(facture);

            // Update the currentStep of the Facture to the new Step
            facture.setCurrentStep(newStep);
            Facture savedFacture =  factureRepository.save(facture);
            return ResponseEntity.status(200).body(savedFacture);
        } else {
            throw new RuntimeException("something goes wrong");            // Handle the case where not all StepFie
            // You can add custom logic for handling this situation, such as error handling or notifications
        }    }

    @Override
    public ResponseEntity<?> transitionFactureToStep(Long factureId, Long newStepID, String commentaire) {


        System.out.println("hello");
        StepFacture newStep = stepRepository.findById(newStepID).get();
        Facture facture = factureRepository.findById(factureId).get();

        // Retrieve relevant StepField entries for the current Step
        List<FactureStepField> factureStepFields = factureStepFieldRepository.findByFactureAndStep(facture,facture.getCurrentStep() );


        // Check if all StepFields of the current step are valid
        boolean allStepFieldsValid = areAllStepFieldsValid(factureStepFields);

        boolean forward = true;

        if(facture.getCurrentStep()!=null){
            if (facture.getCurrentStep().getStepID()>newStepID){
                forward = false;
            }
        }

        boolean passedBythisStepBefore = this.existsByContratAndStep(facture,newStep);
        System.out.println("achraf");
        System.out.println(passedBythisStepBefore);


        if(forward){
            if (allStepFieldsValid) {
                // Create FactureStepField entries for the new step


                if(!passedBythisStepBefore) createContratStepFieldsForNewStep(facture, newStep);

                // Record entry date for the new step
                recordFactureEntryDateForStep(facture, newStep,commentaire,TransactionType.prove);


                // Update the currentStep of the Facture to the new Step
                facture.setCurrentStep(newStep);

                Facture facture1 =  factureRepository.save(facture);
                //notification
                //todo
                this.notificationService.notifyProveFacture(facture1);

                return ResponseEntity.status(200).body(facture1);
            } else {
                throw new RuntimeException("merci de valider toutes les elements de l'etape précedente");            // Handle the case where not all StepFie
                // You can add custom logic for handling this situation, such as error handling or notifications
            }
        }else {
            // Record entry date for the new step
            recordFactureEntryDateForStep(facture, newStep,commentaire,TransactionType.disprove);
            // Update the currentStep of the Facture to the new Step
            facture.setCurrentStep(newStep);
            Facture facture1 =  factureRepository.save(facture);
//            todo
            this.notificationService.notifyProveFacture(facture1);

            return ResponseEntity.status(200).body(facture1);


        }
    }







    public boolean existsByContratAndStep(Facture facture ,StepFacture step){
        return factureStepFieldRepository.existsByFactureAndStep(facture,step);

    }

    @Override
    public ResponseEntity<?> validateFacture(Long factureId, String commentaire) {
        System.out.println("hello");
        StepFacture newStep = stepRepository.findById(6L).get();
        Facture facture = factureRepository.findById(factureId).get();

        // Retrieve relevant StepField entries for the current Step
        List<FactureStepField> factureStepFields = factureStepFieldRepository.findByFactureAndStep(facture,facture.getCurrentStep() );


        // Check if all StepFields of the current step are valid
        boolean allStepFieldsValid = areAllStepFieldsValid(factureStepFields);

        if (allStepFieldsValid) {

            // Record entry date for the new step
            recordFactureEntryDateForStep(facture, newStep,commentaire,TransactionType.validation);
        //todo
           this.notificationService.notifyValidFacture(facture);
            facture.setValid(true);
            Facture facture1 =  factureRepository.save(facture);

            return ResponseEntity.status(200).body(facture1);
        } else {
            throw new RuntimeException("merci de valider toutes les elements de l'etape précedente");            // Handle the case where not all StepFie
            // You can add custom logic for handling this situation, such as error handling or notifications
        }

    }

    @Override
    public void recordFactureEntryDateForStep(Facture facture, StepFacture newStep, TransactionType type) {
        FactureStepEntryDate factureStepEntryDate = new FactureStepEntryDate();
        factureStepEntryDate.setFacture(facture);
        factureStepEntryDate.setStep(newStep);
        factureStepEntryDate.setType(type);

        factureStepEntryDate.setEntryDate(LocalDateTime.now()); // Set the entry date to the current timestamp or relevant date/time

        factureEntryDateRepository.save(factureStepEntryDate); // Save the entry date
    }

    @Override
    public void recordFactureEntryDateForStep(Facture facture, StepFacture newStep, String Commentaire, TransactionType type) {

        FactureStepEntryDate factureStepEntryDate = new FactureStepEntryDate();
        factureStepEntryDate.setFacture(facture);
        factureStepEntryDate.setStep(newStep);
        factureStepEntryDate.setCommentaire(Commentaire);
        factureStepEntryDate.setType(type);


        factureStepEntryDate.setEntryDate(LocalDateTime.now()); // Set the entry date to the current timestamp or relevant date/time

        factureEntryDateRepository.save(factureStepEntryDate); // Save the entry date

    }


    @Override
    public boolean areAllStepFieldsValid(List<FactureStepField> factureStepFields) {
        // Validate each StepField, return false if any are not valid
        for (FactureStepField stepField : factureStepFields) {
            if (!stepField.isValid()) {
                return false; // At least one StepField is not valid
            }
        }
        return true; // All StepFields are valid
    }

    @Override
    public void createContratStepFieldsForNewStep(Facture facture, StepFacture newStep) {
        // Retrieve relevant StepField entries for the new Step
        List<StepFieldF_r> newStepFields = stepFieldRepository.findByStep(newStep);

        for (StepFieldF_r newStepField : newStepFields) {
            // Create corresponding FactureStepField entries for the new Step
            FactureStepField factureStepField = new FactureStepField();
            factureStepField.setFieldName(newStepField.getFieldName());
            factureStepField.setValid(false); // Assume new StepFields are valid by default

            // Associate FactureStepField with the Facture and the new Step
            factureStepField.setFacture(facture);
            factureStepField.setStep(newStep);

            // Save the FactureStepField entry
            factureStepFieldRepository.save(factureStepField);
        }

    }


    @Override
    public ResponseEntity<?> getEntryDateByFactureAndStep(Long FactureID, Long stepID) {
        // Retrieve relevant StepField entries for the new Step
        Facture existingFacture =  factureRepository.findById(FactureID).orElseThrow(()->new ResourceNotFoundException("Facture ","Facture id",""+FactureID));
        StepFacture s =  stepRepository.findById(stepID).orElseThrow(()->new ResourceNotFoundException("step ","step id",""+stepID));


        List<FactureStepEntryDate> entryList = factureEntryDateRepository.findByFactureAndStepOrderByCreatedAtDesc(existingFacture, s);
        if (!entryList.isEmpty()) {
            FactureStepEntryDate latestEntry = entryList.get(0);
            return ResponseEntity.ok(latestEntry);
        }

        throw new RuntimeException("No entry date with the given step and Facture");
    }

    @Override
    public ResponseEntity<?> getEntryDateByFacture(Long FactureID) {

        // Retrieve relevant StepField entries for the new Step
        Facture existingcontrat =  factureRepository.findById(FactureID).orElseThrow(()->new ResourceNotFoundException("facture ","facture id",""+FactureID));


        List<FactureStepEntryDate> entryList = factureEntryDateRepository
                .findByFactureOrderByCreatedAtDesc(existingcontrat);
        if (!entryList.isEmpty()) {
            return ResponseEntity.ok(entryList);
        }

        throw new RuntimeException("No entry date with the given step and cotnrat");
    }
}
