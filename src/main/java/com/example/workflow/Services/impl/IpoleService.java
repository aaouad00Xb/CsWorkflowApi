package com.example.workflow.Services.impl;

import com.example.workflow.Dto.Pole_creation_DTO;
import com.example.workflow.Entities.Pole;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IpoleService {
    ResponseEntity<?> getAllPoles();

    ResponseEntity<?>  getPoleById(Long id);

    ResponseEntity<?> createPole(Pole_creation_DTO pole);

    ResponseEntity<?>  updatePole(Long id, Pole_creation_DTO updatedPole);

    void deletePole(Long id);
}
