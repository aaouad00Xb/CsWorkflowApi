package com.example.workflow.Services;

import com.example.workflow.Dto.*;
import com.example.workflow.Entities.*;
import com.example.workflow.Entities.User.User;
import com.example.workflow.Entities.User.UserRole;
import com.example.workflow.Helpers.DateHelper;
import com.example.workflow.Repositories.*;
import com.example.workflow.Services.interfaces.IcontratService;
import com.example.workflow.Services.interfaces.ItransactionService;
import com.example.workflow.constants.AccountsConstants;
import com.example.workflow.exception.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ContratService implements IcontratService {

    @Autowired
    private ContratSoustraitanceRepo contratSoustraitanceRepo;


    @Autowired
    private DateHelper dateHelper;


    @Autowired
    private ObjectMapper objectMapper;

//    @Autowired
//    private SousTraitanceRepository sousTraitanceRepository;

    @Autowired
    private BusinessRepository businessRepository;


    @Autowired
    private PoleRepository poleRepository;

    @Autowired
    private StepRepository stepRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContratStepEntryDateRepository contratStepEntryDateRepository;


    @Autowired
    private DivisionRepository divisionRepository;


    @Autowired
    private SousTraitantRepo sousTraitantRepo;


    @Autowired
    private ItransactionService transactionService;


    @Override
    public ResponseEntity<?> getAllContrats() {
        try{

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


            // Get the username or any other details about the authenticated user
            String username = authentication.getName();

            User user = userRepository.findUserByUsername(username).orElseThrow(()->new UsernameNotFoundException("user not found"));
//            todo
            if (user.getRole() != null) {
                if (user.getRole() == UserRole.DIVISION_MANAGER || user.getRole() == UserRole.PROJECT_MANAGER) {

                    Set<ContratSoutraitanceResponseDto> divisionDTOList = new HashSet<>(contratSoustraitanceRepo.findContratSousTraitancesByValidIsFalseAndDivisionAndCurrentStep_UserRole(user.getDivision(),user.getRole()).stream()
                            .map(d -> objectMapper.convertValue(d, ContratSoutraitanceResponseDto.class))
                            .collect(Collectors.toList()));

                    List<ContratSoutraitanceResponseDto> start = contratSoustraitanceRepo.findContratSousTraitancesByValidIsFalseAndProjectManagerAndCurrentStep_UserRole(user,UserRole.PROJECT_MANAGER).stream()
                            .map(d -> objectMapper.convertValue(d, ContratSoutraitanceResponseDto.class))
                            .collect(Collectors.toList());
                    divisionDTOList.addAll(start);

                    List<ContratSoutraitanceResponseDto> mergedContracts = new ArrayList<>(divisionDTOList);

                    return ResponseEntity.status(200).body(mergedContracts);
                } else if (user.getRole() == UserRole.DP) {
                    // Query contracts by user's role and division for DP role
                    Pole p = user.getPole();

                    Set<ContratSoutraitanceResponseDto> divisionDTOList = new HashSet<>(contratSoustraitanceRepo.findInvalidContratByPole(p.getPoleID()).stream()
                            .map(d -> objectMapper.convertValue(d, ContratSoutraitanceResponseDto.class))
                            .collect(Collectors.toList())) ;


                    List<ContratSoutraitanceResponseDto> start = contratSoustraitanceRepo.findContratSousTraitancesByValidIsFalseAndProjectManagerAndCurrentStep_UserRole(user,UserRole.PROJECT_MANAGER).stream()
                            .map(d -> objectMapper.convertValue(d, ContratSoutraitanceResponseDto.class))
                            .collect(Collectors.toList());

                    divisionDTOList.addAll(start);

                    List<ContratSoutraitanceResponseDto> mergedContracts = new ArrayList<>(divisionDTOList);
                    return ResponseEntity.status(200).body(mergedContracts);

                } else {
                    // For CG, DAF, or DG roles, rely on the user's role only

                    List<ContratSoutraitanceResponseDto> divisionDTOList = contratSoustraitanceRepo.findAll().stream()
                            .map(d -> objectMapper.convertValue(d, ContratSoutraitanceResponseDto.class))
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
//            List<ContratSoutraitanceResponseDto> divisionDTOList = contratSousTraitances.stream()
//                    .map(d -> objectMapper.convertValue(d, ContratSoutraitanceResponseDto.class))
//                    .collect(Collectors.toList());
//
//            return ResponseEntity.status(200).body(divisionDTOList);
        }catch (Exception e){
            System.out.println(e);
            throw new RuntimeException("something goes wrong with this API");
        }
    }

    @Override
    public ResponseEntity<?>  getContratById(Long id) {
        try {
            ContratSousTraitance contratSousTraitance =  contratSoustraitanceRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("contrat ","contrat id",""+id));
            return ResponseEntity.status(200).body(objectMapper.convertValue(contratSousTraitance,ContratSoutraitanceResponseDto.class));
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }


    }

    @Override
    public ResponseEntity<?>  createContrat(ContratSoutraitanceCreationDto contrat) {

        try{
            Business business =  businessRepository.findById(contrat.getBusinessID()).orElseThrow(()->new ResourceNotFoundException("Business ","getBusiness",""+contrat.getBusinessID()));
            Soustraitant soustraitant =  sousTraitantRepo.findById(contrat.getSoustraitantID()).orElseThrow(()->new ResourceNotFoundException("soustraitant ","traitant id",""+contrat.getSoustraitantID()));
            Division division =  divisionRepository.findById(contrat.getDivisionID()).orElseThrow(()->new ResourceNotFoundException("division","division id ",""+contrat.getDivisionID()));
            User projectManager =  userRepository.findById(contrat.getProjectManagerID()).orElseThrow(()->new ResourceNotFoundException("projectManager","projectManagerID",""+contrat.getProjectManagerID()));

            Step step =  stepRepository.findById(1L).orElseThrow(()->new ResourceNotFoundException("step","step id ","1"));

            ContratSousTraitance contratSousTraitance = objectMapper.convertValue(contrat,ContratSousTraitance.class);
            contratSousTraitance.setBusiness(business);
            contratSousTraitance.setProjectManager(projectManager);
            contratSousTraitance.setSoustraitant(soustraitant);
            contratSousTraitance.setDivision(division);
            contratSousTraitance.setCurrentStep(step);

            ContratSousTraitance contratSousTraitance1 =  contratSoustraitanceRepo.save(contratSousTraitance);
            this.transactionService.initContratToStep(contratSousTraitance1);

            return ResponseEntity.status(200).body(contratSousTraitance1);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }


    }

//    @Override
//    public ContratSousTraitance createFacture(FactureCreationDto facture) {
//
//        ContratSousTraitance f = ContratSousTraitance.builder()
//                .sousTraitance(sousTraitanceRepository.findById(facture.getSousTraitant()).get())
//                .business(businessRepository.findById(facture.getBusiness()).get())
//                .factureDate(facture.getFactureDate())
//                .totalAmount(facture.getTotalAmount())
//                .factureNumber(facture.getFactureNumber())
//                .build();
//
//        return this.transactionService.initFactureToStep(factureRepository.save(f));
//    }

    @Override
    public ResponseEntity<?> updateContrat(Long id, ContratSoutraitanceCreationDto updatedFacture) {

        try{
            ContratSousTraitance existingFacture =  contratSoustraitanceRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("contrat ","contrat id",""+id));


            // Update fields as needed
            existingFacture.setContratNumber(updatedFacture.getContratNumber());
            existingFacture.setSignatureDate(updatedFacture.getSignatureDate());
            existingFacture.setTypeContrat(updatedFacture.getTypeContrat());
            existingFacture.setDateDebut_contrat(updatedFacture.getDateDebut_contrat());
            existingFacture.setDateFin_contrat(updatedFacture.getDateFin_contrat());
            existingFacture.setTotalAmount(updatedFacture.getTotalAmount());
            existingFacture.setTotalAmount(updatedFacture.getTotalAmount());
            // Update other fields here

            contratSoustraitanceRepo.save(existingFacture);
            return ResponseEntity.status(200).body(new ResponseDto(AccountsConstants.STATUS_200,AccountsConstants.MESSAGE_200));
        }catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }





    }

    @Override
    public ResponseEntity<?> deleteContrat(Long id) {
        try{
            contratSoustraitanceRepo.deleteById(id);
            return ResponseEntity.status(200).body(new ResponseDto("200", AccountsConstants.MESSAGE_200));

        }catch(Exception e){
            throw new RuntimeException("could not drop the given division");
        }

    }




    @Override
    public ResponseEntity<?> getContratBySousTraitance(Long soustranceID) {
        return null;
    }


    @Override
        public ResponseEntity<?> getContratByBusinessAndSousTraitant(Long businessId, Long soustraitantId) {
        // Implement the logic to retrieve Factures by Business ID and SousTraitant ID
        try{
           List<ContratSousTraitance> contratSousTraitance =  contratSoustraitanceRepo.findByBusinessIDAndSousTraitantId(businessId, soustraitantId);
            return ResponseEntity.status(200).body(contratSousTraitance);
        }catch (Exception e){
            System.out.println(e);
            throw new RuntimeException("someting goes wrong");
        }
    }

