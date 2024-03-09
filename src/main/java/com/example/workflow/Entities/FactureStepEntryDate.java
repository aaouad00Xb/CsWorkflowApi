package com.example.workflow.Entities;

import com.example.workflow.Enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Entity
public class FactureStepEntryDate extends  BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Facture facture;

    @Column(columnDefinition = "Text")
    private String commentaire;


    @Enumerated(EnumType.STRING)
    private TransactionType type;


    @ManyToOne
    private StepFacture step;

    private LocalDateTime entryDate; // Entry date of Facture in this step

    // Constructors, getters, and setters

    public FactureStepEntryDate(Long id, LocalDateTime entryDate) {
        this.id = id;
        this.entryDate = entryDate;
    }

}
