package com.example.workflow.Services;

import com.example.workflow.Dto.ContratSoutraitanceResponseDto;
import com.example.workflow.Dto.FactureResponseDto;
import com.example.workflow.Dto.FactureCreationDto;
import com.example.workflow.Entities.*;
import com.example.workflow.Entities.User.User;
import com.example.workflow.Entities.User.UserRole;
import com.example.workflow.Repositories.ContratSoustraitanceRepo;
import com.example.workflow.Repositories.FactureRepository;
import com.example.workflow.Repositories.FactureStepRepository;
import com.example.workflow.Repositories.UserRepository;
import com.example.workflow.Services.interfaces.IFTransactionService;
import com.example.workflow.Services.interfaces.IFactureService;
import com.example.workflow.exception.ResourceNotFoundException;
import com.example.workflow.mapper.DeserializationHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FactureService implements IFactureService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FactureRepository factureRepository;

    @Autowired
    private FactureStepRepository stepRepository;





    @Autowired
    @Lazy
    private TransactionFService transactionService;

    @Autowired
    private  DeserializationHelper deserializationHelper;


    @Autowired
    private ContratSoustraitanceRepo contratSoustraitanceRepo;

    @Autowired
    private ObjectMapper objectMapper;



    @Override
    public ResponseEntity<?> getAllFactures() {
        try{

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


            // Get the username or any other details about the authenticated user
            String username = authentication.getName();

            User user = userRepository.findUserByUsername(username).orElseThrow(()->new UsernameNotFoundException("user not found"));
//            todo
            if (user.getRole() != null) {
                if (user.getRole() == UserRole.DIVISION_MANAGER || user.getRole() == UserRole.PROJECT_MANAGER) {

                    Set<FactureResponseDto> divisionDTOList = new HashSet<>(factureRepository.findFacturesByValidFalseAndContratSousTraitance_DivisionAndCurrentStep_UserRole(user.getDivision(),user.getRole()).stream()
                            .map(d -> objectMapper.convertValue(d, FactureResponseDto.class))
                            .collect(Collectors.toList()));

                    List<FactureResponseDto> start = factureRepository.findFacturesByValidFalseAndContratSousTraitance_ProjectManagerAndCurrentStep_UserRole(user,UserRole.PROJECT_MANAGER).stream()
                            .map(d -> objectMapper.convertValue(d, FactureResponseDto.class))
                            .collect(Collectors.toList());
                    divisionDTOList.addAll(start);

                    List<FactureResponseDto> mergedContracts = new ArrayList<>(divisionDTOList);

                    return ResponseEntity.status(200).body(mergedContracts);
                } else if (user.getRole() == UserRole.DP) {
                    // Query contracts by user's role and division for DP role
                    Pole p = user.getPole();

                    Set<FactureResponseDto> divisionDTOList = new HashSet<>(factureRepository.findInvalidContratByPole(p.getPoleID()).stream()
                            .map(d -> objectMapper.convertValue(d, FactureResponseDto.class))
                            .collect(Collectors.toList())) ;


                    List<FactureResponseDto> start = factureRepository.findFacturesByValidFalseAndContratSousTraitance_ProjectManagerAndCurrentStep_UserRole(user,UserRole.PROJECT_MANAGER).stream()
                            .map(d -> objectMapper.convertValue(d, FactureResponseDto.class))
                            .collect(Collectors.toList());

                    divisionDTOList.addAll(start);

                    List<FactureResponseDto> mergedContracts = new ArrayList<>(divisionDTOList);
                    return ResponseEntity.status(200).body(mergedContracts);

                } else {
                    // For CG, DAF, or DG roles, rely on the user's role only

                    List<FactureResponseDto> divisionDTOList = factureRepository.findAll().stream()
                            .map(d -> objectMapper.convertValue(d, FactureResponseDto.class))
                            .collect(Collectors.toList());
                    return ResponseEntity.status(200).body(divisionDTOList);
                }
            }else{
                throw new RuntimeException("user has no role");

            }

//            todo
//
//          List<ContratSousTraitance> contratSousTraitances = contratSoustraitanceRepo.findAll();
//
//            List<FactureResponseDto> divisionDTOList = contratSousTraitances.stream()
//                    .map(d -> objectMapper.convertValue(d, FactureResponseDto.class))
//                    .collect(Collectors.toList());
//
//            return ResponseEntity.status(200).body(divisionDTOList);
        }catch (Exception e){
            System.out.println(e);
            throw new RuntimeException("something goes wrong with this API");
        }
    }

    @Override
    public ResponseEntity<?> getFactureById(Long id) {
        try {
            Facture facture =  factureRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("contrat ","contrat id",""+id));
            return ResponseEntity.status(200).body(objectMapper.convertValue(facture, FactureResponseDto.class));
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> createFacture(FactureCreationDto factureDto) {
        try{
            ContratSousTraitance contratSousTraitance =  contratSoustraitanceRepo.findById(factureDto.getContratSousTraitanceID()).orElseThrow(()->new ResourceNotFoundException("contrat","contrat id ",""+factureDto.getContratSousTraitanceID()));

            StepFacture step =  stepRepository.findById(1L).orElseThrow(()->new ResourceNotFoundException("step","step id ","1"));

            Facture facture = objectMapper.convertValue(factureDto,Facture.class);

            facture.setCurrentStep(step);
            facture.setContratSousTraitance(contratSousTraitance);

            Facture facture1 =  factureRepository.save(facture);

            this.transactionService.initFactureToStep(facture);

            return ResponseEntity.status(200).body(facture);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> updateFacture(Long id, FactureCreationDto updatedFacture) {
        return null;
    }

    @Override
    public ResponseEntity<?> deleteFacture(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<?> getFactureBySousTraitance(Long soustranceID) {
        return null;
    }

    @Override
    public List<FactureResponseDto> getFacturesByUserRoleAndDivision(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Get the username or any other details about the authenticated user
        String username = authentication.getName();

        User user = userRepository.findUserByUsername(username).orElseThrow(()->new UsernameNotFoundException("user not found"));
        UserRole userRole = user.getRole(); // Get user's role

        if (userRole != null) {
            if (userRole == UserRole.DIVISION_MANAGER || userRole == UserRole.PROJECT_MANAGER) {

                Set<FactureResponseDto> divisionDTOList = new HashSet<>(factureRepository.findFacturesByValidFalseAndContratSousTraitance_DivisionAndCurrentStep_UserRole(user.getDivision(),userRole).stream()
                        .map(d -> objectMapper.convertValue(d, FactureResponseDto.class))
                        .collect(Collectors.toList()));

                List<FactureResponseDto> start = factureRepository.findFacturesByValidFalseAndContratSousTraitance_ProjectManagerAndCurrentStep_UserRole(user,UserRole.PROJECT_MANAGER).stream()
                        .map(d -> objectMapper.convertValue(d, FactureResponseDto.class))
                        .collect(Collectors.toList());
                divisionDTOList.addAll(start);


                List<FactureResponseDto> secondStepFactures = factureRepository.findFacturesByValidFalseAndContratSousTraitance_DivisionAndCurrentStep_StepID(user.getDivision(),2l).stream()
                        .map(d -> objectMapper.convertValue(d, FactureResponseDto.class))
                        .collect(Collectors.toList());
                divisionDTOList.addAll(secondStepFactures);

                List<FactureResponseDto> mergedContracts = new ArrayList<>(divisionDTOList);

                mergedContracts.stream().forEach(factureResponseDto -> {
                    factureResponseDto.setFactureStepEntryDate((FactureStepEntryDate) transactionService.getEntryDateByFactureAndStep(factureResponseDto.getFactureID(),factureResponseDto.getCurrentStep().getStepID()).getBody());
                });
                return mergedContracts;
            } else if (userRole == UserRole.DP) {
                // Query contracts by user's role and division for DP role
                Pole p = user.getPole();

                Set<FactureResponseDto> divisionDTOList = new HashSet<>(factureRepository.findInvalidContratByPole(p.getPoleID()).stream()
                        .map(d -> objectMapper.convertValue(d, FactureResponseDto.class))
                        .collect(Collectors.toList())) ;


                List<FactureResponseDto> start = factureRepository.findFacturesByValidFalseAndContratSousTraitance_ProjectManagerAndCurrentStep_UserRole(user,UserRole.PROJECT_MANAGER).stream()
                        .map(d -> objectMapper.convertValue(d, FactureResponseDto.class))
                        .collect(Collectors.toList());

                divisionDTOList.addAll(start);

                List<FactureResponseDto> mergedContracts = new ArrayList<>(divisionDTOList);
                mergedContracts.stream().forEach(factureResponseDto -> {
                    factureResponseDto.setFactureStepEntryDate((FactureStepEntryDate) transactionService.getEntryDateByFactureAndStep(factureResponseDto.getFactureID(),factureResponseDto.getCurrentStep().getStepID()).getBody());
                });
                return mergedContracts;
            } else {
                // For CG, DAF, or DG roles, rely on the user's role only

                Set<FactureResponseDto> divisionDTOList = new HashSet<>(factureRepository.findFacturesByValidIsFalseAndCurrentStep_UserRole(userRole).stream()
                        .map(d -> objectMapper.convertValue(d, FactureResponseDto.class))
                        .collect(Collectors.toList())) ;


                List<FactureResponseDto> start = factureRepository.findFacturesByValidFalseAndContratSousTraitance_ProjectManagerAndCurrentStep_UserRole(user,UserRole.PROJECT_MANAGER).stream()
                        .map(d -> objectMapper.convertValue(d, FactureResponseDto.class))
                        .collect(Collectors.toList());
                divisionDTOList.addAll(start);
                List<FactureResponseDto> mergedContracts = new ArrayList<>(divisionDTOList);
                mergedContracts.stream().forEach(factureResponseDto -> {
                    factureResponseDto.setFactureStepEntryDate((FactureStepEntryDate) transactionService.getEntryDateByFactureAndStep(factureResponseDto.getFactureID(),factureResponseDto.getCurrentStep().getStepID()).getBody());
                });
                return mergedContracts;
            }
        }

        return Collections.emptyList(); // Handle scenario where user role is null
    }


    @Override
    public List<FactureResponseDto> getFacturesDivision(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Get the username or any other details about the authenticated user
        String username = authentication.getName();
        System.out.println(username);
        User user = userRepository.findUserByUsername(username).orElseThrow(()->new UsernameNotFoundException("user not found"));
        UserRole userRole = user.getRole(); // Get user's role
        System.out.println(userRole);

        if (userRole != null) {
            if (userRole == UserRole.DIVISION_MANAGER || userRole == UserRole.PROJECT_MANAGER) {


                Set<FactureResponseDto> divisionDTOList = new HashSet<>(factureRepository.findFacturesByAndContratSousTraitance_Division(user.getDivision()).stream()
                        .map(d -> objectMapper.convertValue(d, FactureResponseDto.class))
                        .collect(Collectors.toList()));

//                List<FactureResponseDto> start = factureRepository.findFacturesByValidFalseAndContratSousTraitance_ProjectManagerAndCurrentStep_UserRole(user,UserRole.PROJECT_MANAGER).stream()
//                        .map(d -> objectMapper.convertValue(d, FactureResponseDto.class))
//                        .collect(Collectors.toList());
//                divisionDTOList.addAll(start);

                List<FactureResponseDto> mergedContracts = new ArrayList<>(divisionDTOList);
                System.out.println(mergedContracts);


                return mergedContracts;
            } else if (userRole == UserRole.DP) {
                // Query contracts by user's role and division for DP role
                Pole p = user.getPole();

                Set<FactureResponseDto> divisionDTOList = new HashSet<>(factureRepository.findInvalidContratByPole(p.getPoleID()).stream()
                        .map(d -> objectMapper.convertValue(d, FactureResponseDto.class))
                        .collect(Collectors.toList())) ;


//                List<FactureResponseDto> start = factureRepository.findFacturesByValidFalseAndContratSousTraitance_ProjectManagerAndCurrentStep_UserRole(user,UserRole.PROJECT_MANAGER).stream()
//                        .map(d -> objectMapper.convertValue(d, FactureResponseDto.class))
//                        .collect(Collectors.toList());
//
//                divisionDTOList.addAll(start);

                List<FactureResponseDto> mergedContracts = new ArrayList<>(divisionDTOList);
                System.out.println(mergedContracts);

                return mergedContracts;
            } else {
                // For CG, DAF, or DG roles, rely on the user's role only

                Set<FactureResponseDto> divisionDTOList = new HashSet<>(factureRepository.findFacturesByValidIsFalse().stream()
                        .map(d -> objectMapper.convertValue(d, FactureResponseDto.class))
                        .collect(Collectors.toList())) ;


//                List<FactureResponseDto> start = factureRepository.findFacturesByValidFalseAndContratSousTraitance_ProjectManagerAndCurrentStep_UserRole(user,UserRole.PROJECT_MANAGER).stream()
//                        .map(d -> objectMapper.convertValue(d, FactureResponseDto.class))
//                        .collect(Collectors.toList());
//                divisionDTOList.addAll(start);
                List<FactureResponseDto> mergedContracts = new ArrayList<>(divisionDTOList);
                System.out.println(mergedContracts);
                return mergedContracts;
            }
        }

        return Collections.emptyList(); // Handle scenario where user role is null
    }


    @Override
    public List<FactureResponseDto> getFacturesByUserRoleAndDivisionAilleur() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        // Get the username or any other details about the authenticated user
        String username = authentication.getName();

        User user = userRepository.findUserByUsername(username).orElseThrow(()->new UsernameNotFoundException("user not found"));
        UserRole userRole = user.getRole(); // Get user's role

        if (userRole != null) {
            if (userRole == UserRole.DIVISION_MANAGER || userRole == UserRole.PROJECT_MANAGER) {
                // Query contracts by user's role and division for Division Manager or Project Manager roles

                List<FactureResponseDto> divisionDTOList = factureRepository.findFacturesByValidIsFalseAndContratSousTraitance_DivisionAndCurrentStep_UserRoleIsNot(user.getDivision(),userRole).stream()
                        .map(d -> objectMapper.convertValue(d, FactureResponseDto.class))
                        .collect(Collectors.toList());

                return divisionDTOList;

            } else if (userRole == UserRole.DP) {
                // Query contracts by user's role and division for DP role
                Pole p = user.getPole();


                List<FactureResponseDto> divisionDTOList = factureRepository.findInvalidContratByPoleAilleur(p.getPoleID()).stream()
                        .map(d -> objectMapper.convertValue(d, FactureResponseDto.class))
                        .collect(Collectors.toList());

                return divisionDTOList;

            } else {
                // For CG, DAF, or DG roles, rely on the user's role only


                List<FactureResponseDto> divisionDTOList = factureRepository.findFacturesByValidIsFalseAndCurrentStep_UserRoleIsNot(userRole).stream()
                        .map(d -> objectMapper.convertValue(d, FactureResponseDto.class))
                        .collect(Collectors.toList());

                return divisionDTOList;
            }
        }

        return Collections.emptyList(); // Handle scenario where user role is null
    }

    @Override
    public List<FactureResponseDto> getFacturesByUserRoleAndDivisionValid() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        // Get the username or any other details about the authenticated user
        String username = authentication.getName();

        User user = userRepository.findUserByUsername(username).orElseThrow(()->new UsernameNotFoundException("user not found"));        UserRole userRole = user.getRole(); // Get user's role

        if (userRole != null) {
            if (userRole == UserRole.DIVISION_MANAGER || userRole == UserRole.PROJECT_MANAGER) {
                // Query contracts by user's role and division for Division Manager or Project Manager roles

                List<FactureResponseDto> divisionDTOList = contratSoustraitanceRepo.findContratSousTraitancesByValidIsTrueAndDivision(user.getDivision()).stream()
                        .map(d -> objectMapper.convertValue(d, FactureResponseDto.class))
                        .collect(Collectors.toList());

                return divisionDTOList;

            } else if (userRole == UserRole.DP) {
                // Query contracts by user's role and division for DP role
                Pole p = user.getPole();


                List<FactureResponseDto> divisionDTOList = contratSoustraitanceRepo.findInvalidContratByPoleAilleurValid(p.getPoleID()).stream()
                        .map(d -> objectMapper.convertValue(d, FactureResponseDto.class))
                        .collect(Collectors.toList());

                return divisionDTOList;

            } else {
                // For CG, DAF, or DG roles, rely on the user's role only

                List<FactureResponseDto> divisionDTOList = contratSoustraitanceRepo.findContratSousTraitancesByValidIsTrue().stream()
                        .map(d -> objectMapper.convertValue(d, FactureResponseDto.class))
                        .collect(Collectors.toList());

                return divisionDTOList;
            }
        }

        return Collections.emptyList(); // Handle scenario where user role is null
    }

    @Override
    public int getTotalActiveFactures() {
        return 0;
    }

    @Override
    public long countFacturesInStepOne() {
        return 0;
    }

    @Override
    public long countOngoingFactures() {
        return 0;
    }

    @Override
    public long countAll() {
        return 0;
    }
}