//    @Override
//    public List<ContratSousTraitance> getFacturesBySousTraitance(Long soustranceID) {
//    // Implement the logic to retrieve Factures by Business ID and SousTraitant ID
//    return factureRepository.findBySousTraitanceId(soustranceID);
//
//    }

//todo  old version

    @Override
    public List<ContratSoutraitanceResponseDto> getContractsByUserRoleAndDivision() {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


    // Get the username or any other details about the authenticated user
    String username = authentication.getName();

    User user = userRepository.findUserByUsername(username).orElseThrow(()->new UsernameNotFoundException("user not found"));
    UserRole userRole = user.getRole(); // Get user's role

        if (userRole != null) {
            if (userRole == UserRole.DIVISION_MANAGER || userRole == UserRole.PROJECT_MANAGER) {

                Set<ContratSoutraitanceResponseDto> divisionDTOList = new HashSet<>(contratSoustraitanceRepo.findContratSousTraitancesByValidIsFalseAndDivisionAndCurrentStep_UserRole(user.getDivision(),userRole).stream()
                        .map(d -> objectMapper.convertValue(d, ContratSoutraitanceResponseDto.class))
                        .collect(Collectors.toList()));

                List<ContratSoutraitanceResponseDto> start = contratSoustraitanceRepo.findContratSousTraitancesByValidIsFalseAndProjectManagerAndCurrentStep_UserRole(user,UserRole.PROJECT_MANAGER).stream()
                        .map(d -> objectMapper.convertValue(d, ContratSoutraitanceResponseDto.class))
                        .collect(Collectors.toList());
                divisionDTOList.addAll(start);

                List<ContratSoutraitanceResponseDto> mergedContracts = new ArrayList<>(divisionDTOList);

                mergedContracts.stream().forEach(factureResponseDto -> {
                    factureResponseDto.setContratStepEntryDate((ContratStepEntryDate) transactionService.getEntryDateByContratAndStep(factureResponseDto.getContratID(),factureResponseDto.getCurrentStep().getStepID()).getBody());
                });

                return mergedContracts;
            } else if (userRole == UserRole.DP) {
                // Query contracts by user's role and division for DP role
                Pole p = user.getPole();

                Set<ContratSoutraitanceResponseDto> divisionDTOList = new HashSet<>(contratSoustraitanceRepo.findInvalidContratByPole(p.getPoleID()).stream()
                        .map(d -> objectMapper.convertValue(d, ContratSoutraitanceResponseDto.class))
                        .collect(Collectors.toList())) ;


                List<ContratSoutraitanceResponseDto> start = contratSoustraitanceRepo.findContratSousTraitancesByValidIsFalseAndProjectManagerAndCurrentStep_UserRole(user,UserRole.PROJECT_MANAGER).stream()
                        .map(d -> objectMapper.convertValue(d, ContratSoutraitanceResponseDto.class))
                        .collect(Collectors.toList());

                divisionDTOList.addAll(start);

                List<ContratSoutraitanceResponseDto> mergedContracts = new ArrayList<>(divisionDTOList);
                 mergedContracts.stream().forEach(factureResponseDto -> {
                    factureResponseDto.setContratStepEntryDate((ContratStepEntryDate) transactionService.getEntryDateByContratAndStep(factureResponseDto.getContratID(),factureResponseDto.getCurrentStep().getStepID()).getBody());
                });

                return mergedContracts;
            } else {
                // For CG, DAF, or DG roles, rely on the user's role only

                Set<ContratSoutraitanceResponseDto> divisionDTOList = new HashSet<>(contratSoustraitanceRepo.findContratSousTraitancesByValidIsFalseAndCurrentStep_UserRole(userRole).stream()
                        .map(d -> objectMapper.convertValue(d, ContratSoutraitanceResponseDto.class))
                        .collect(Collectors.toList())) ;


                List<ContratSoutraitanceResponseDto> start = contratSoustraitanceRepo.findContratSousTraitancesByValidIsFalseAndProjectManagerAndCurrentStep_UserRole(user,UserRole.PROJECT_MANAGER).stream()
                        .map(d -> objectMapper.convertValue(d, ContratSoutraitanceResponseDto.class))
                        .collect(Collectors.toList());
                divisionDTOList.addAll(start);
                List<ContratSoutraitanceResponseDto> mergedContracts = new ArrayList<>(divisionDTOList);
                mergedContracts.stream().forEach(factureResponseDto -> {
                    factureResponseDto.setContratStepEntryDate((ContratStepEntryDate) transactionService.getEntryDateByContratAndStep(factureResponseDto.getContratID(),factureResponseDto.getCurrentStep().getStepID()).getBody());
                });

                return mergedContracts;
            }
        }

        return Collections.emptyList(); // Handle scenario where user role is null
    }

