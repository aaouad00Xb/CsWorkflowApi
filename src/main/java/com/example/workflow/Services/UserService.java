package com.example.workflow.Services;

import com.example.workflow.Dto.UserDtoResp;
import com.example.workflow.Dto.UserEditDto;
import com.example.workflow.Entities.ContratSousTraitance;
import com.example.workflow.Entities.Division;
import com.example.workflow.Entities.Pole;
import com.example.workflow.Entities.User.User;
import com.example.workflow.Entities.User.UserRole;
import com.example.workflow.Repositories.UserRepository;
import com.example.workflow.Services.impl.IUserService;
import com.example.workflow.Services.impl.IcontratService;
import com.example.workflow.exception.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final DivisionService divisionService;
        private final ObjectMapper objectMapper;

    @Autowired
    public UserService(UserRepository userRepository, DivisionService divisionService, ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.divisionService = divisionService;
        this.objectMapper = objectMapper;
    }

    @Override
    public ResponseEntity<List<User>> getUsersBySelectedRoleAndDivision(UserRole selectedRole, Long divisionId) {
        UserRole roleToSearch;
        Long divisionIdToSearch;

        // Define the logic to determine the role and division ID to search based on the selected role
        switch (selectedRole) {
//            case Simple_Employee:
//                roleToSearch = UserRole.PROJECT_MANAGER;
//                divisionIdToSearch = divisionId; // Search in the same division for Simple_Employee
//                break;
            case PROJECT_MANAGER:
                roleToSearch = UserRole.DIVISION_MANAGER;
                divisionIdToSearch = divisionId; // Search in the same division for PROJECT_MANAGER
                break;
            case DIVISION_MANAGER:
                roleToSearch = UserRole.DP;
                divisionIdToSearch = divisionId; // Search in the same division for PROJECT_MANAGER
                break;
            // Add more cases if needed for other roles
            default:
                roleToSearch = null; // Handle default case
                divisionIdToSearch = null;
        }

        if (roleToSearch != null && divisionIdToSearch != null) {
            return new ResponseEntity<List<User>>(userRepository.findByRoleAndDivisionDivisionId(roleToSearch, divisionIdToSearch), HttpStatus.OK) ;
        } else {
            // Handle the case where no matching role or division ID is found
            return null;
        }
    }

    @Override
    public List<User> getUsersWithUnconfirmedMail() {
        return userRepository.findByConfirmedFalseAndMailconfirmedTrue();
    }

    @Override
    public List<User> getUsersWithConfirmed() {
        return userRepository.findByConfirmedTrueAndMailconfirmedTrue();
    }

    @Override
    public User saveUser(User user) {
        log.info("save user ");
        return this.userRepository.save(user);
    }

    @Override
    public List<UserDtoResp> getUsers() {
//        log.info("getting users");
//        return this.userRepository.findAll();

        log.info("getting users");
        List<User> users = this.userRepository.findAll();

        // Map User entities to UserDTOs using ObjectMapper
        return users.stream()
                .map(user -> {
                    UserDtoResp converted =  objectMapper.convertValue(user, UserDtoResp.class);
                    converted.setPole(user.getPole());
                    return converted;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDtoResp> getchefsByDivisionID(Long divisionID) {
        log.info("getting users");
        Division d =   divisionService.findInternDivisionById(divisionID);
        List<UserDtoResp> DP = this.findByDivisionAndRole(d, UserRole.DP);
        List<User> projectsManager = this.userRepository.findByRoleAndDivision_DivisionId(UserRole.PROJECT_MANAGER,divisionID);
        List<User> divisionsManagers = this.userRepository.findByRoleAndDivision_DivisionId(UserRole.DIVISION_MANAGER,divisionID);
        List<User> directeursPole =  DP.stream()
                .map(user -> {
                    return  objectMapper.convertValue(user, User.class);

                })
                .collect(Collectors.toList());
        List<User>cg = this.userRepository.findByRole(UserRole.CG);
        List<User>dg = this.userRepository.findByRole(UserRole.DG);
        List<User>daf = this.userRepository.findByRole(UserRole.DAF);

        List<User> allUsers = new ArrayList<>();
        allUsers.addAll(projectsManager);
        allUsers.addAll(divisionsManagers);
        allUsers.addAll(directeursPole);
        allUsers.addAll(cg);
        allUsers.addAll(dg);
        allUsers.addAll(daf);

        // Map User entities to UserDTOs using ObjectMapper
        return allUsers.stream()
                .map(user -> {
                    UserDtoResp converted =  objectMapper.convertValue(user, UserDtoResp.class);
                    converted.setPole(user.getPole());
                    return converted;
                })
                .collect(Collectors.toList());


    }


    @Override
    public List<UserDtoResp> findByDivision(Division d) {
//        log.info("getting users");
//        return this.userRepository.findAll();

        log.info("getting users203666");
        Optional<List<User>> users = this.userRepository.findByDivision(d);

        // Map User entities to UserDTOs using ObjectMapper
        return users.get().stream()
                .map(user -> objectMapper.convertValue(user, UserDtoResp.class))
                .collect(Collectors.toList());
    }


    @Override
    public List<UserDtoResp> findByDivisionAndRole(Division d, UserRole role) {
//        log.info("getting users");
//        return this.userRepository.findAll();

        Pole p = d.getPole();

        log.info("getting users 1444");
        List<User> users = this.userRepository.findByRoleAndPole(role, p);
        return users.stream()
                .map(user -> objectMapper.convertValue(user, UserDtoResp.class))
                .collect(Collectors.toList());
    }


    @Override
    public ResponseEntity<?> getUserById(Long userID) {


        Optional user =   this.userRepository.findById(userID);
        if (user.isPresent()){
            UserDtoResp userDtoResp = objectMapper.convertValue(user, UserDtoResp.class);

            return ResponseEntity.status(200).body(userDtoResp);
        }else{
            throw new ResourceNotFoundException("user","userId",""+userID);
        }


    }


    @Override
    public boolean toggleUserActiveStatus(Long userId) {
        System.out.println(userId);
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setActive(!user.isActive()); // Toggle active status
            userRepository.save(user);
            System.out.println("tr");

            return true; // Return true indicating successful toggle
        }
        System.out.println("false");

        return false; // Return false if user not found

    }




    public User updateUser(Long userId, UserEditDto userDTO) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();

            // Update user information based on the DTO received
            existingUser.setName(userDTO.getName());
            existingUser.setEmail(userDTO.getEmail());
            existingUser.setRole(userDTO.getRole());
            // Update other user attributes as needed

            return userRepository.save(existingUser); // Save and return the updated user
        }
        return null; // Return null if user not found
    }


}
