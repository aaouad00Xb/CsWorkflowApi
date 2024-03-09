package com.example.workflow.Controllers;

import com.example.workflow.Services.interfaces.IFTransactionService;
import com.example.workflow.Services.interfaces.ItransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/TransactionF")
public class TransactionFController  {



    @Autowired
    private IFTransactionService transactionService;


    @GetMapping("/getEntryDate/{factureID}/{stepId}")
    public ResponseEntity<?> getEntryDate(@PathVariable("factureID") Long factureID, @PathVariable("stepId") Long newstepId) {
        try{
            return  this.transactionService.getEntryDateByFactureAndStep(factureID,newstepId);

        }catch (Exception e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }


    }


    @GetMapping("/getAllEntryDate/{factureID}")
    public ResponseEntity<?> getAllEntryDate(@PathVariable("factureID") Long factureID) {
        try{
            return  this.transactionService.getEntryDateByFacture(factureID);

        }catch (Exception e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }


    }



}
