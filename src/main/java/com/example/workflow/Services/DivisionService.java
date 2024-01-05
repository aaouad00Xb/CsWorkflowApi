package com.example.workflow.Services;

import com.example.workflow.Dto.DivisionDtoResponse;
import com.example.workflow.Dto.DivisioncreationDto;
import com.example.workflow.Dto.ResponseDto;
import com.example.workflow.Entities.Division;
import com.example.workflow.Entities.Pole;
import com.example.workflow.Repositories.DivisionRepository;
import com.example.workflow.Repositories.PoleRepository;
import com.example.workflow.Services.impl.IDivisionService;
import com.example.workflow.constants.AccountsConstants;
import com.example.workflow.exception.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DivisionService implements IDivisionService {

    @Autowired
    private DivisionRepository divisionRepository;



    @Autowired
    private PoleRepository poleRepository;


    @Autowired
    private ObjectMapper objectMapper;



    @Override
    public ResponseEntity<?> getAllDivisions() {
        try{
        List<Division> divisions =divisionRepository.findAll();

        // Mapping Pole entities to PoleDTO objects using Java streams and ObjectMapper
        List<DivisionDtoResponse> divisionDTOList = divisions.stream()
                .map(d -> objectMapper.convertValue(d, DivisionDtoResponse.class))
                .collect(Collectors.toList());


        return ResponseEntity.status(200).body(divisionDTOList);
    }catch(Exception e){
        throw new ResourceNotFoundException("list of poles","poles","all");
    }
    }


    @Override
    public ResponseEntity<?> getDivisionByPoleId(Long id) {
        try{
            return ResponseEntity.status(200).body(objectMapper.convertValue(divisionRepository.findByPoleId(id), DivisionDtoResponse.class) )  ;

        }catch (Exception e){
            throw new ResourceNotFoundException("division by pole id", "pole-id",""+id);
        }
    }

    @Override
    public ResponseEntity<?> getDivisionById(Long id) {
        Division d =  divisionRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("division by pole id", "pole-id",""+id));
        return ResponseEntity.status(200).body(objectMapper.convertValue(d, DivisionDtoResponse.class));
    }

@Override
    public Division findInternDivisionById(Long id) {
        Division d =  divisionRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("division by pole id", "pole-id",""+id));
        return d;
    }


    @Override
    public ResponseEntity<?> createDivision(DivisioncreationDto division) {
        try{

            Pole p = poleRepository.findById(division.getPole()).orElseThrow(()->new ResourceNotFoundException("pole ","pole id",""+division.getPole()));
            Division d = Division.builder().divisionName(division.getDivisionName()).abreviation(division.getAbreviation()).pole(p).build();
            divisionRepository.save(d);
            return ResponseEntity.status(200).body(new ResponseDto("200", AccountsConstants.MESSAGE_200));

        }catch (Exception e){
            System.out.println(e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> updateDivision(Long id, DivisioncreationDto updatedDivision) {
        Division existingDivision = divisionRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("division by pole id", "pole-id",""+id));

        if (existingDivision != null) {
            // Update fields as needed
            existingDivision.setDivisionName(updatedDivision.getDivisionName());
            existingDivision.setAbreviation(updatedDivision.getAbreviation());
            // Update other fields here
            divisionRepository.save(existingDivision);
            return ResponseEntity.status(200).body(new ResponseDto("200", AccountsConstants.MESSAGE_200));

        }
        throw new RuntimeException("something goes wrong with this request");
    }

    @Override
    public ResponseEntity<?> deleteDivision(Long id) {

        try{
         divisionRepository.deleteById(id);
            return ResponseEntity.status(200).body(new ResponseDto("200", AccountsConstants.MESSAGE_200));

        }catch(Exception e){
            throw new RuntimeException("could not drop the given division");
        }
    }
}
