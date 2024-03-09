package com.example.workflow.Controllers;

import com.example.workflow.Dto.ResponseDto;
import com.example.workflow.Dto.UserEditDto;
import com.example.workflow.Entities.User.User;
import com.example.workflow.Entities.User.UserRole;
import com.example.workflow.Services.interfaces.IUserService;
import com.example.workflow.constants.AccountsConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService userService;

    @GetMapping("/getUsersBySelectedRoleAndDivision/{userRole}/{divisionID}")
     public ResponseEntity<?> getUsersBySelectedRoleAndDivision(@PathVariable("userRole") UserRole userRole,@PathVariable("divisionID") Long divisionID){

        System.out.println(userRole);
        System.out.println(divisionID);
       return this.userService.getUsersBySelectedRoleAndDivision(userRole,divisionID);

     }


    @GetMapping("/getUsers")
    public ResponseEntity<?> getUsers(){

        return ResponseEntity.status(Integer.parseInt(AccountsConstants.STATUS_200)).body(this.userService.getUsers());

    }
    @GetMapping("/getchefsByDivisionID/{divisionID}")
    public ResponseEntity<?> getchefsByDivisionID(@PathVariable Long divisionID){
        return ResponseEntity.status(Integer.parseInt(AccountsConstants.STATUS_200)).body(this.userService.getchefsByDivisionID(divisionID));

    }


    @PostMapping("/{userId}/update")
    public ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody UserEditDto userDTO) {
        try {
            System.out.println(userDTO);
            User updatedUser = userService.updateUser(userId, userDTO);
            if (updatedUser != null) {
                return ResponseEntity.ok(updatedUser); // Return the updated user details
            } else {
                return ResponseEntity.notFound().build(); // User with the given ID not found
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update user");
        }
    }



    @GetMapping("/{userId}/toggle-active")
    public ResponseEntity<?> toggleUserActiveStatus(@PathVariable Long userId) {
        try {
            System.out.println(userId);
            boolean toggledStatus = userService.toggleUserActiveStatus(userId);
            System.out.println(toggledStatus);
            String message = toggledStatus ? "User status toggled successfully" : "User not found";
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseDto(AccountsConstants.STATUS_201, message));

        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException("Failed to toggle user status");
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to toggle user status");
        }
        }




    @GetMapping("/me/{userID}")
    public ResponseEntity<?> getUserByID(@PathVariable("userID") Long userID){

        System.out.println(userID);

        return this.userService.getUserById(userID);

    }


}










