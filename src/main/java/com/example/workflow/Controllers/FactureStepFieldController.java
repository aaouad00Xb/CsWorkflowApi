package com.example.workflow.Controllers;

import com.example.workflow.Dto.FactureStepFieldRquest;
import com.example.workflow.Entities.FactureStepField;
import com.example.workflow.Services.interfaces.IfactureStepFeildService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/FactureStepFields")
public class FactureStepFieldController {

        @Autowired
        private IfactureStepFeildService factureStepFieldService;

        @GetMapping("/getAll")
        public List<FactureStepField> getAllFactureStepFields() {
        return factureStepFieldService.getAllFactureStepFields();
    }

        @GetMapping("/{id}")
        public ResponseEntity<?> getFactureStepFieldById(@PathVariable Long id) {
        return factureStepFieldService.getFactureStepFieldById(id);
    }

        @GetMapping("/findByContratAndStep/{id}/{step}")
        public List<FactureStepField> findByFactureAndStep(@PathVariable Long id, @PathVariable Long step) {
        return factureStepFieldService.findByFactureAndStep(id,step);
    }

        @PostMapping
        public FactureStepField createFactureStepField(@RequestBody FactureStepField factureStepField) {
        return factureStepFieldService.createFactureStepField(factureStepField);
    }

        @PutMapping("/{id}")
        public ResponseEntity<?> updateFactureStepField(@PathVariable Long id, @RequestBody FactureStepField factureStepField) {
        return factureStepFieldService.updateFactureStepField(id, factureStepField);
    }

        @DeleteMapping("/{id}")
        public ResponseEntity<?> deleteFactureStepField(@PathVariable Long id) {
        return factureStepFieldService.deleteFactureStepField(id);
    }


        @PutMapping("/update")
        public ResponseEntity<?> updateStepFields(@RequestBody List< FactureStepFieldRquest > facturesStepField) {
        return factureStepFieldService.updateStepFields(facturesStepField);
    }

}