//todo  old version






    @Override
    public List<ContratSoutraitanceResponseDto> getContracts() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        // Get the username or any other details about the authenticated user
        String username = authentication.getName();

        User user = userRepository.findUserByUsername(username).orElseThrow(()->new UsernameNotFoundException("user not found"));
        UserRole userRole = user.getRole(); // Get user's role

        if (userRole != null) {
            if (userRole == UserRole.DIVISION_MANAGER || userRole == UserRole.PROJECT_MANAGER) {

                Set<ContratSoutraitanceResponseDto> divisionDTOList = new HashSet<>(contratSoustraitanceRepo.findContratSousTraitancesByValidIsFalseAndDivision(user.getDivision()).stream()
                        .map(d -> objectMapper.convertValue(d, ContratSoutraitanceResponseDto.class))
                        .collect(Collectors.toList()));

                List<ContratSoutraitanceResponseDto> start = contratSoustraitanceRepo.findContratSousTraitancesByValidIsFalseAndProjectManagerAndCurrentStep_UserRole(user,UserRole.PROJECT_MANAGER).stream()
                        .map(d -> objectMapper.convertValue(d, ContratSoutraitanceResponseDto.class))
                        .collect(Collectors.toList());
                divisionDTOList.addAll(start);

                List<ContratSoutraitanceResponseDto> mergedContracts = new ArrayList<>(divisionDTOList);

                return mergedContracts;
            } else if (userRole == UserRole.DP) {
                // Query contracts by user's role and division for DP role
                Pole p = user.getPole();

                Set<ContratSoutraitanceResponseDto> divisionDTOList = new HashSet<>(contratSoustraitanceRepo.findContratByPole(p.getPoleID()).stream()
                        .map(d -> objectMapper.convertValue(d, ContratSoutraitanceResponseDto.class))
                        .collect(Collectors.toList())) ;


                List<ContratSoutraitanceResponseDto> start = contratSoustraitanceRepo.findContratSousTraitancesByValidIsFalseAndProjectManagerAndCurrentStep_UserRole(user,UserRole.PROJECT_MANAGER).stream()
                        .map(d -> objectMapper.convertValue(d, ContratSoutraitanceResponseDto.class))
                        .collect(Collectors.toList());

                divisionDTOList.addAll(start);

                List<ContratSoutraitanceResponseDto> mergedContracts = new ArrayList<>(divisionDTOList);

                return mergedContracts;
            } else {
                // For CG, DAF, or DG roles, rely on the user's role only

                Set<ContratSoutraitanceResponseDto> divisionDTOList = new HashSet<>(contratSoustraitanceRepo.findAll().stream()
                        .map(d -> objectMapper.convertValue(d, ContratSoutraitanceResponseDto.class))
                        .collect(Collectors.toList())) ;


                List<ContratSoutraitanceResponseDto> start = contratSoustraitanceRepo.findContratSousTraitancesByValidIsFalseAndProjectManagerAndCurrentStep_UserRole(user,UserRole.PROJECT_MANAGER).stream()
                        .map(d -> objectMapper.convertValue(d, ContratSoutraitanceResponseDto.class))
                        .collect(Collectors.toList());
                divisionDTOList.addAll(start);
                List<ContratSoutraitanceResponseDto> mergedContracts = new ArrayList<>(divisionDTOList);

                return mergedContracts;
            }
        }

        return Collections.emptyList(); // Handle scenario where user role is null
    }

    @Override
    public List<ContratSoutraitanceResponseDto> findAll() {
        List<ContratSoutraitanceResponseDto> contratSoutraitanceResponseDtos = contratSoustraitanceRepo.findAll().stream()
                .map(d -> objectMapper.convertValue(d, ContratSoutraitanceResponseDto.class))
                .collect(Collectors.toList());

        return contratSoutraitanceResponseDtos;
    }


    @Override
        public List<ContratSoutraitanceResponseDto> getContractsByUserRoleAndDivisionAilleur() {

           Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


           // Get the username or any other details about the authenticated user
           String username = authentication.getName();

           User user = userRepository.findUserByUsername(username).orElseThrow(()->new UsernameNotFoundException("user not found"));
           UserRole userRole = user.getRole(); // Get user's role

        if (userRole != null) {
            if (userRole == UserRole.DIVISION_MANAGER || userRole == UserRole.PROJECT_MANAGER) {
                // Query contracts by user's role and division for Division Manager or Project Manager roles

                List<ContratSoutraitanceResponseDto> divisionDTOList = contratSoustraitanceRepo.findContratSousTraitancesByValidIsFalseAndDivisionAndCurrentStep_UserRoleIsNot(user.getDivision(),userRole).stream()
                        .map(d -> objectMapper.convertValue(d, ContratSoutraitanceResponseDto.class))
                        .collect(Collectors.toList());

                return divisionDTOList;

            } else if (userRole == UserRole.DP) {
                // Query contracts by user's role and division for DP role
                Pole p = user.getPole();


                List<ContratSoutraitanceResponseDto> divisionDTOList = contratSoustraitanceRepo.findInvalidContratByPoleAilleur(p.getPoleID()).stream()
                        .map(d -> objectMapper.convertValue(d, ContratSoutraitanceResponseDto.class))
                        .collect(Collectors.toList());

                return divisionDTOList;

            } else {
                // For CG, DAF, or DG roles, rely on the user's role only

                List<ContratSoutraitanceResponseDto> divisionDTOList = contratSoustraitanceRepo.findContratSousTraitancesByValidIsFalseAndCurrentStep_UserRoleIsNot(userRole).stream()
                        .map(d -> objectMapper.convertValue(d, ContratSoutraitanceResponseDto.class))
                        .collect(Collectors.toList());

                return divisionDTOList;
            }
        }

        return Collections.emptyList(); // Handle scenario where user role is null
    }





    @Override
    public List<ContratSoutraitanceResponseDto> getContractsByUserRoleAndDivisionValid() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        // Get the username or any other details about the authenticated user
        String username = authentication.getName();

        User user = userRepository.findUserByUsername(username).orElseThrow(()->new UsernameNotFoundException("user not found"));        UserRole userRole = user.getRole(); // Get user's role

        if (userRole != null) {
            if (userRole == UserRole.DIVISION_MANAGER || userRole == UserRole.PROJECT_MANAGER) {
                // Query contracts by user's role and division for Division Manager or Project Manager roles

                List<ContratSoutraitanceResponseDto> divisionDTOList = contratSoustraitanceRepo.findContratSousTraitancesByValidIsTrueAndDivision(user.getDivision()).stream()
                        .map(d -> objectMapper.convertValue(d, ContratSoutraitanceResponseDto.class))
                        .collect(Collectors.toList());

                return divisionDTOList;

            } else if (userRole == UserRole.DP) {
                // Query contracts by user's role and division for DP role
                Pole p = user.getPole();


                List<ContratSoutraitanceResponseDto> divisionDTOList = contratSoustraitanceRepo.findInvalidContratByPoleAilleurValid(p.getPoleID()).stream()
                        .map(d -> objectMapper.convertValue(d, ContratSoutraitanceResponseDto.class))
                        .collect(Collectors.toList());

                return divisionDTOList;

            } else {
                // For CG, DAF, or DG roles, rely on the user's role only

                List<ContratSoutraitanceResponseDto> divisionDTOList = contratSoustraitanceRepo.findContratSousTraitancesByValidIsTrue().stream()
                        .map(d -> objectMapper.convertValue(d, ContratSoutraitanceResponseDto.class))
                        .collect(Collectors.toList());

                return divisionDTOList;
            }
        }

        return Collections.emptyList(); // Handle scenario where user role is null
    }


    // TODO: 26/12/2023







    @Override
    public int getTotalActiveContracts() {
        try{
            System.out.println("getTotalActiveContracts");
            return contratSoustraitanceRepo.countByValid(true);

        }catch (Exception e){
            throw new RuntimeException("getTotalActiveContracts");
        }
    }

    @Override
    public long countContractsInStepOne() {
        try{
            System.out.println("countContractsInStepOne");

            return contratSoustraitanceRepo.countByCurrentStepStepID(1L);

        }catch (Exception e){
            throw new RuntimeException("countContractsInStepOne");
        }
    }


    @Override
    public long countOngoingContracts() {

        try{
            System.out.println("countOngoingContracts");


            return contratSoustraitanceRepo.countByValidFalseAndCurrentStepStepIDNot(1L);

        }catch (Exception e){
            throw new RuntimeException("countOngoingContracts");
        }

    }

    @Override
    public long countAll() {

        try{
            System.out.println("countAll");

            return contratSoustraitanceRepo.count();

        }catch (Exception e){
            throw new RuntimeException("countAll");
        }
    }

    @Override
    public double calculateTotalContractValue() {

        try{

            System.out.println("calculateTotalContractValue");


            Optional<Double> totalValue = contratSoustraitanceRepo.sumTotalAmount();
            return totalValue.orElse(0.0); // Return 0 if no contracts found or sum is null
        }catch (Exception e){
            throw new RuntimeException("calculateTotalContractValue");
        }


    }



    public long calculateContractTraceabilityDuration(ContratSousTraitance contratSousTraitance) {

try{
    System.out.println("calculateContractTraceabilityDuration");

    List<ContratStepEntryDate> entries = contratStepEntryDateRepository.findByFactureValidOrderByEntryDateAsc(contratSousTraitance);
    System.out.println(entries);


    System.out.println(entries);

    if (entries.isEmpty()) {
        return 0; // No traceability data found
    }

    LocalDateTime firstEntryDate = entries.get(0).getEntryDate();
    LocalDateTime lastEntryDate = entries.get(entries.size() - 1).getEntryDate();

    System.out.println(firstEntryDate);
    System.out.println(lastEntryDate);

    // Calculate the duration between the first and last entry dates in days
    return ChronoUnit.DAYS.between(firstEntryDate, lastEntryDate);
}catch (Exception e){
    System.out.println("achraf aouad");
    System.out.println(e);
    throw new RuntimeException(e.getMessage());
}

    }

    @Override
    public double calculateAverageContractTraceabilityDuration() {
        System.out.println("calculateAverageContractTraceabilityDuration");


        List<ContratSousTraitance> validatedContracts = contratSoustraitanceRepo.findContratSousTraitancesByValidIsTrue();
        int numberOfValidatedContracts = validatedContracts.size();

        System.out.println(numberOfValidatedContracts);

        if (numberOfValidatedContracts == 0) {
            return 0.0; // No validated contracts found
        }

        long totalTraceabilityDuration = 0;

        for (ContratSousTraitance contrat : validatedContracts) {
            long duration = calculateContractTraceabilityDuration(contrat);
            totalTraceabilityDuration += duration;
        }

        // Calculate the average traceability duration for all validated contracts
        return (double) totalTraceabilityDuration / numberOfValidatedContracts;
    }




    @Override
    public long findLongestContractDuration() {

        System.out.println("findLongestContractDuration");


        List<ContratSousTraitance> validatedContracts = contratSoustraitanceRepo.findContratSousTraitancesByValidIsTrue();

        if (validatedContracts.isEmpty()) {
            return 0; // No validated contracts found
        }

        long longestDuration = 0;

        for (ContratSousTraitance contrat : validatedContracts) {
            long duration = calculateContractTraceabilityDuration(contrat);
            longestDuration = Math.max(longestDuration, duration);
        }

        // Return the longest contract duration
        return longestDuration;
    }

    @Override
    public long findShortestContractDuration() {
        System.out.println("findShortestContractDuration");

        List<ContratSousTraitance> validatedContracts = contratSoustraitanceRepo.findContratSousTraitancesByValidIsTrue();

        if (validatedContracts.isEmpty()) {
            return 0; // No validated contracts found
        }

        long shortestDuration = Long.MAX_VALUE;

        for (ContratSousTraitance contrat : validatedContracts) {
            long duration = calculateContractTraceabilityDuration(contrat);
            shortestDuration = Math.min(shortestDuration, duration);
        }


        // If no duration is found (i.e., all contracts have empty traceability data), return 0

        System.out.println("********************");

        return shortestDuration == Long.MAX_VALUE ? 0 : shortestDuration;
    }




