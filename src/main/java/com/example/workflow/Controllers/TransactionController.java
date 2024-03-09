package com.example.workflow.Controllers;


import com.example.workflow.Services.interfaces.ItransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Transaction")
public class TransactionController {



    @Autowired
    private ItransactionService transactionService;


    @GetMapping("/getEntryDate/{contratID}/{stepId}")
    public ResponseEntity<?> getEntryDate(@PathVariable("contratID") Long contratID, @PathVariable("stepId") Long newstepId) {
        try{
            return  this.transactionService.getEntryDateByContratAndStep(contratID,newstepId);

        }catch (Exception e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }


    }


    @GetMapping("/getAllEntryDate/{contratID}")
    public ResponseEntity<?> getAllEntryDate(@PathVariable("contratID") Long contratID) {
        try{
            return  this.transactionService.getEntryDateByContrat(contratID);

        }catch (Exception e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }


    }



}
