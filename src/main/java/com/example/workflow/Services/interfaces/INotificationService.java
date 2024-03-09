package com.example.workflow.Services.interfaces;

import com.example.workflow.Dto.UserDtoResp;
import com.example.workflow.Entities.ContratSousTraitance;
import com.example.workflow.Entities.Facture;
import com.example.workflow.Entities.Notification;

import java.util.List;

public interface INotificationService {
    void notifyInitContrat(ContratSousTraitance contrat);

    void notifyInitFacture(Facture facture);

    void notifyProveContrat(ContratSousTraitance contrat);

    void notifyProveFacture(Facture facture);

    void notifyValidContrat(ContratSousTraitance contrat);

    void notifyValidFacture(Facture facture);

    void notifydisProveContrat(ContratSousTraitance contrat);

    Notification createNotificationFordisproveUser(UserDtoResp user, ContratSousTraitance contrat);

    List<Notification> findUnreadNotificationsByUser(String userName);
}
