package com.example.workflow.Controllers;

import com.example.workflow.Dto.BuisnessCreationDto;
import com.example.workflow.Dto.BusinessDTO;
import com.example.workflow.Entities.Business;
import com.example.workflow.Services.BusinessService;
import com.example.workflow.Services.impl.IBuisinessService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/businesses")
@Validated
public class BusinessController {

    @Autowired
    private IBuisinessService businessService;

    @GetMapping
    public Page<Business> getBusinesses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return businessService.getAllBusinesses(pageable);
    }

    @GetMapping("/getall")
    public ResponseEntity<?> getAllBusinesses(

    ) {
        return new ResponseEntity<>(businessService.getAllBusinesses(), HttpStatus.OK) ;
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getBusinessById(@PathVariable Long id) {
        return businessService.getBusinessById(id);
    }

    @PostMapping
    public ResponseEntity<?> createBusiness(@Valid @RequestBody BuisnessCreationDto business) {
        return businessService.createBusiness(business);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBusiness(@Valid @PathVariable Long id, @RequestBody Business business) {
        return businessService.updateBusiness(id, business);
    }

    @DeleteMapping("/{id}")
    public void deleteBusiness(@PathVariable Long id) {
        businessService.deleteBusiness(id);
    }


//    @GetMapping("findBusinessesByUserId/{id}")
//    public List<BusinessDTO> findBusinessesByUserId(@PathVariable Long id) {
//        return new ResponseEntity<>(businessService.getAllBusinesses(), HttpStatus.OK) ;
//    }


}