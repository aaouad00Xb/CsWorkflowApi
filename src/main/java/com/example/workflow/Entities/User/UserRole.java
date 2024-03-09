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
    ADMIN("Administrator"),
    ASSISTANTE_POLE("Assistante pole"),
    COMPTABILITE("comptabilite");
    private String displayName;

}
