package com.example.workflow.Entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Facture extends  BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long factureID;
    @Column(columnDefinition = "Text")
    private String factureNumber;
    private Date factureDate;
    private double totalAmount;


    @ManyToOne
    private ContratSousTraitance contratSousTraitance;

    @ManyToOne
    private StepFacture currentStep;  // The current step for the facture


    @Column(columnDefinition = "boolean default false")
    private boolean valid;


    @OneToMany(cascade=CascadeType.MERGE)
    private List<Document> uploadedFiles;



//    @JsonIgnoreProperties({"username","email","manager","manager","division","myBuinesses"})
//    @ManyToOne
//    private User projectManager;  // The current step for the facture
//
//

}