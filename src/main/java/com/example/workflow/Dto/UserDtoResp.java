package com.example.workflow.Dto;

import com.example.workflow.Entities.Division;
import com.example.workflow.Entities.Pole;
import com.example.workflow.Entities.User.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDtoResp {

    private Long id;
    private String name;
    private String username;

    private String bio;

    private String email;

    private UserRole role;


    private boolean isActive;

    @JsonIgnoreProperties({"pole","contratSousTraitances"})
    private Division division; // The division to which the user belongs


    @JsonIgnoreProperties({"divisions"})
    private Pole pole; // The division to which the user belongs

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
