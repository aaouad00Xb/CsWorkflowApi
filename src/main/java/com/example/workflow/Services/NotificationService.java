package com.example.workflow.Services;

import com.example.workflow.Dto.ContratSoutraitanceResponseDto;
import com.example.workflow.Dto.FactureResponseDto;
import com.example.workflow.Dto.UserDtoResp;
import com.example.workflow.Entities.ContratSousTraitance;
import com.example.workflow.Entities.Division;
import com.example.workflow.Entities.Facture;
import com.example.workflow.Entities.Notification;
import com.example.workflow.Entities.User.UserRole;
import com.example.workflow.Enums.NotificationType;
import com.example.workflow.Repositories.NotificationRepo;
import com.example.workflow.Services.interfaces.IFactureService;
import com.example.workflow.Services.interfaces.INotificationService;
import com.example.workflow.Services.interfaces.IcontratService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.catalina.User;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class NotificationService implements INotificationService {


    private final NotificationRepo notificationRepository;
    private final EmailService emailService;


    private final IcontratService icontratService;

    private final IFactureService iFactureService;
    private final ObjectMapper objectMapper;

    private final UserService userService;

    public NotificationService(NotificationRepo notificationRepository, EmailService emailService, @Lazy IcontratService icontratService, IFactureService iFactureService, ObjectMapper objectMapper, UserService userService) {
        this.notificationRepository = notificationRepository;
        this.emailService = emailService;
        this.icontratService = icontratService;
        this.iFactureService = iFactureService;
        this.objectMapper = objectMapper;
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

                emailService.sendSimpleMail(notification.getContent(), notification.getIntitule(), notification.getReceiver());

            }

            User projectManager = null;

            if(!UserRole.DIVISION_MANAGER.equals(contrat.getProjectManager().getRole()) && !UserRole.PROJECT_MANAGER.equals(contrat.getProjectManager().getRole()) ){
                Notification notification = createNotificationForUser(objectMapper.convertValue(contrat.getProjectManager(),UserDtoResp.class), contrat);
                // Save the notification to the database or send it via a messaging system
                notificationRepository.save(notification);
                emailService.sendSimpleMail(notification.getContent(), notification.getIntitule(), notification.getReceiver());

            }


            List<UserDtoResp> concernedUser = userService.findByDivisionAndRole(contratDivision, contrat.getCurrentStep().getUserRole());

//            if(!concernedUser.isEmpty()){
//                Notification notification = createNotificationCstBelongsTOme(concernedUser.get(0), contrat);
//                notificationRepository.save(notification);
//                emailService.sendSimpleMail(notification.getContent(), notification.getIntitule(), notification.getReceiver());
//
//            }

            if(!concernedUser.isEmpty()){
                concernedUser.stream().forEach(user -> {
                    Notification notification = createNotificationCstBelongsTOme(user, contrat);
                    notificationRepository.save(notification);
                    emailService.sendSimpleMail(notification.getContent(), notification.getIntitule(), notification.getReceiver());
                });


            }else{
                System.out.println("makayn");

                concernedUser = userService.getUserByRole(contrat.getCurrentStep().getUserRole());
                for(UserDtoResp u: concernedUser){
                    Notification notification = createNotificationCstBelongsTOme(u, contrat);

                    notificationRepository.save(notification);
                    emailService.sendSimpleMail(notification.getContent(), notification.getIntitule(), notification.getReceiver());
                }
            }



            for (UserDtoResp user : usersInDivision) {
                Notification notification = createNotificationForUser(user, contrat);
                // Save the notification to the database or send it via a messaging system
                notificationRepository.save(notification);
                emailService.sendSimpleMail(notification.getContent(), notification.getIntitule(), notification.getReceiver());

            }
        }catch (Exception e){
            System.out.println(e);
        }

    }



    @Override
    public void notifyInitFacture(Facture facture) {

        try{
            Division contratDivision = facture.getContratSousTraitance().getDivision();

            List<UserDtoResp> usersInDivision = userService.findByDivision(contratDivision);
            List<UserDtoResp> chefPole = userService.findByDivisionAndRole(contratDivision, UserRole.DP);
            if(!chefPole.isEmpty()){
                Notification notification = createNotificationForUser(chefPole.get(0), facture);
                notificationRepository.save(notification);

                emailService.sendSimpleMail(notification.getContent(), notification.getIntitule(), notification.getReceiver());

            }

            User projectManager = null;

            if(!UserRole.DIVISION_MANAGER.equals(facture.getContratSousTraitance().getProjectManager().getRole()) && !UserRole.PROJECT_MANAGER.equals(facture.getContratSousTraitance().getProjectManager().getRole()) ){
                Notification notification = createNotificationForUser(objectMapper.convertValue(facture.getContratSousTraitance().getProjectManager(),UserDtoResp.class), facture);
                // Save the notification to the database or send it via a messaging system
                notificationRepository.save(notification);
                emailService.sendSimpleMail(notification.getContent(), notification.getIntitule(), notification.getReceiver());

            }


            List<UserDtoResp> concernedUser = userService.findByDivisionAndRole(contratDivision, facture.getCurrentStep().getUserRole());

            if(!concernedUser.isEmpty()){
//                Notification notification = createNotificationCstBelongsTOme(concernedUser.get(0), facture);
//                notificationRepository.save(notification);
//                emailService.sendSimpleMail(notification.getContent(), notification.getIntitule(), notification.getReceiver());



                    concernedUser.stream().forEach(user -> {
                        Notification notification = createNotificationCstBelongsTOme(user, facture);
                        notificationRepository.save(notification);
                        emailService.sendSimpleMail(notification.getContent(), notification.getIntitule(), notification.getReceiver());
                    });

            }else{
                concernedUser = userService.getUserByRole(facture.getCurrentStep().getUserRole());
                for(UserDtoResp u: concernedUser){
                    Notification notification = createNotificationCstBelongsTOme(u, facture);

                    notificationRepository.save(notification);
                    emailService.sendSimpleMail(notification.getContent(), notification.getIntitule(), notification.getReceiver());
                }
            }



            for (UserDtoResp user : usersInDivision) {
                Notification notification = createNotificationForUser(user, facture);
                // Save the notification to the database or send it via a messaging system
                notificationRepository.save(notification);
                emailService.sendSimpleMail(notification.getContent(), notification.getIntitule(), notification.getReceiver());

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


    private Notification createNotificationForUser(UserDtoResp user, Facture facture) {
        // Create a new notification for the user
        Notification notification = new Notification();
        notification.setIntitule(facture.getFactureNumber() + " a été bien crée");
        notification.setContent("Un nouveau facture a été créé.");
        notification.setSended_by("System");
        notification.setFactureID(facture.getFactureID());
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

            if(!UserRole.DIVISION_MANAGER.equals(contrat.getProjectManager().getRole()) && !UserRole.PROJECT_MANAGER.equals(contrat.getProjectManager().getRole()) ){
                Notification notification = createNotificationForproveUser(objectMapper.convertValue(contrat.getProjectManager(),UserDtoResp.class), contrat);
                // Save the notification to the database or send it via a messaging system
                notificationRepository.save(notification);

            }

            //notify me if its belong to me
            //todo
            List<UserDtoResp> concernedUser = userService.findByDivisionAndRole(contratDivision, contrat.getCurrentStep().getUserRole());

//            if(!concernedUser.isEmpty()){
//                Notification notification = createNotificationCstBelongsTOme(concernedUser.get(0), contrat);
//                notificationRepository.save(notification);
//                emailService.sendSimpleMail(notification.getContent(), notification.getIntitule(), notification.getReceiver());
//
//            }

            if(!concernedUser.isEmpty()){
                concernedUser.stream().forEach(user -> {
                    Notification notification = createNotificationCstBelongsTOme(user, contrat);
                    notificationRepository.save(notification);
                    emailService.sendSimpleMail(notification.getContent(), notification.getIntitule(), notification.getReceiver());
                });
            }else{
                System.out.println("makayn");

                concernedUser = userService.getUserByRole(contrat.getCurrentStep().getUserRole());
                for(UserDtoResp u: concernedUser){
                    Notification notification = createNotificationCstBelongsTOme(u, contrat);

                    notificationRepository.save(notification);
                    emailService.sendSimpleMail(notification.getContent(), notification.getIntitule(), notification.getReceiver());
                }
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
    public void notifyProveFacture(Facture facture) {

        System.out.println("notifyProveFacture");
        try{
            Division contratDivision = facture.getContratSousTraitance().getDivision();

            System.out.println(contratDivision.getDivisionName());

            List<UserDtoResp> usersInDivision = userService.findByDivision(contratDivision);
            List<UserDtoResp> chefPole = userService.findByDivisionAndRole(contratDivision, UserRole.DP);

            if(!chefPole.isEmpty()){
                Notification notification = createNotificationForproveUser(chefPole.get(0), facture);
                notificationRepository.save(notification);
            }

            if(!UserRole.DIVISION_MANAGER.equals(facture.getContratSousTraitance().getProjectManager().getRole()) && !UserRole.PROJECT_MANAGER.equals(facture.getContratSousTraitance().getProjectManager().getRole()) ){
                Notification notification = createNotificationForproveUser(objectMapper.convertValue(facture.getContratSousTraitance().getProjectManager(),UserDtoResp.class), facture);
                // Save the notification to the database or send it via a messaging system
                notificationRepository.save(notification);

            }

            //notify me if its belong to me
            //todo
            List<UserDtoResp> concernedUser = userService.findByDivisionAndRole(contratDivision, facture.getCurrentStep().getUserRole());

            System.out.println("concerned user");

            if(!concernedUser.isEmpty()){
                System.out.println("kayn");
//                Notification notification = createNotificationCstBelongsTOme(concernedUser.get(0), facture);
//                notificationRepository.save(notification);
//                emailService.sendSimpleMail(notification.getContent(), notification.getIntitule(), notification.getReceiver());

                concernedUser.stream().forEach(user -> {
                    Notification notification = createNotificationCstBelongsTOme(user, facture);
                    notificationRepository.save(notification);
                    emailService.sendSimpleMail(notification.getContent(), notification.getIntitule(), notification.getReceiver());
                });

            }else{
                System.out.println("makayn");

                concernedUser = userService.getUserByRole(facture.getCurrentStep().getUserRole());
                for(UserDtoResp u: concernedUser){
                    Notification notification = createNotificationCstBelongsTOme(u, facture);

                    notificationRepository.save(notification);
                    emailService.sendSimpleMail(notification.getContent(), notification.getIntitule(), notification.getReceiver());
                }
            }


            for (UserDtoResp user : usersInDivision) {
                Notification notification = createNotificationForproveUser(user, facture);
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
                emailService.sendSimpleMail(notification.getContent(), notification.getIntitule(), notification.getReceiver());


            }

            if(!UserRole.DIVISION_MANAGER.equals(contrat.getProjectManager().getRole()) && !UserRole.PROJECT_MANAGER.equals(contrat.getProjectManager().getRole()) ){
                Notification notification = createNotificationForValidUser(objectMapper.convertValue(contrat.getProjectManager(),UserDtoResp.class), contrat);
                // Save the notification to the database or send it via a messaging system
                notificationRepository.save(notification);
                emailService.sendSimpleMail(notification.getContent(), notification.getIntitule(), notification.getReceiver());

            }


            for (UserDtoResp user : usersInDivision) {
                Notification notification = createNotificationForValidUser(user, contrat);
                // Save the notification to the database or send it via a messaging system
                notificationRepository.save(notification);
                emailService.sendSimpleMail(notification.getContent(), notification.getIntitule(), notification.getReceiver());

            }
        }catch (Exception e){
            System.out.println(e);
        }

    }
@Override
    public void notifyValidFacture(Facture facture) {
        try{
            Division contratDivision = facture.getContratSousTraitance().getDivision();

            List<UserDtoResp> usersInDivision = userService.findByDivision(contratDivision);
            List<UserDtoResp> chefPole = userService.findByDivisionAndRole(contratDivision, UserRole.DP);
            if(!chefPole.isEmpty()){
                Notification notification = createNotificationForValidUser(chefPole.get(0), facture);
                notificationRepository.save(notification);
                emailService.sendSimpleMail(notification.getContent(), notification.getIntitule(), notification.getReceiver());


            }

            if(!UserRole.DIVISION_MANAGER.equals(facture.getContratSousTraitance().getProjectManager().getRole()) && !UserRole.PROJECT_MANAGER.equals(facture.getContratSousTraitance().getProjectManager().getRole()) ){
                Notification notification = createNotificationForValidUser(objectMapper.convertValue(facture.getContratSousTraitance().getProjectManager(),UserDtoResp.class), facture);
                // Save the notification to the database or send it via a messaging system
                notificationRepository.save(notification);
                emailService.sendSimpleMail(notification.getContent(), notification.getIntitule(), notification.getReceiver());

            }


            for (UserDtoResp user : usersInDivision) {
                Notification notification = createNotificationForValidUser(user, facture);
                // Save the notification to the database or send it via a messaging system
                notificationRepository.save(notification);
                emailService.sendSimpleMail(notification.getContent(), notification.getIntitule(), notification.getReceiver());

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


    private Notification createNotificationForproveUser(UserDtoResp user, Facture facture) {
        // Create a new notification for the user
        Notification notification = new Notification();
        notification.setIntitule("la facure N° "+facture.getFactureNumber() + " a été passée vers l'etape " + facture.getCurrentStep().getStepName());
        notification.setContent("nouvelle situation, à consulter ");
        notification.setSended_by("System");
        notification.setFactureID(facture.getFactureID());
        notification.setReceiver(user.getUsername());
        notification.setRead(false);
        notification.setType(NotificationType.prove);
        return notification;
    }
    public Notification createNotificationCstBelongsTOme(UserDtoResp user, ContratSousTraitance contrat) {
        // Create a new notification for the user
        Notification notification = new Notification();
        notification.setIntitule(contrat.getContratNumber() + " en attend votre validation " );
        notification.setContent("un contrat en attente, à consulter ");
        notification.setSended_by("System");
        notification.setContratID(contrat.getContratID());
        notification.setReceiver(user.getUsername());
        notification.setRead(false);
        notification.setType(NotificationType.prove);
        return notification;
    }

    public Notification createNotificationCstBelongsTOme(UserDtoResp user, Facture facture) {
        // Create a new notification for the user
        Notification notification = new Notification();
        notification.setIntitule(facture.getContratSousTraitance().getContratNumber() + " en attend votre validation " );
        notification.setContent("un facture en attente, à consulter ");
        notification.setSended_by("System");
        notification.setContratID(facture.getContratSousTraitance().getContratID());
        notification.setReceiver(user.getUsername());
        notification.setRead(false);
        notification.setType(NotificationType.prove);
        return notification;
    }






    public Notification createNotificationForValidUser(UserDtoResp user, ContratSousTraitance contrat) {
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


    public Notification createNotificationForValidUser(UserDtoResp user, Facture facture) {
        // Create a new notification for the user
        Notification notification = new Notification();
        notification.setIntitule("la facture N° " + facture.getFactureNumber() + " a été Validée par le DG");
        notification.setContent("la facture N° " + facture.getFactureNumber() + " a été Validée par le DG");
        notification.setSended_by("System");
        notification.setFactureID(facture.getFactureID());
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

            if(!UserRole.DIVISION_MANAGER.equals(contrat.getProjectManager().getRole()) && !UserRole.PROJECT_MANAGER.equals(contrat.getProjectManager().getRole()) ){
                Notification notification = createNotificationFordisproveUser(objectMapper.convertValue(contrat.getProjectManager(),UserDtoResp.class), contrat);
                // Save the notification to the database or send it via a messaging system
                notificationRepository.save(notification);

            }

            List<UserDtoResp> concernedUser = userService.findByDivisionAndRole(contratDivision, contrat.getCurrentStep().getUserRole());

//            if(!concernedUser.isEmpty()){
//
//                Notification notification = createNotificationCstBelongsTOme(concernedUser.get(0), contrat);
//                notificationRepository.save(notification);
//                emailService.sendSimpleMail(notification.getContent(), notification.getIntitule(), notification.getReceiver());
//
//            }

            if(!concernedUser.isEmpty()){
                concernedUser.stream().forEach(user -> {
                    Notification notification = createNotificationCstBelongsTOme(user, contrat);
                    notificationRepository.save(notification);
                    emailService.sendSimpleMail(notification.getContent(), notification.getIntitule(), notification.getReceiver());
                });


            }else{
                System.out.println("makayn");

                concernedUser = userService.getUserByRole(contrat.getCurrentStep().getUserRole());
                for(UserDtoResp u: concernedUser){
                    Notification notification = createNotificationCstBelongsTOme(u, contrat);

                    notificationRepository.save(notification);
                    emailService.sendSimpleMail(notification.getContent(), notification.getIntitule(), notification.getReceiver());
                }
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
        List<FactureResponseDto> mesFactures = iFactureService.getFacturesByUserRoleAndDivision();

        for (Notification n : notifications) {
            boolean isForMe = false;
            if(n.getContratID()!= null){
                for (ContratSoutraitanceResponseDto c : mesContrat) {
                    if (Objects.equals(n.getContratID(), c.getContratID())) {
                        isForMe = true;
                        break;
                    }
                }
            }else if(n.getFactureID() != null){
                System.out.println("hana kanchouf");
                for (FactureResponseDto c : mesFactures) {
                    if (Objects.equals(n.getFactureID(), c.getFactureID())) {
                        isForMe = true;
                        break;
                    }
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
