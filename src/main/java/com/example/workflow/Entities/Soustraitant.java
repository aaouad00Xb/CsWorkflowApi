package com.example.workflow.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Soustraitant extends  BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(columnDefinition = "Text")
    private String name;
    private String rc;
    private String cin;
    @Column(columnDefinition = "Text")
    private String address;
    private String tele;
    private String email;
    private String domainActivity;
    private Date dateAncient;
    @Column(columnDefinition = "boolean default false")
    private boolean patente;
    @Column(columnDefinition = "Text")
    private String commentaire;
}
