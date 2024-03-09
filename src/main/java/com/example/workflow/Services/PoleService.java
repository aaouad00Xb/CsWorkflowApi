package com.example.workflow.Services;

import com.example.workflow.Dto.PoleDtoResponse;
import com.example.workflow.Dto.Pole_creation_DTO;
import com.example.workflow.Dto.ResponseDto;
import com.example.workflow.Entities.Pole;
import com.example.workflow.Repositories.PoleRepository;
import com.example.workflow.Services.interfaces.IpoleService;
import com.example.workflow.constants.AccountsConstants;
import com.example.workflow.exception.CustomerAlreadyExistsException;
import com.example.workflow.exception.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PoleService implements IpoleService {

    @Autowired
    private PoleRepository poleRepository;

    @Autowired
    private  ObjectMapper objectMapper;



    @Override
    public ResponseEntity<?> getAllPoles() {

        try{
            List<Pole> poleList =  poleRepository.findAll();

            // Mapping Pole entities to PoleDTO objects using Java streams and ObjectMapper
            List<PoleDtoResponse> poleDTOList = poleList.stream()
                    .map(pole -> objectMapper.convertValue(pole, PoleDtoResponse.class))
                    .collect(Collectors.toList());


            return ResponseEntity.status(200).body(poleDTOList);
        }catch(Exception e){
            throw new ResourceNotFoundException("list of poles","poles","all");
        }
    }

    @Override
    public  ResponseEntity<?> getPoleById(Long id) {
        Optional<Pole> p = Optional.ofNullable(poleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("pole", "pole id", "" + id)));
        if(p.isPresent()) return ResponseEntity.status(200).body(p.get());

        return null;
    }

    @Override
    public ResponseEntity<?> createPole(Pole_creation_DTO pole) {
        Pole myPole = objectMapper.convertValue(pole, Pole.class);
       Optional<Pole> p =  poleRepository.findByPoleName(pole.getPoleName());
       if(p.isPresent()){
           throw new CustomerAlreadyExistsException("the pole exists already in the applicatipon");
       }
        System.out.println("hello world");
       poleRepository.save(myPole);
        return ResponseEntity.status(200).body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
    }

    @Override
    public ResponseEntity<?> updatePole(Long id, Pole_creation_DTO updatedPole) {
        Optional<Pole>  existingPole = Optional.ofNullable(poleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("pole", "pole id", "" + id)));
        if (existingPole != null) {
            // Update fields as needed
            Pole p = existingPole.get();
            p.setPoleName(updatedPole.getPoleName());
            p.setAbreviation(updatedPole.getAbreviation());
            // Update other fields here
            try{
                poleRepository.save(p);
                return ResponseEntity.status(200).body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));

            }catch (Exception e){
                throw new RuntimeException("something is happening");
            }
        }
        return null;
    }

    @Override
    public void deletePole(Long id) {
        poleRepository.deleteById(id);
    }
}