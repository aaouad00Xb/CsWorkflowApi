package com.example.workflow.Services;

import com.example.workflow.Dto.FactureStepFieldRquest;
import com.example.workflow.Dto.ResponseDto;
import com.example.workflow.Entities.ContratStepField;
import com.example.workflow.Repositories.ContratSoustraitanceRepo;
import com.example.workflow.Repositories.ContratStepFieldRepository;
import com.example.workflow.Repositories.StepRepository;
import com.example.workflow.Services.interfaces.IcontratStepFieldService;
import com.example.workflow.constants.AccountsConstants;
import com.example.workflow.exception.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ContratStepFieldService implements IcontratStepFieldService {

    @Autowired
    private ContratStepFieldRepository factureStepFieldRepository;

    @Autowired
    private ContratSoustraitanceRepo contratRepository;


    @Autowired
    private ObjectMapper objectMapper;


    @Autowired
    private StepRepository stepRepository;


    @Override
    public List<ContratStepField> getAllFactureStepFields() {
        return factureStepFieldRepository.findAll();
    }

    @Override
    public ResponseEntity<?> getFactureStepFieldById(Long id) {
        try{
           Optional<ContratStepField> contratStepField = Optional.ofNullable(factureStepFieldRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("stepField", "id", "" + id)));
           return ResponseEntity.status(200).body(contratStepField.get());
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public ContratStepField createFactureStepField(ContratStepField factureStepField) {
        return factureStepFieldRepository.save(factureStepField);
    }

    @Override
    public ResponseEntity<?> updateFactureStepField(Long id, ContratStepField updatedFactureStepField) {
        try{

        Optional<ContratStepField> existingFactureStepField = Optional.ofNullable(factureStepFieldRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("stepField", "id", "" + id)));
            ContratStepField contratStepField = existingFactureStepField.get();

            contratStepField.setValid(updatedFactureStepField.isValid());
            factureStepFieldRepository.save(contratStepField);
            return ResponseEntity.status(200).body(new ResponseDto("200", AccountsConstants.MESSAGE_200));

    }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<ContratStepField> findByContratAndStep(Long factureID, Long stepID) {
        // Implement the logic to retrieve Factures by Business ID and SousTraitant ID
        return factureStepFieldRepository.findByContratAndStep(contratRepository.findById(factureID).get(), stepRepository.findById(stepID).get());
    }


   @Override
   public ResponseEntity<?> updateStepFields(List<FactureStepFieldRquest> facturesStepField) {

       List<ContratStepField> fsaved = new ArrayList<>();
        for (FactureStepFieldRquest factureStepFieldRquest :facturesStepField){
            ContratStepField f=    factureStepFieldRepository.findById(factureStepFieldRquest.getStepFieldID()).get();
            f.setValid(factureStepFieldRquest.isValid());
            fsaved.add(factureStepFieldRepository.save(f));
        }
        return new ResponseEntity<>(fsaved, HttpStatus.OK);
    }


    @Override
    public ResponseEntity<?> deleteFactureStepField(Long id) {
        try{
            factureStepFieldRepository.deleteById(id);
            return ResponseEntity.status(200).body(new ResponseDto("200", AccountsConstants.MESSAGE_200));

        }catch (Exception e){
            return ResponseEntity.status(417).body(new ResponseDto("417", AccountsConstants.STATUS_417));

        }
    }
}