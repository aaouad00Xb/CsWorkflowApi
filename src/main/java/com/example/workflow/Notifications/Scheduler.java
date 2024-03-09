package com.example.workflow.Notifications;

import com.example.workflow.Dto.UserDtoResp;
import com.example.workflow.Entities.ContratSousTraitance;
import com.example.workflow.Entities.Division;
import com.example.workflow.Entities.Facture;
import com.example.workflow.Entities.Notification;
import com.example.workflow.Repositories.ContratSoustraitanceRepo;
import com.example.workflow.Repositories.FactureRepository;
import com.example.workflow.Repositories.NotificationRepo;
import com.example.workflow.Repositories.UserRepository;
import com.example.workflow.Services.EmailService;
import com.example.workflow.Services.NotificationService;
import com.example.workflow.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.List;

@Component
@EnableScheduling
public class Scheduler {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private final NotificationRepo notificationRepository;
    private UserRepository userRepo;
    private final  EmailService emailService;

    private final ContratSoustraitanceRepo contratSoustraitanceRepo;
    private final FactureRepository factureRepository;

    private final NotificationService notificationService;

    private final UserService userService;


    @Autowired
    public Scheduler( NotificationRepo notificationRepository, UserRepository userRepo, EmailService emailService, ContratSoustraitanceRepo contratSoustraitanceRepo, FactureRepository factureRepository, NotificationService notificationService, UserService userService) {
        this.notificationRepository = notificationRepository;
        this.userRepo = userRepo;
        this.emailService = emailService;
        this.contratSoustraitanceRepo = contratSoustraitanceRepo;
        this.factureRepository = factureRepository;
        this.notificationService = notificationService;
        this.userService = userService;
    }

    @Scheduled(cron = "0 0 8 * * MON-FRI")
//    @Scheduled(cron = "0 * * * * *")
    public void run() {

        List<ContratSousTraitance> contratSousTraitances =  contratSoustraitanceRepo.findInvalidContrat();

        contratSousTraitances.stream().forEach(
                contratSousTraitance -> {
                    Division contratDivision = contratSousTraitance.getDivision();
                    List<UserDtoResp> concernedUser = userService.findByDivisionAndRole(contratDivision, contratSousTraitance.getCurrentStep().getUserRole());

                    if(!concernedUser.isEmpty()){
                        concernedUser.stream().forEach(user -> {
                            Notification notification = notificationService.createNotificationCstBelongsTOme(user, contratSousTraitance);
                            notificationRepository.save(notification);
                            emailService.sendSimpleMail(notification.getContent(), notification.getIntitule(), notification.getReceiver());
                        });


                    }else{
                        System.out.println("makayn");

                        concernedUser = userService.getUserByRole(contratSousTraitance.getCurrentStep().getUserRole());
                        for(UserDtoResp u: concernedUser){
                            Notification notification = notificationService.createNotificationCstBelongsTOme(u, contratSousTraitance);

                            notificationRepository.save(notification);
                            emailService.sendSimpleMail(notification.getContent(), notification.getIntitule(), notification.getReceiver());
                        }
                    }
                }
        );



        List<Facture> factures =  factureRepository.findInvalidFacture();

        factures.stream().forEach(
                facture -> {
                    Division contratDivision = facture.getContratSousTraitance().getDivision();
                    List<UserDtoResp> concernedUser = userService.findByDivisionAndRole(contratDivision, facture.getCurrentStep().getUserRole());

                    if(!concernedUser.isEmpty()){
                        concernedUser.stream().forEach(user -> {
                            Notification notification = notificationService.createNotificationCstBelongsTOme(user, facture);
                            notificationRepository.save(notification);
                            emailService.sendSimpleMail(notification.getContent(), notification.getIntitule(), notification.getReceiver());
                        });


                    }else{
                        System.out.println("makayn");

                        concernedUser = userService.getUserByRole(facture.getCurrentStep().getUserRole());
                        for(UserDtoResp u: concernedUser){
                            Notification notification = notificationService.createNotificationCstBelongsTOme(u, facture);

                            notificationRepository.save(notification);
                            emailService.sendSimpleMail(notification.getContent(), notification.getIntitule(), notification.getReceiver());
                        }
                    }
                }
        );



    }




}
