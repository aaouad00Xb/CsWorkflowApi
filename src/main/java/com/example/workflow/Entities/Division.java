package com.example.workflow.Entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "divisionId")

public class Division extends  BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long divisionId;
       @Column(columnDefinition = "Text")
        private String divisionName;

        private String abreviation;

        // Constructors, getters, and setters

        @ManyToOne
        private Pole pole; // Reference to the Pole entity



//    @JsonIgnoreProperties({"division"})
//    @OneToMany(mappedBy = "division")
//    private List<Business> businesses;


        @JsonIgnoreProperties({"division","business","currentStep","createdAt"})
        @OneToMany(mappedBy = "division")
        private List<ContratSousTraitance> contratSousTraitances;




}

