package com.example.workflow.Services;

import com.example.workflow.Dto.ContratSoutraitanceResponseDto;
import com.example.workflow.Dto.UserDtoResp;
import com.example.workflow.Entities.ContratSousTraitance;
import com.example.workflow.Entities.Division;
import com.example.workflow.Entities.Notification;
import com.example.workflow.Entities.User.UserRole;
import com.example.workflow.Enums.NotificationType;
import com.example.workflow.Repositories.NotificationRepo;
import com.example.workflow.Services.impl.INotificationService;
import com.example.workflow.Services.impl.IcontratService;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class NotificationService implements INotificationService {


    private final NotificationRepo notificationRepository;


    private final IcontratService icontratService;

    private final UserService userService;

    public NotificationService(NotificationRepo notificationRepository, @Lazy IcontratService icontratService, UserService userService) {
        this.notificationRepository = notificationRepository;
        this.icontratService = icontratService;
        this.userService = userService;
    }

    public List<Notification> getNotificationsForUser(String receiver) {
        return notificationRepository.findByReceiverAndReadFalseOrderByCreatedAtDesc(receiver);
    }

    public Notification saveNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

   @Override
    public void notifyInitContrat(ContratSousTraitance contrat) {

        try{
            Division contratDivision = contrat.getDivision();

            List<UserDtoResp> usersInDivision = userService.findByDivision(contratDivision);
            List<UserDtoResp> chefPole = userService.findByDivisionAndRole(contratDivision, UserRole.DP);
            if(!chefPole.isEmpty()){
                Notification notification = createNotificationForUser(chefPole.get(0), contrat);
                notificationRepository.save(notification);

            }


            for (UserDtoResp user : usersInDivision) {
                Notification notification = createNotificationForUser(user, contrat);
                // Save the notification to the database or send it via a messaging system
                notificationRepository.save(notification);
            }
        }catch (Exception e){
            System.out.println(e);
        }

    }

    private Notification createNotificationForUser(UserDtoResp user, ContratSousTraitance contrat) {
        // Create a new notification for the user
        Notification notification = new Notification();
        notification.setIntitule(contrat.getContratNumber() + " a été bien crée");
        notification.setContent("Un nouveau contrat a été créé.");
        notification.setSended_by("System");
        notification.setContratID(contrat.getContratID());
        notification.setReceiver(user.getUsername());
        notification.setRead(false);
        notification.setType(NotificationType.init);
        return notification;
    }



    @Override
    public void notifyProveContrat(ContratSousTraitance contrat) {
        try{
            Division contratDivision = contrat.getDivision();

            List<UserDtoResp> usersInDivision = userService.findByDivision(contratDivision);
            List<UserDtoResp> chefPole = userService.findByDivisionAndRole(contratDivision, UserRole.DP);
            if(!chefPole.isEmpty()){
                Notification notification = createNotificationForproveUser(chefPole.get(0), contrat);
                notificationRepository.save(notification);

            }


            for (UserDtoResp user : usersInDivision) {
                Notification notification = createNotificationForproveUser(user, contrat);
                // Save the notification to the database or send it via a messaging system
                notificationRepository.save(notification);
            }
        }catch (Exception e){
            System.out.println(e);
        }

    }


    @Override
    public void notifyValidContrat(ContratSousTraitance contrat) {
        try{
            Division contratDivision = contrat.getDivision();

            List<UserDtoResp> usersInDivision = userService.findByDivision(contratDivision);
            List<UserDtoResp> chefPole = userService.findByDivisionAndRole(contratDivision, UserRole.DP);
            if(!chefPole.isEmpty()){
                Notification notification = createNotificationForValidUser(chefPole.get(0), contrat);
                notificationRepository.save(notification);

            }


            for (UserDtoResp user : usersInDivision) {
                Notification notification = createNotificationForValidUser(user, contrat);
                // Save the notification to the database or send it via a messaging system
                notificationRepository.save(notification);
            }
        }catch (Exception e){
            System.out.println(e);
        }

    }


    private Notification createNotificationForproveUser(UserDtoResp user, ContratSousTraitance contrat) {
        // Create a new notification for the user
        Notification notification = new Notification();
        notification.setIntitule(contrat.getContratNumber() + " a été passée vers l'etape " + contrat.getCurrentStep().getStepName());
        notification.setContent("nouvelle situation, à consulter ");
        notification.setSended_by("System");
        notification.setContratID(contrat.getContratID());
        notification.setReceiver(user.getUsername());
        notification.setRead(false);
        notification.setType(NotificationType.prove);
        return notification;
    }



    private Notification createNotificationForValidUser(UserDtoResp user, ContratSousTraitance contrat) {
        // Create a new notification for the user
        Notification notification = new Notification();
        notification.setIntitule(contrat.getContratNumber() + " a été Validée par le DG");
        notification.setContent(contrat.getContratNumber() + " a été Validée par le DG");
        notification.setSended_by("System");
        notification.setContratID(contrat.getContratID());
        notification.setReceiver(user.getUsername());
        notification.setRead(false);
        notification.setType(NotificationType.prove);
        return notification;
    }






    @Override
    public void notifydisProveContrat(ContratSousTraitance contrat) {
        try{
            Division contratDivision = contrat.getDivision();

            List<UserDtoResp> usersInDivision = userService.findByDivision(contratDivision);
            List<UserDtoResp> chefPole = userService.findByDivisionAndRole(contratDivision, UserRole.DP);
            if(!chefPole.isEmpty()){
                Notification notification = createNotificationFordisproveUser(chefPole.get(0), contrat);
                notificationRepository.save(notification);

            }


            for (UserDtoResp user : usersInDivision) {
                Notification notification = createNotificationFordisproveUser(user, contrat);
                // Save the notification to the database or send it via a messaging system
                notificationRepository.save(notification);
            }
        }catch (Exception e){
            System.out.println(e);
        }

    }

    @Override
    public Notification createNotificationFordisproveUser(UserDtoResp user, ContratSousTraitance contrat) {
        // Create a new notification for the user
        Notification notification = new Notification();
        notification.setIntitule(contrat.getContratNumber() + " a été reenvoyé vers l'étape " + contrat.getCurrentStep().getStepName());
        notification.setContent("nouvelle situation, à consulter ");
        notification.setSended_by("System");
        notification.setContratID(contrat.getContratID());
        notification.setReceiver(user.getUsername());
        notification.setRead(false);
        notification.setType(NotificationType.disprove);
        return notification;
    }







    @Override
    public List<Notification> findUnreadNotificationsByUser(String userName) {
        List<Notification> notifications = notificationRepository.findByReceiverAndReadFalseOrderByCreatedAtDesc(userName);
        List<ContratSoutraitanceResponseDto> mesContrat = icontratService.getContractsByUserRoleAndDivision();

        for (Notification n : notifications) {
            boolean isForMe = false;
            for (ContratSoutraitanceResponseDto c : mesContrat) {
                if (Objects.equals(n.getContratID(), c.getContratID())) {
                    isForMe = true;
                    break;
                }
            }
            n.setForMe(isForMe);
        }
        return notifications;
    }




    public ResponseEntity<?> updateNotification(){
        return ResponseEntity.status(200).body("well saved");
    }



}
