package com.example.workflow.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Entity
public class FactureStepField  extends  BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stepFieldID;
    @Column(columnDefinition = "Text")
    private String fieldName;
    private String fieldValue;
    private boolean isValid;  // Indicates whether the field is valid or not

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.MERGE)
    private Facture facture;  // The facture associated with this step field

    @ManyToOne(cascade = CascadeType.MERGE)
    private StepFacture step;  // The step to which this field belongs

    // Constructors, getters, and setters
}