package com.example.workflow.Services.interfaces;

import com.example.workflow.Dto.StepDTO;
import com.example.workflow.Dto.StepResponseDto;
import com.example.workflow.Entities.Step;
import com.example.workflow.Entities.StepFacture;

import java.util.List;

public interface IstepFactureService {
    List<StepResponseDto> getAllSteps();

    StepDTO getStepById(Long id);

    StepDTO createStep(StepFacture step);

    StepFacture updateStep(Long id, StepFacture updatedStep);

    void deleteStep(Long id);

    StepDTO convertStepToStepDTO(StepFacture step);

    StepFacture convertStepDTOToStep(StepDTO stepDTO);
}


