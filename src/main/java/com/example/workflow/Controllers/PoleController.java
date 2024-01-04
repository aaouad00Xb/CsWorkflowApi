package com.example.workflow.Controllers;

import com.example.workflow.Dto.Pole_creation_DTO;
import com.example.workflow.Entities.Pole;
import com.example.workflow.Services.PoleService;
import com.example.workflow.Services.impl.IpoleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/poles")
@Validated
public class PoleController {

    @Autowired
    private IpoleService poleService;

    @GetMapping
    public ResponseEntity<?> getAllPoles() {
        return poleService.getAllPoles();
    }




    @GetMapping("/{id}")
    public ResponseEntity<?> getPoleById(@PathVariable Long id) {
        return poleService.getPoleById(id);
    }

    @PostMapping("/createPole")
    public ResponseEntity<?> createPole(@Valid @RequestBody Pole_creation_DTO pole) {
        System.out.println("hello world");
        return poleService.createPole(pole);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePole(@PathVariable Long id, @RequestBody Pole_creation_DTO pole) {
        return poleService.updatePole(id, pole);
    }

    @DeleteMapping("/{id}")
    public void deletePole(@PathVariable Long id) {
        poleService.deletePole(id);
    }
}