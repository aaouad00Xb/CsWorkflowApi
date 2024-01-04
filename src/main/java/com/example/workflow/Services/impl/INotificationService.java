package com.example.workflow.Services.impl;

import com.example.workflow.Dto.UserDtoResp;
import com.example.workflow.Entities.ContratSousTraitance;
import com.example.workflow.Entities.Notification;

import java.util.List;

public interface INotificationService {
    void notifyInitContrat(ContratSousTraitance contrat);

    void notifyProveContrat(ContratSousTraitance contrat);

    void notifyValidContrat(ContratSousTraitance contrat);

    void notifydisProveContrat(ContratSousTraitance contrat);

    Notification createNotificationFordisproveUser(UserDtoResp user, ContratSousTraitance contrat);

    List<Notification> findUnreadNotificationsByUser(String userName);
}
