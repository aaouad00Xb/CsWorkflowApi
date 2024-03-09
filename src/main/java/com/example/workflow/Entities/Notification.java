package com.example.workflow.Entities;

import com.example.workflow.Entities.BaseEntity;
import com.example.workflow.Enums.NotificationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notification extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    @Enumerated(EnumType.STRING)
    private NotificationType type;
    private String intitule;
    @Column(columnDefinition = "Text")
    private String content;
    private String sended_by;
    private String receiver;
    private Date readAt;
    private Long contratID;
    private Long factureID;
    private boolean read;
    @Transient
    private boolean forMe;

}