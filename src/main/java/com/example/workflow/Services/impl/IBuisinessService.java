package com.example.workflow.Services.impl;

import com.example.workflow.Dto.BuisnessCreationDto;
import com.example.workflow.Dto.BusinessDTO;
import com.example.workflow.Entities.Business;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IBuisinessService {
    Page<Business> getAllBusinesses(Pageable pageable);

    ResponseEntity<?> getAllBusinesses();

    ResponseEntity<?>  getBusinessById(Long id);

    ResponseEntity<?>  createBusiness(BuisnessCreationDto business);

    ResponseEntity<?>  updateBusiness(Long id, Business updatedBusiness);

    ResponseEntity<?>  deleteBusiness(Long id);
}
