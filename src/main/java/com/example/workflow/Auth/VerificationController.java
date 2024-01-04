package com.example.workflow.Auth;

import com.example.workflow.Entities.User.User;
import com.example.workflow.Repositories.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class VerificationController {

    private final AuthenticationService userService;
    private final UserRepository userRepository;

    public VerificationController(AuthenticationService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/verify")
    public String verifyUser(@RequestParam("token") String confirmationToken) {
        // Find the user with the given confirmation token
        User user = userService.findByConfirmationToken(confirmationToken);

        if (user != null) {
            // Mark the user's email as confirmed
            user.setMailconfirmed(true);
            user.setConfirmationToken(null); // Optional: clear the token after verification
            userRepository.save(user);
            // Redirect to the Angular login component with success query parameter
            return "redirect:http://localhost:4200/login?verification=success";
        } else {
            // Redirect to the Angular login component with failure query parameter
            return "redirect:http://localhost:4200/login?verification=failure";
        }
    }
}
