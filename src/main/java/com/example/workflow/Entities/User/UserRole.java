package com.example.workflow.Entities.User;

import lombok.*;

@RequiredArgsConstructor
@Getter
@AllArgsConstructor
public enum UserRole {
    DG("Directeur Général"),
    DAF("Directeur Administratif et Financier"),
    CG("Contrôle de gestion"),
    DP("Directeur de Pôle"),
    DIVISION_MANAGER("Division Manager"),
    PROJECT_MANAGER("Project Manager"),
//    Simple_Employee("Simple employee"),
    ADMIN("Administrator");
//    COMPTABILITE("comptabilite");
    private String displayName;

}
