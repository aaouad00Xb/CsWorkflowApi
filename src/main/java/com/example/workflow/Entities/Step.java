package com.example.workflow.Entities;

import com.example.workflow.Entities.User.UserRole;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "stepID")

public class Step extends  BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stepID;
    @Column(columnDefinition = "Text")
    private String stepName;
    @Column(columnDefinition = "TEXT")
    private String stepDescription;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @JsonIgnoreProperties({"stepDescription","nextStep","previousStep","entryDates"})
    @ManyToOne
    private Step nextStep;  // The next step

    @JsonIgnoreProperties({"stepDescription","nextStep","previousStep","entryDates"})
    @ManyToOne
    private Step previousStep;  // The previous step

    @JsonIgnore
    @OneToMany(mappedBy = "step")
    private List<StepField_r> stepFields;  // The collection of step fields associated with this step

    @Column(name = "maxDays",nullable = true)
    private int maxDays;

    @OneToMany()
    private List<ContratStepEntryDate> entryDates;

    public Step(Long stepID, String stepName) {
        this.stepID = stepID;
        this.stepName = stepName;
    }
}