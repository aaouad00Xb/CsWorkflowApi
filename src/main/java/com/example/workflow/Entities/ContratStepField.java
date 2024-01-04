package com.example.workflow.Entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "stepFieldID")

public class ContratStepField extends  BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stepFieldID;
    @Column(columnDefinition = "Text")
    private String fieldName;
    private String fieldValue;
    private boolean isValid;  // Indicates whether the field is valid or not

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.MERGE)
    private ContratSousTraitance contrat;  // The facture associated with this step field

    @ManyToOne(cascade = CascadeType.MERGE)
    private Step step;  // The step to which this field belongs

    // Constructors, getters, and setters
}
