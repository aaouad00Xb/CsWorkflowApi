package com.example.workflow.Controllers;

import com.example.workflow.Entities.Notification;
import com.example.workflow.Entities.User.User;
import com.example.workflow.Repositories.UserRepository;
import com.example.workflow.Services.interfaces.INotificationService;
import com.example.workflow.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/notifications")
@Validated
public class NotificationsController {


    @Autowired
    private INotificationService notificationService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/unread/{userId}")
    public ResponseEntity<List<Notification>> getUnreadNotificationsByUserId(@PathVariable Long userId) {

        //todo find from context user data
        System.out.println("hello");

        Optional<User> user = Optional.ofNullable(userRepository.findByIdOrderByCreatedAtDesc(userId).orElseThrow(() -> new ResourceNotFoundException("user", "userID", "" + userId)));

        List<Notification> unreadNotifications = notificationService.findUnreadNotificationsByUser(user.get().getUsername());

        return ResponseEntity.ok(unreadNotifications);
    }

}
