package com.example.workflow.Repositories;

import com.example.workflow.Entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepo extends JpaRepository<Notification,Long> {

    List<Notification> findByReceiver(String reseiver);
    List<Notification> findByReceiverAndReadFalseOrderByCreatedAtDesc(String reseiver);
}
