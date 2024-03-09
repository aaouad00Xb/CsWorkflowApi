package com.example.workflow.Services;

import com.example.workflow.Dto.ResponseDto;
import com.example.workflow.Dto.SoustraitantCreationDto;
import com.example.workflow.Entities.Soustraitant;
import com.example.workflow.Repositories.SousTraitantRepo;
import com.example.workflow.Services.interfaces.IsoutraitantService;
import com.example.workflow.constants.AccountsConstants;
import com.example.workflow.exception.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SousTraitantService implements IsoutraitantService {


    @Autowired
    private SousTraitantRepo sousTraitantRepo;
    @Override
    public List<Soustraitant> getAllSoutraitant(){
        return sousTraitantRepo.findAll();
    }




    @Autowired
    private ObjectMapper objectMapper;


    @Override
    public ResponseEntity<?> createSoutraitant(SoustraitantCreationDto soustraitantCreationDto) {

        try{

            Soustraitant soustraitant = objectMapper.convertValue(soustraitantCreationDto,Soustraitant.class);

            sousTraitantRepo.save(soustraitant);


            return ResponseEntity.status(200).body(new ResponseDto(AccountsConstants.STATUS_200,AccountsConstants.MESSAGE_200));
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }


    }

    @Override
    public ResponseEntity<?> updateSoustraitant(Long id, SoustraitantCreationDto soustraitantCreationDto) {
        Optional<Soustraitant> existingSoustrait = Optional.ofNullable(sousTraitantRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("pole", "pole id", "" + id)));
        if (existingSoustrait != null) {
            // Update fields as needed
            System.out.println(existingSoustrait);
            Soustraitant s = existingSoustrait.get();
            s.setRc(soustraitantCreationDto.getRc());
            s.setEmail(soustraitantCreationDto.getEmail());
            s.setName(soustraitantCreationDto.getName());
            s.setDomainActivity(soustraitantCreationDto.getDomainActivity());
            // Update other fields here
            System.out.println(existingSoustrait);
            System.out.println(s);

            try{
                sousTraitantRepo.save(s);
                return ResponseEntity.status(200).body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));

            }catch (Exception e){
                throw new RuntimeException("something is happening");
            }
        }
        return null;
    }

    @Override
    public void deleteSoustraitant(Long id) {
        sousTraitantRepo.deleteById(id);
    }





}
