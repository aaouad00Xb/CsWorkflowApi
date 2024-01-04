package com.example.workflow.Services;

import com.example.workflow.Dto.BuisnessCreationDto;
import com.example.workflow.Dto.BuisnessResponseDto;
import com.example.workflow.Dto.ResponseDto;
import com.example.workflow.Entities.Business;
import com.example.workflow.Entities.Division;
import com.example.workflow.Repositories.BusinessRepository;
import com.example.workflow.Repositories.DivisionRepository;
import com.example.workflow.Services.impl.IBuisinessService;
import com.example.workflow.constants.AccountsConstants;
import com.example.workflow.exception.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BusinessService implements IBuisinessService {


    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private DivisionRepository divisionRepository;

    @Override
    public Page<Business> getAllBusinesses(Pageable pageable) {
        try{
            return businessRepository.findAll(pageable);

        }catch (Exception e){
            System.out.println(e);
            return businessRepository.findAll(pageable);

        }
    }
    @Override
    public ResponseEntity<?> getAllBusinesses() {
        try{
           List<BuisnessResponseDto> buisnessResponseDtos =  businessRepository.findAll().stream()
                    .map(b -> objectMapper.convertValue(b, BuisnessResponseDto.class))
                    .collect(Collectors.toList());

           return ResponseEntity.status(HttpStatus.OK).body(buisnessResponseDtos);
        }catch (Exception e){
            System.out.println(e);
         throw new RuntimeException("something goes wrong");
        }
    }

    @Override
    public ResponseEntity<?> getBusinessById(Long id) {
        try{
            Business b = businessRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("buisness","BuisnessId",""+id));
            BuisnessResponseDto bb = objectMapper.convertValue(b,BuisnessResponseDto.class);
            return ResponseEntity.status(HttpStatus.OK).body(bb);
            }catch (Exception e){
                throw new RuntimeException(e.getMessage());
            }

    }


//    public List<BusinessDTO> findBusinessesByUserId(Long id) {
//        return businessRepository.findBusinessesByUserId(id).stream()
//                .map(b -> objectMapper.convertValue(b, BusinessDTO.class))
//                .collect(Collectors.toList());
//
//    }


    @Override
    public ResponseEntity<?> createBusiness(BuisnessCreationDto business) {
        try{
            Business b = objectMapper.convertValue(business,Business.class);
            Division d = divisionRepository.findById(business.getDivisionID()).orElseThrow(()->new ResourceNotFoundException("division","division id",""+business.getDivisionID()));
            b.setDivision(d);
            businessRepository.save(b);
            return ResponseEntity.status(200).body(new ResponseDto("200", AccountsConstants.MESSAGE_200));

        }catch(Exception e){
            System.out.println(e);
            throw new RuntimeException(e.getMessage());
        }


    }
    @Override
    public ResponseEntity<?> updateBusiness(Long id, Business updatedBusiness) {
        Business existingBusiness = businessRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("buisness","BuisnessId",""+id));
       try {
           if (existingBusiness != null) {
               // Update fields as needed
               existingBusiness.setObjet(updatedBusiness.getObjet());
               existingBusiness.setMo(updatedBusiness.getMo());
               existingBusiness.setContactPerson(updatedBusiness.getContactPerson());
               // Update other fields here

               businessRepository.save(existingBusiness);

               return ResponseEntity.status(200).body(new ResponseDto("200", AccountsConstants.MESSAGE_200));

           }
       }catch (Exception e){
           throw new RuntimeException(e.getMessage());
       }
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new ResponseDto("500", AccountsConstants.STATUS_417));

    }

    @Override
    public ResponseEntity<?> deleteBusiness(Long id) {
        try{
            businessRepository.deleteById(id);
            return ResponseEntity.status(200).body(new ResponseDto("200", AccountsConstants.MESSAGE_200));

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new ResponseDto("500", AccountsConstants.STATUS_417));

        }
    }
}
