package com.example.workflow.Repositories;

import com.example.workflow.Entities.ContratSousTraitance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ContratStepEntryDateRepositoryTest {



    @Autowired
    private ContratStepEntryDateRepository entryDateRepository;

    @Autowired
    private ContratSoustraitanceRepo contratSoustraitanceRepo;

    @Test
    public void getting(){
        List<ContratSousTraitance> validatedContracts =    contratSoustraitanceRepo.findContratSousTraitancesByValidIsTrue();


    }
}