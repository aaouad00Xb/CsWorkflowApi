package com.example.workflow.Services.interfaces;

import com.example.workflow.Dto.Pole_creation_DTO;
import org.springframework.http.ResponseEntity;

public interface IpoleService {
    ResponseEntity<?> getAllPoles();

    ResponseEntity<?>  getPoleById(Long id);

    ResponseEntity<?> createPole(Pole_creation_DTO pole);

    ResponseEntity<?>  updatePole(Long id, Pole_creation_DTO updatedPole);

    void deletePole(Long id);
}
