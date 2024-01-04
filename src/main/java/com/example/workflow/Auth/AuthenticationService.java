package com.example.workflow.Auth;

import com.example.workflow.Configuration.JwtService;
import com.example.workflow.Entities.Division;
import com.example.workflow.Entities.Pole;
import com.example.workflow.Entities.User.User;
import com.example.workflow.Repositories.DivisionRepository;
import com.example.workflow.Repositories.PoleRepository;
import com.example.workflow.Repositories.UserRepository;
import com.example.workflow.Services.EmailService;
import com.example.workflow.Services.impl.IEmailService;
import com.example.workflow.exception.CustomerAlreadyExistsException;
import com.example.workflow.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final DivisionRepository divisionRepository;
    private final PoleRepository poleRepository;

    private final IEmailService emailService;

    public AuthenticationResponse register(RegisterRequest request) {
        System.out.println("hello");
        System.out.println(request);
        // Check if the username or email is already used by another account
        if (repository.existsByUsername(request.getUsername())) {
            throw new CustomerAlreadyExistsException("Username is already used by another account.");
//            throw new RuntimeException("Username is already used by another account.");
        }

        if (repository.existsByEmail(request.getEmail())) {
            throw new CustomerAlreadyExistsException("Email is already used by another account.");
        }
        String confirmationToken = UUID.randomUUID().toString();
        if(request.getPassword().equals(request.getConfirm_password())){
            var user = User.builder()
                    .name(request.getName())
                    .username(request.getUsername())
                    .email(request.getEmail())
                    .bio(request.getBio())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(request.getRole())
                    .nearDeadLine(request.isNearDeadLine())
                    .deadLines(request.isDeadLines())
                    .notifySysuperior(request.isNotifySysuperior())
                    .newFacutresTovalidate(request.isNewFacutresTovalidate())
                    .facturePaidDone(request.isFacturePaidDone())

                    .confirmed(false)
                    .build();

            user.setConfirmationToken(confirmationToken);

            System.out.println("hana");

            if(request.getDivision()!= null){
                Optional<Division> d = divisionRepository.findById(request.getDivision());
                if(d.isPresent()){
                    user.setDivision(d.get());
                }else{
                    throw new ResourceNotFoundException("Division","division ID",request.getDivision().toString() );
                }
            }

            if(request.getPoles()!= null){
                Optional<Pole> p = poleRepository.findById(request.getPoles());
                if(p.isPresent()){
                    user.setPole(p.get());
                }else{
                    throw new ResourceNotFoundException("Pole","pole ID",request.getPoles().toString() );
                }
            }

            if(request.getManager()!= null){
                Optional<User> d = repository.findById(request.getManager());
                if(d.isPresent()){
                    user.setManager(d.get());
                }else{
                    throw new ResourceNotFoundException("Manager","Manager ID",request.getDivision().toString() );
                }
            }
            System.out.println("hana");

            try {
                userRepository.save(user);
//            emailService.sendRegistrationConfirmationEmail(request.getEmail(),user.getConfirmationToken());

            } catch (DataIntegrityViolationException ex) {
                System.out.println(ex);
                throw new RuntimeException("Error saving the user. Some information may already be used by another account.");
            }

            var jwtToken = jwtService.generateToken(user);
            var Token = jwtService.generateToken(user);
            return AuthenticationResponse.builder()
                    .accessToken(jwtToken)
                    .refreshToken(Token)
                    .build();
        }else {
            throw new RuntimeException("passwords are not the same");
        }



    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();

//        if (user.isConfirmed()){
//
//        }
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .userID(user.getId())
                .build();
    }

    public User findByConfirmationToken(String confirmationToken) {
        return repository.findByConfirmationToken(confirmationToken);
    }


}
