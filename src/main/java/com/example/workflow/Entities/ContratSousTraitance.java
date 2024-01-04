package com.example.workflow.Entities;

import com.example.workflow.Enums.TypeContrat;
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
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "factureID")

public class ContratSousTraitance extends  BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contratID;
    private String contratNumber;
    private Date signatureDate;
    private double totalAmount;

    @Enumerated(EnumType.STRING)
    private TypeContrat typeContrat;



    @JsonIgnoreProperties({ "sousTraitances"})
    @ManyToOne
    private Business business;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn
    private Soustraitant soustraitant;

    private Date dateDebut_contrat;
    private Date dateFin_contrat;

    @Column(columnDefinition = "boolean default false")
    private boolean valid;


    @ManyToOne()
    private Step currentStep;  // The current step for the facture


    @OneToMany(cascade=CascadeType.MERGE)
    private List<Document> uploadedFiles;


    @ManyToOne()
    private Division division;

//    @JsonIgnoreProperties({"username","email","manager","manager","division","myBuinesses"})
//    @ManyToOne
//    private User projectManager;  // The current step for the facture
//
//

}
