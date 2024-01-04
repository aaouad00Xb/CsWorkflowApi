package com.example.workflow.Notifications;

import com.example.workflow.Entities.User.User;
import com.example.workflow.Repositories.NotificationRepo;
import com.example.workflow.Repositories.UserRepository;
import com.example.workflow.Services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

@Component
@EnableScheduling
public class Scheduler {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private NotificationRepo notificationRepo;
    private UserRepository userRepo;
    private EmailService emailService;


    @Autowired
    public Scheduler(NotificationRepo notificationRepo, UserRepository userRepo, EmailService emailService) {
        this.notificationRepo = notificationRepo;
        this.userRepo = userRepo;
        this.emailService = emailService;
    }

    @Scheduled(cron = "0 0 0 * * MON")
//    @Scheduled(cron = "*/10 * * * * *")
    public void run() {


//        for(User ele : userRepo.findAll()){
//            System.out.println(ele);
//            if(ele.getProfil() != null){
//                for(Habilitation ha : ele.getProfil().getHabilitations()){
//                    if(ha.getName().equals("insert")){
//                        Notification_content n = notification_content_repo.getNotification_content("tuyaux_Semaine");
//                        if(ele.getNotifications() != null){
//                            System.out.println(ele.getNotifications());
//                            ele.getNotifications().add(Notification.builder().intitule(n.getIntitule()).content(n.getContent()).type("warning").sended_by("system").sended_at(new Date()).read(false).build());
//
//                        }else {
//                            List<Notification> notificationList = new ArrayList<>();
//                            notificationList.add(Notification.builder().intitule(n.getIntitule()).content(n.getContent()).type("warning").sended_by("system").sended_at(new Date()).read(false).build());
//                            ele.setNotifications(notificationList);
//                        }
//                        userRepo.save(ele);
//                        if(ele.getEmail() != null){
//                            EmailDetails emailDetails = EmailDetails.builder().recipient(ele.getEmail()).subject(n.getIntitule()).msgBody(n.getContent()).build();
//                            String status = emailService.sendSimpleMail(emailDetails);
//                            System.out.println(status);
//                        }
//                    }
//                }
//                }
//                }


    }




}
