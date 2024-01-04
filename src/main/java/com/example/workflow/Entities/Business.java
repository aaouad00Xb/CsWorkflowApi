package com.example.workflow.Entities;

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
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "businessID")
public class Business extends  BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long businessID;
    private String objet;
    private String codeAffaire;
    private String mo;
    private String contactPerson;
    private String contactEmail;
    private String contactPhone;
    private Double BudgetTotal;

    private Date dateBuisnes;  // Date when the business record was created

    @ManyToOne
    private Division division;  // Many businesses are associated with one pole

    @OneToMany(cascade = CascadeType.MERGE)
    @JoinColumn(name = "business")
    private List<ContratSousTraitance> contratSousTraitances;



//    @ManyToMany
//    @JoinTable(name = "business_users",
//            joinColumns = @JoinColumn(name = "business_id"),
//            inverseJoinColumns = @JoinColumn(name = "user_id"))
//    @JsonIgnoreProperties({"myBuinesses"})
//    private List<User> userList;


}