@Override
    public DashboardBarDto populateLists() {
        List<Division> divisions = divisionRepository.findAll(); // Fetch all divisions

        List<Integer> contractCounts = new ArrayList<>();
        List<Integer> restartedContractsCount = new ArrayList<>();
        List<Integer> validatedContractsCount = new ArrayList<>();
        List<String> divisionNames = new ArrayList<>();

        for (Division division : divisions) {

            List<ContratSousTraitance> contractsInDivision = division.getContratSousTraitances();

            int totalContracts = contractsInDivision.size();
            int restartedCount = 0;
            int validatedCount = 0;

            for (ContratSousTraitance contrat : contractsInDivision) {
                System.out.println("hana dkhelt");
                List<ContratStepEntryDate> entryDates = contratStepEntryDateRepository
                        .findByFactureEncoursOrderByEntryDateAsc(contrat);


                // Logic for restarted contracts
                if (hasretardEntryDays(entryDates)) {
                    restartedCount++;
                }

                // Logic for validated contracts
                if (contrat.isValid()) {
                    validatedCount++;
                }
            }

            // Add counts and division names to the lists
            contractCounts.add(totalContracts);
            restartedContractsCount.add(restartedCount);
            validatedContractsCount.add(validatedCount);
            divisionNames.add(division.getDivisionName());
        }


        return  DashboardBarDto.builder()
                .contractCounts(contractCounts)
                .divisionNames(divisionNames)
                .restartedContractsCount(restartedContractsCount)
                .validatedContractsCount(validatedContractsCount)
                .build();
    }



    @Override
    public DashboardPoleBarDto populatePoleList() {
        List<Pole> poles = poleRepository.findAll(); // Fetch all poles

        List<Integer> contractCounts = new ArrayList<>();
        List<Integer> restartedContractsCount = new ArrayList<>();
        List<Integer> validatedContractsCount = new ArrayList<>();
        List<String> poleNames = new ArrayList<>();

        for (Pole pole : poles) {
            int totalContracts = 0;
            int restartedCount = 0;
            int validatedCount = 0;

            for (Division division : pole.getDivisions()) {
                List<ContratSousTraitance> contractsInDivision = division.getContratSousTraitances();
                totalContracts += contractsInDivision.size();

                for (ContratSousTraitance contrat : contractsInDivision) {
                    List<ContratStepEntryDate> entryDates = contratStepEntryDateRepository
                            .findByFactureEncoursOrderByEntryDateAsc(contrat);

                    if (hasretardEntryDays(entryDates)) {
                        restartedCount++;
                    }

                    if (contrat.isValid()) {
                        validatedCount++;
                    }
                }
            }

            contractCounts.add(totalContracts);
            restartedContractsCount.add(restartedCount);
            validatedContractsCount.add(validatedCount);
            poleNames.add(pole.getPoleName());
        }

        return DashboardPoleBarDto.builder()
                .contractCounts(contractCounts)
                .polesNames(poleNames)
                .restartedContractsCount(restartedContractsCount)
                .validatedContractsCount(validatedContractsCount)
                .build();
    }


    private boolean hasretardEntryDays(List<ContratStepEntryDate> entryDates) {
        if (!entryDates.isEmpty()) {
            ContratStepEntryDate lastEntryDate = entryDates.get(entryDates.size() - 1);
            LocalDateTime today = LocalDateTime.now();
            LocalDateTime lastEntryDay = lastEntryDate.getEntryDate();

            // Check if the difference between last entry day and today exceeds 1 day
            return ChronoUnit.DAYS.between(lastEntryDay, today) > 1;
        }
        return false;
    }










    @Override
    public List<EvolutionChartDto> populateEvolution() {
        List<Division> divisions = divisionRepository.findAll(); // Fetch all divisions

        List<Integer> delayedContractsCount = new ArrayList<>();
        List<Integer> validatedContractsCount = new ArrayList<>();
        List<String> divisionNames = new ArrayList<>();

        // Determine the start date and end date for the last three months

        Double i = 0D;
        Integer j = 6;

        List <EvolutionChartDto> evolutionChartDto = new ArrayList<>();

            for (Division division : divisions) {
                EvolutionChartDto ev  = new EvolutionChartDto();
                List<ContratSousTraitance> contractsInDivision = division.getContratSousTraitances();

                ev.setType("line");
                ev.setName(division.getDivisionName());

                int validatedCount = 0;
                while(j>=0){
                    Double delayedCount = 0D;
                    LocalDateTime startDate = LocalDateTime.now().minusMonths(j+1);
                    LocalDateTime endDate =LocalDateTime.now().minusMonths(j);
//                    printing the time between the dates

                    System.out.format("start date %s / end date %s \n",startDate,endDate);

                for (ContratSousTraitance contrat : contractsInDivision) {
                    List<ContratStepEntryDate> entryDates = contratStepEntryDateRepository
                            .findByFactureEncoursOrderByEntryDateAsc(contrat);


                    ContratStepEntryDate preventryDates = contratStepEntryDateRepository.getLastEntryByContratIdBeforeDateTime(contrat.getContratID(),startDate);

                    // Logic for delayed contracts within the last three months
                    if (hasRetardEntryDaysWithinRange(entryDates, startDate, endDate)) {
                        delayedCount = delayedCount + calculateretard(entryDates,startDate,endDate,preventryDates);
                    }
                }

                // Add counts and division names to the lists

                    ev.getData().add(delayedCount+i);
                    i = delayedCount + i;

                    j--;
            }
                evolutionChartDto.add(ev);
        }


        return evolutionChartDto;
    }

    // Method to check if a contract has delayed entry dates within the specified time range
    private boolean hasRetardEntryDaysWithinRange(List<ContratStepEntryDate> entryDates, LocalDateTime startDate, LocalDateTime endDate) {
        for (ContratStepEntryDate entryDate : entryDates) {
            LocalDateTime entryDay = entryDate.getEntryDate();
            // Check if the entry day falls within the specified time range
            if (entryDay.isAfter(startDate) && entryDay.isBefore(endDate)) {
                return true; // Contract has delayed entry within the specified time range
            }
        }
        return false;
    }



    private int calculateretard(List<ContratStepEntryDate> entryDates, LocalDateTime startDate, LocalDateTime endDate,ContratStepEntryDate prev) {


        //previous month

//        LocalDateTime startDatePrev = startDate.minusMonths(1);
//        LocalDateTime endDatePrev = startDate;
//        List<ContratStepEntryDate> entrydates = new ArrayList<>();
//        for (ContratStepEntryDate entryDate : entryDates) {
//            LocalDateTime currentEntryDay = entryDate.getEntryDate();
//
//            if (currentEntryDay.isAfter(startDatePrev) && currentEntryDay.isBefore(endDatePrev)) {
//
//
//            }
//
//
//        }

        int delaySum = 0;
        LocalDateTime previousEntryDay = null;

        if(prev!= null){
            if(prev.getEntryDate()!= null){
                previousEntryDay = prev.getEntryDate();
            }
        }


        if(!entryDates.isEmpty()){
            LocalDateTime currentEntryDay = null;


            Optional<ContratStepEntryDate> firstMatchingEntry = entryDates.stream()
                    .filter(entryDate -> entryDate.getEntryDate().isAfter(startDate) && entryDate.getEntryDate().isBefore(endDate))
                    .findFirst();
            if(firstMatchingEntry.isPresent()){
                currentEntryDay = firstMatchingEntry.get().getEntryDate();
            }

            if (previousEntryDay != null  && currentEntryDay!= null) {
                // Calculate the delay between consecutive entry days
                long delayInHours = ChronoUnit.HOURS.between(previousEntryDay, currentEntryDay); //28
                System.out.format("delay in hours %s \n",delayInHours);

                // If the delay exceeds 24 hours, add it to the sum
                if (delayInHours > 24) {
                    System.out.format("upper 24 %s \n",delayInHours);
                    delaySum += (int) (delayInHours); // Subtract 24 hours to account for the threshold
                }else{
                    System.out.format("under 24 %s \n",delayInHours);

                    Long d =24 -  delayInHours;
                    delaySum -= d;
                }


            }
        }

        previousEntryDay = null;

        for (ContratStepEntryDate entryDate : entryDates) {


            LocalDateTime currentEntryDay = entryDate.getEntryDate();

            // Check if the entry day falls within the specified time range
            if (currentEntryDay.isAfter(startDate) && currentEntryDay.isBefore(endDate)) {

                if (previousEntryDay != null) {
                    // Calculate the delay between consecutive entry days
                    long delayInHours = ChronoUnit.HOURS.between(previousEntryDay, currentEntryDay); //28
                    System.out.format("delay in hours %s \n",delayInHours);

                    // If the delay exceeds 24 hours, add it to the sum
                    if (delayInHours > 24) {
                        System.out.format("upper 24 %s \n",delayInHours);
                        delaySum += (int) (delayInHours - 24); // Subtract 24 hours to account for the threshold
                    }else{
                        System.out.format("under 24 %s \n",delayInHours);

                        Long d =24 -  delayInHours;
                        delaySum -= d;
                    }


                }

                // Update previous entry day for the next iteration
                previousEntryDay = currentEntryDay;

            }
        }
        return delaySum;

    }



}
