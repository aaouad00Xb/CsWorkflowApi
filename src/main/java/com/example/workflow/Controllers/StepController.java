package com.example.workflow.Controllers;

import com.example.workflow.Dto.StepDTO;
import com.example.workflow.Dto.StepResponseDto;
import com.example.workflow.Entities.Step;
import com.example.workflow.Services.StepService;
import com.example.workflow.Services.impl.IstepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/steps")
public class StepController {

    @Autowired
    private IstepService stepService;

    @GetMapping
    public List<StepResponseDto> getAllSteps() {
        return stepService.getAllSteps();
    }

    @GetMapping("/{id}")
    public StepDTO getStepById(@PathVariable Long id) {
        return stepService.getStepById(id);
    }

    @PostMapping
    public StepDTO createStep(@RequestBody Step step) {
        return stepService.createStep(step);
    }

    @PutMapping("/{id}")
    public Step updateStep(@PathVariable Long id, @RequestBody Step step) {
        return stepService.updateStep(id, step);
    }

    @DeleteMapping("/{id}")
    public void deleteStep(@PathVariable Long id) {
        stepService.deleteStep(id);
    }
}
