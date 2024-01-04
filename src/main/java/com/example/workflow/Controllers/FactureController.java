//package com.example.workflow.Controllers;
//
//import com.example.workflow.Dto.FactureCreationDto;
//import com.example.workflow.Entities.ContratSousTraitance;
//import com.example.workflow.Services.impl.IcontratService;
//import com.example.workflow.Services.impl.ItransactionService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/factures")
//public class FactureController {
//
//    @Autowired
//    private IcontratService factureService;
//
//    @Autowired
//    private ItransactionService transactionService;
//
//    @GetMapping
//    public List<ContratSousTraitance> getAllFactures() {
//        return factureService.getAllFactures();
//    }
//
//    @GetMapping("/{id}")
//    public ContratSousTraitance getFactureById(@PathVariable Long id) {
//        return factureService.getFactureById(id);
//    }
//
//    @PutMapping("/{id}")
//    public ContratSousTraitance updateFacture(@PathVariable Long id, @RequestBody ContratSousTraitance facture) {
//        return factureService.updateFacture(id, facture);
//    }
//
//
//    @GetMapping("/by-business-and-soustraitant")
//    public ResponseEntity<List<ContratSousTraitance>> getFacturesByBusinessAndSousTraitant(
//            @RequestParam Long businessId, @RequestParam Long soustraitantId) {
//        List<ContratSousTraitance> factures = factureService.getFacturesByBusinessAndSousTraitant(businessId, soustraitantId);
//        return ResponseEntity.ok(factures);
//    }
//
//
//    @GetMapping("/getFacturesBySousTraitance/{id}")
//    public ResponseEntity<List<ContratSousTraitance>> getFacturesBySousTraitance(@PathVariable Long id) {
//        List<ContratSousTraitance> factures = factureService.getFacturesBySousTraitance(id);
//        return ResponseEntity.ok(factures);
//    }
//
//
//    @DeleteMapping("/{id}")
//    public void deleteFacture(@PathVariable Long id) {
//        factureService.deleteFacture(id);
//    }
//
//    @PostMapping("/create")
//    public ContratSousTraitance createFacture(@RequestBody FactureCreationDto facture) {
//
//        return factureService.createFacture(facture);
//    }
//
//
//    @GetMapping("/transitionFactureToStep/{factureId}/{stepId}")
//    public ResponseEntity<?> transitionFactureToStep(@PathVariable("factureId") Long factureId,@PathVariable("stepId") Long newstepId) {
//        try{
//            this.transactionService.transitionContratToStep(factureId,newstepId);
//            return new ResponseEntity<>(HttpStatus.ACCEPTED);
//
//        }catch (Exception e){
//            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
//        }
//
//
//    }
//
//
//
//
//
//
//
//}