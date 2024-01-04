package com.example.workflow.Dto;

import com.example.workflow.Entities.User.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BusinessDTO {

    private Long businessID;
    private String businessName;
    private String businessAddress;
    private String contactPerson;
    private String contactEmail;
    private String contactPhone;
    private Double budgetTotal;
    private Date dateBusiness;
    private List<User> userList;

  }
