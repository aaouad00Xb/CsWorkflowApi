package com.example.workflow.Services;

import com.example.workflow.Entities.User.User;
import com.example.workflow.Entities.User.UserRole;
import com.example.workflow.Repositories.UserRepository;
import com.example.workflow.Services.impl.IEmailService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService implements IEmailService {

    private final JavaMailSender javaMailSender;
    private final UserRepository userRepository;

    @Autowired
    public EmailService(JavaMailSender javaMailSender, UserRepository userRepository) {
        this.javaMailSender = javaMailSender;
        this.userRepository = userRepository;
    }

    public void sendRegistrationConfirmationEmail(String recipientEmail, String confirmationToken) {
        // Create the verification link using the confirmation token
        String verificationLink = "http://localhost:8084/verify?token=" + confirmationToken;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);
        message.setSubject("Registration Confirmation");
        message.setText("Dear User, thank you for registering. Please click on the link below to confirm your registration:\n\n"
                + verificationLink);

        javaMailSender.send(message);
    }


    public void sendConfirmationEmail(String recipientEmail) {
        // Create the verification link using the confirmation token
        String verificationLink = "http://localhost:4200/login";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);
        message.setSubject("Account Confirmation");
        message.setText("Dear User, your registration is been preccesed by our team, and your request has been approuved:\n\n"
                + verificationLink);

        javaMailSender.send(message);
    }


    public void sendreclamationEmail(String reclamation_body, String reclamation_owner) {
        // Create the verification link using the confirmation token
        String verificationLink = "http://localhost:4200/login";

        SimpleMailMessage message = new SimpleMailMessage();
        List<User> users = this.userRepository.findAll();
        for(User u :users){
            if(u.getEmail() != null && u.getRole() != null){

                if(u.getRole().equals(UserRole.ADMIN)){
                    message.setTo(u.getEmail());
                    message.setSubject("Nouvelle réclamation par " + reclamation_owner);
                    message.setText("Dear Admin,  réclamation is been added by the user, and u have to proccess it:\n\n Le core du réclamation s'agit de \n"+reclamation_body+"\n"
                            + verificationLink);

                    javaMailSender.send(message);
                }
            }
        }

    }


//    public void sendComplementEmail(Long demandId,List<Complement> complements) {
//        // Create the verification link using the confirmation token
//        String verificationLink = "http://localhost:4200/login";
//
//        SimpleMailMessage message = new SimpleMailMessage();
//        List<User> users = this.userRepository.findAll();
//        for(User u :users){
//            if(u.getEmail() != null && u.getRole() != null){
//
//                if(u.getRole().equals(Role.Admin)){
//                    message.setTo(u.getEmail());
//                    message.setSubject("Nouvelle demande de complément pour le dossier N` " + demandId);
//
//                    String complement_body = "";
//
//                    for (Complement complement : complements) {
//                        complement_body = complement_body + "\n le complements : " + complement.getComplementType().getName() + "\n motif : " + complement.getMotif();
//                    }
//
//                    message.setText(complement_body);
//
//                    javaMailSender.send(message);
//                }
//            }
//        }
//
//    }

//    public void sendComplementReceivedEmail(String demandId,Complement complement) {
//        // Create the verification link using the confirmation token
//        String verificationLink = "http://localhost:4200/login";
//
//        SimpleMailMessage message = new SimpleMailMessage();
//        List<User> users = this.userRepository.findAll();
//        for(User u :users){
//            if(u.getEmail() != null && u.getRole() != null){
//
//                if(u.getRole().equals(Role.Admin)){
//                    message.setTo(u.getEmail());
//                    message.setSubject("Nouveau complément pour le dossier N` " + demandId);
//
//                    String complement_body = "";
//
//
//                        complement_body = complement_body + "\n le complements : " + complement.getComplementType().getName() + "\n motif : " + complement.getMotif();
//
//
//                    message.setText(complement_body);
//
//                    javaMailSender.send(message);
//                }
//            }
//        }
//
//    }

}