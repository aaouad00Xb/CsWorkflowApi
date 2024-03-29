package com.example.workflow.Controllers;

import com.example.workflow.Dto.DivisioncreationDto;
import com.example.workflow.Services.interfaces.IDivisionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/division")
@RequiredArgsConstructor
public class DivisionController {


    private final IDivisionService divisionService;

    @GetMapping("/getall")
    public ResponseEntity<?> getAllDivisions(
    ) {
        return new ResponseEntity<>(divisionService.getAllDivisions(), HttpStatus.OK) ;
    }

    @GetMapping("/getDivisionByPoleId/{id}")
    public ResponseEntity<?> getDivisionByPoleId(@PathVariable Long id) {
        return divisionService.getDivisionByPoleId(id);
    }

    @PostMapping("/createDivision")
    public ResponseEntity<?> createDivision(@Valid @RequestBody DivisioncreationDto divisioncreationDto) {
        System.out.println("hello world");
        return divisionService.createDivision(divisioncreationDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDivision(@PathVariable Long id, @RequestBody DivisioncreationDto divisioncreationDto) {
        return divisionService.updateDivision(id, divisioncreationDto);
    }

    @DeleteMapping("/{id}/delete")
    public void deletePole(@PathVariable Long id) {
        System.out.println("hello");
        divisionService.deleteDivision(id);
    }



}
