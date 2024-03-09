package com.example.workflow.Controllers;

import com.example.workflow.Dto.StepDTO;
import com.example.workflow.Dto.StepResponseDto;
import com.example.workflow.Entities.Step;
import com.example.workflow.Entities.StepFacture;
import com.example.workflow.Services.interfaces.IstepFactureService;
import com.example.workflow.Services.interfaces.IstepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stepFactures")
public class StepFactureController {

    @Autowired
    private IstepFactureService stepService;

    @GetMapping
    public List<StepResponseDto> getAllSteps() {
        return stepService.getAllSteps();
    }

    @GetMapping("/{id}")
    public StepDTO getStepById(@PathVariable Long id) {
        return stepService.getStepById(id);
    }

    @PostMapping
    public StepDTO createStep(@RequestBody StepFacture step) {
        return stepService.createStep(step);
    }

    @PutMapping("/{id}")
    public StepFacture updateStep(@PathVariable Long id, @RequestBody StepFacture step) {
        return stepService.updateStep(id, step);
    }

    @DeleteMapping("/{id}")
    public void deleteStep(@PathVariable Long id) {
        stepService.deleteStep(id);
    }
}