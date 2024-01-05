package com.example.workflow.Services.impl;

import com.example.workflow.Dto.UserDtoResp;
import com.example.workflow.Dto.UserEditDto;
import com.example.workflow.Entities.Division;
import com.example.workflow.Entities.User.User;
import com.example.workflow.Entities.User.UserRole;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IUserService {
    ResponseEntity<List<User>> getUsersBySelectedRoleAndDivision(UserRole selectedRole, Long divisionId);

    List<User> getUsersWithUnconfirmedMail();

    List<User> getUsersWithConfirmed();

    User saveUser(User user);

    List<UserDtoResp> findByDivision(Division d);

    List<UserDtoResp> findByDivisionAndRole(Division d, UserRole role);

    ResponseEntity<?> getUserById(Long userId);
    List<UserDtoResp> getUsers();

    List<UserDtoResp> getchefsByDivisionID(Long divisionID);

    boolean toggleUserActiveStatus(Long userId);

    User updateUser(Long userId, UserEditDto userDTO);

}
