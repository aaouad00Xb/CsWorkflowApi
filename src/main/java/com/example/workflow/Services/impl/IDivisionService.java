package com.example.workflow.Services.impl;

import com.example.workflow.Dto.DivisioncreationDto;
import com.example.workflow.Entities.Division;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IDivisionService {
    ResponseEntity<?> getAllDivisions();

    ResponseEntity<?> getDivisionByPoleId(Long id);

    ResponseEntity<?> getDivisionById(Long id);

//    ResponseEntity<?> createDivision(Division division);

    ResponseEntity<?> createDivision(DivisioncreationDto division);

//    ResponseEntity<?> updateDivision(Long id, Division updatedDivision);

    ResponseEntity<?> updateDivision(Long id, DivisioncreationDto updatedDivision);

    ResponseEntity<?>  deleteDivision(Long id);
}