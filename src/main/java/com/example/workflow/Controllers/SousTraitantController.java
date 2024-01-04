package com.example.workflow.Controllers;


import com.example.workflow.Dto.ContratSoutraitanceCreationDto;
import com.example.workflow.Dto.Pole_creation_DTO;
import com.example.workflow.Dto.ResponseDto;
import com.example.workflow.Dto.SoustraitantCreationDto;
import com.example.workflow.Entities.*;
import com.example.workflow.Services.SousTraitantService;
import com.example.workflow.Services.impl.IsoutraitantService;
import com.example.workflow.constants.AccountsConstants;
import com.example.workflow.exception.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/SousTraitant")
@Validated
public class SousTraitantController {

    @Autowired
    private IsoutraitantService sousTraitantService;



    @GetMapping
    public List<Soustraitant> getAllSoustraitant(){
       return  this.sousTraitantService.getAllSoutraitant();
    }


    @PostMapping("/create-soustraitant")
    public ResponseEntity<?> createSoustraitant(@Valid @RequestBody SoustraitantCreationDto soustraitantCreationDto) {
        return this.sousTraitantService.createSoutraitant(soustraitantCreationDto);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateSoustraitant(@PathVariable Long id, @RequestBody SoustraitantCreationDto soustraitantCreationDto) {
        return sousTraitantService.updateSoustraitant(id, soustraitantCreationDto);
    }

    @DeleteMapping("/{id}")
    public void deleteSoustraitant(@PathVariable Long id) {
        sousTraitantService.deleteSoustraitant(id);
    }

}
