package com.example.workflow.mapper;

import com.example.workflow.Dto.FactureResponseDto;
import com.example.workflow.Entities.Facture;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class DeserializationHelper {

    private final ObjectMapper objectMapper;

    public DeserializationHelper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public FactureResponseDto deserializeFactureEntity(Facture entity) {
        // Implement custom deserialization logic as needed
        // Example: Mapping fields manually or handling nested objects
        FactureResponseDto dto = new FactureResponseDto();
        dto.setFactureID(entity.getFactureID());
        dto.setFactureNumber(entity.getFactureNumber());
        dto.setFactureDate(entity.getFactureDate());
        dto.setValid(entity.isValid());
        dto.setCurrentStep(entity.getCurrentStep());
        dto.setUploadedFiles(entity.getUploadedFiles());
        // Map other fields...

        return dto;
    }
}
