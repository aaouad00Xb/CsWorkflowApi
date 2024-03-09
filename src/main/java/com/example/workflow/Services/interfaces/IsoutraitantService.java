package com.example.workflow.Services.interfaces;

import com.example.workflow.Dto.SoustraitantCreationDto;
import com.example.workflow.Entities.Soustraitant;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IsoutraitantService {
    List<Soustraitant> getAllSoutraitant();

    ResponseEntity<?> createSoutraitant(SoustraitantCreationDto soustraitantCreationDto);

    ResponseEntity<?>  updateSoustraitant(Long id, SoustraitantCreationDto updatedPole);

    void deleteSoustraitant(Long id);


}
