package com.example.workflow.Controllers;

import com.example.workflow.Dto.FactureStepFieldRquest;
import com.example.workflow.Entities.ContratStepField;
import com.example.workflow.Services.impl.IcontratStepFieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ContratStepFields")
public class ContratStepFieldController {

    @Autowired
    private IcontratStepFieldService contratStepFieldService;

    @GetMapping("/getAll")
    public List<ContratStepField> getAllFactureStepFields() {
        return contratStepFieldService.getAllFactureStepFields();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFactureStepFieldById(@PathVariable Long id) {
        return contratStepFieldService.getFactureStepFieldById(id);
    }

    @GetMapping("/findByContratAndStep/{id}/{step}")
    public List<ContratStepField> findByFactureAndStep(@PathVariable Long id, @PathVariable Long step) {
        return contratStepFieldService.findByContratAndStep(id,step);
    }

    @PostMapping
    public ContratStepField createFactureStepField(@RequestBody ContratStepField factureStepField) {
        return contratStepFieldService.createFactureStepField(factureStepField);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateFactureStepField(@PathVariable Long id, @RequestBody ContratStepField factureStepField) {
        return contratStepFieldService.updateFactureStepField(id, factureStepField);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFactureStepField(@PathVariable Long id) {
        return contratStepFieldService.deleteFactureStepField(id);
    }


    @PutMapping("/update")
    public ResponseEntity<?> updateStepFields(@RequestBody List<FactureStepFieldRquest> facturesStepField) {
        return contratStepFieldService.updateStepFields(facturesStepField);
    }



}
