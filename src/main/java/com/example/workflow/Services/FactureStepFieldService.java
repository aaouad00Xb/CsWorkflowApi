package com.example.workflow.Services;

import com.example.workflow.Dto.FactureStepFieldRquest;
import com.example.workflow.Dto.ResponseDto;
import com.example.workflow.Entities.FactureStepField;
import com.example.workflow.Repositories.*;
import com.example.workflow.Services.interfaces.IfactureStepFeildService;
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
public class FactureStepFieldService implements IfactureStepFeildService {

    @Autowired
    private FactureStepFieldRepository factureStepFieldRepository;

    @Autowired
    private FactureRepository factureRepository;

    @Autowired
    private ObjectMapper objectMapper;


    @Autowired
    private FactureStepRepository stepRepository;


    @Override
    public List<FactureStepField> getAllFactureStepFields() {
        return factureStepFieldRepository.findAll();
    }

    @Override
    public ResponseEntity<?> getFactureStepFieldById(Long id) {
        try{
            Optional<FactureStepField> factureStepField = Optional.ofNullable(factureStepFieldRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("stepField", "id", "" + id)));
            return ResponseEntity.status(200).body(factureStepField.get());
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public FactureStepField createFactureStepField(FactureStepField factureStepField) {
        return factureStepFieldRepository.save(factureStepField);
    }

    @Override
    public ResponseEntity<?> updateFactureStepField(Long id, FactureStepField updatedFactureStepField) {
        try{

            Optional<FactureStepField> existingFactureStepField = Optional.ofNullable(factureStepFieldRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("stepField", "id", "" + id)));
            FactureStepField factureStepField = existingFactureStepField.get();

            factureStepField.setValid(updatedFactureStepField.isValid());
            factureStepFieldRepository.save(factureStepField);
            return ResponseEntity.status(200).body(new ResponseDto("200", AccountsConstants.MESSAGE_200));

        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<FactureStepField> findByFactureAndStep(Long factureID, Long stepID) {
        // Implement the logic to retrieve Factures by Business ID and SousTraitant ID
        return factureStepFieldRepository.findByFactureAndStep(factureRepository.findById(factureID).get(), stepRepository.findById(stepID).get());
    }


    @Override
    public ResponseEntity<?> updateStepFields(List<FactureStepFieldRquest> facturesStepField) {

        List<FactureStepField> fsaved = new ArrayList<>();
        for (FactureStepFieldRquest factureStepFieldRquest :facturesStepField){
            FactureStepField f=    factureStepFieldRepository.findById(factureStepFieldRquest.getStepFieldID()).get();
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
