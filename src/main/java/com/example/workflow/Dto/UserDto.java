package com.example.workflow.Dto;

import com.example.workflow.Entities.Division;
import com.example.workflow.Entities.User.User;
import com.example.workflow.Entities.User.UserRole;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {

    private String name;
    private String username;

    private String bio;

    private String email;
    private String password;
    private String confirm_password;
    // Other user-related attributes


    private Long manager; // Self-referential relationship to represent the manager of a user

    private UserRole role;



    private Long division; // The division to which the user belongs
    private Long poles; // The division to which the user belongs

    private boolean confirmed;


    private boolean mailconfirmed;
    private String confirmationToken;

//    notification
    //    Notifications relatives à la durée

    private boolean nearDeadLine;

    private boolean deadLines;

    //    Notifications relatives à l'hiérarchie

    private boolean notifySysuperior;


    //   Notifications générales

    private boolean facturePaidDone;


    private boolean newFacutresTovalidate;

}
