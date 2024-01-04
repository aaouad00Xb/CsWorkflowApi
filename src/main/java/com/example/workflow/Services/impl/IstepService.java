package com.example.workflow.Services.impl;

import com.example.workflow.Dto.StepDTO;
import com.example.workflow.Dto.StepResponseDto;
import com.example.workflow.Entities.Step;

import java.util.List;

public interface IstepService {
    List<StepResponseDto> getAllSteps();

    StepDTO getStepById(Long id);

    StepDTO createStep(Step step);

    Step updateStep(Long id, Step updatedStep);

    void deleteStep(Long id);

    StepDTO convertStepToStepDTO(Step step);

    Step convertStepDTOToStep(StepDTO stepDTO);
}
