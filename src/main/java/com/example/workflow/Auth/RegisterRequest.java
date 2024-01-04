package com.example.workflow.Auth;

import com.example.workflow.Entities.Division;
import com.example.workflow.Entities.User.User;
import com.example.workflow.Entities.User.UserRole;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class RegisterRequest {

        @NotEmpty(message = "You should insert a name")
        private String name;
        @NotEmpty(message = "You should insert a username")
        private String username;

        private String bio;

        @NotEmpty(message = "Email address can not be a null or empty")
        @Email(message = "Email address should be a valid value")
        private String email;
        @NotEmpty(message = "You should insert a password")
        private String password;
        @NotEmpty(message = "You should insert a confirm_password")
        private String confirm_password;
        // Other user-related attributes


        private Long manager; // Self-referential relationship to represent the manager of a user

        @NotNull(message = "You should insert a role")
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