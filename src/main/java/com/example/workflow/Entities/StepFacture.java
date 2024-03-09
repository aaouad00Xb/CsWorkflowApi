package com.example.workflow.Entities;

import com.example.workflow.Entities.User.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class StepFacture extends  BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stepID;
    private String stepName;
    @Column(columnDefinition = "TEXT")
    private String stepDescription;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @JsonIgnoreProperties({"stepDescription","nextStep","previousStep","entryDates"})
    @ManyToOne
    private StepFacture nextStep;  // The next step

    @JsonIgnoreProperties({"stepDescription","nextStep","previousStep","entryDates"})
    @ManyToOne
    private StepFacture previousStep;  // The previous step

    @JsonIgnore
    @OneToMany(mappedBy = "step")
    private List<StepFieldF_r> stepFields;  // The collection of step fields associated with this step

    @Column(name = "maxDays",nullable = true)
    private Integer maxDays;

    @OneToMany()
    private List<ContratStepEntryDate> entryDates;

    public StepFacture(Long stepID, String stepName) {
        this.stepID = stepID;
        this.stepName = stepName;
    }
}