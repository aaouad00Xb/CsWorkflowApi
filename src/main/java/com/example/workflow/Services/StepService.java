package com.example.workflow.Services;

import com.example.workflow.Dto.StepDTO;
import com.example.workflow.Dto.StepResponseDto;
import com.example.workflow.Entities.Step;
import com.example.workflow.Entities.StepField_r;
import com.example.workflow.Repositories.StepFieldRepository;
import com.example.workflow.Repositories.StepRepository;
import com.example.workflow.Services.impl.IstepService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class StepService implements IstepService {

    @Autowired
    private StepRepository stepRepository;

    @Autowired
    private  ObjectMapper objectMapper; // Autowire the ObjectMapper


     @Autowired
    private StepFieldRepository stepFieldRepository; // Autowire the ObjectMapper

    @Override
    public List<StepResponseDto> getAllSteps() {
        List<Step> steps = stepRepository.findSteps();

        // Convert the list of Step entities to StepDTO objects with related entity IDs
        return steps.stream()
                .map(step -> objectMapper.convertValue(step,StepResponseDto.class))
                .collect(Collectors.toList());
    }




    @Override
    public StepDTO getStepById(Long id) {
        return convertStepToStepDTO(stepRepository.findById(id).orElse(null));
    }

    @Override
    public StepDTO createStep(Step step) {
        return convertStepToStepDTO(stepRepository.save(step));
    }

    @Override
    public Step updateStep(Long id, Step updatedStep) {

//        StepDTO existingStep = getStepById(id);
//        if (existingStep != null) {
//            // Update fields as needed
//            existingStep.setStepName(updatedStep.getStepName());
//            existingStep.setStepDescription(updatedStep.getStepDescription());
//            // Update other fields here
//
//            return stepRepository.save(existingStep);
//        }
//        return null;
        return null;

    }

    @Override
    public void deleteStep(Long id) {
        stepRepository.deleteById(id);
    }




    @Override
    public StepDTO convertStepToStepDTO(Step step) {
        StepDTO stepDTO = objectMapper.convertValue(step, StepDTO.class);

        // Fetch the related entities and populate their IDs
        if (step.getNextStep() != null) {
            stepDTO.setNextStepID(step.getNextStep().getStepID());
        }
        if (step.getPreviousStep() != null) {
            stepDTO.setPreviousStepID(step.getPreviousStep().getStepID());
        }

        List<Long> stepFieldIds = step.getStepFields()
                .stream()
                .map(stepField -> stepField.getStepFieldID())
                .collect(Collectors.toList());
        stepDTO.setStepFieldsIds(stepFieldIds);

        return stepDTO;
    }


    @Override
    public Step convertStepDTOToStep(StepDTO stepDTO) {
        Step step = new Step(); // Create a new Step entity or retrieve an existing one

        // Populate the Step entity with data from the StepDTO
        step.setStepID(stepDTO.getStepID());
        step.setStepName(stepDTO.getStepName());
        step.setStepDescription(stepDTO.getStepDescription());

        // If stepDTO includes related entity IDs, you can fetch the related entities and set them
        // For example, for nextStep and previousStep:
        if (stepDTO.getNextStepID() != null) {
            Step nextStep = stepRepository.findById(stepDTO.getNextStepID()).orElse(null); // Replace with your repository call
            step.setNextStep(nextStep);
        }
        if (stepDTO.getPreviousStepID() != null) {
            Step previousStep = stepRepository.findById(stepDTO.getPreviousStepID()).orElse(null); // Replace with your repository call
            step.setPreviousStep(previousStep);
        }

        // For stepFields, you can fetch related StepField entities using their IDs and set them
        List<StepField_r> stepFields = stepDTO.getStepFieldsIds().stream()
                .map(stepFieldId -> stepFieldRepository.findById(stepFieldId).orElse(null)) // Replace with your repository call
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        step.setStepFields(stepFields);

        return step;
    }

}