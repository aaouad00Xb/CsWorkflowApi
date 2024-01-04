package com.example.workflow.Controllers;

import com.example.workflow.Dto.ContratSoutraitanceCreationDto;
import com.example.workflow.Entities.ContratSousTraitance;
import com.example.workflow.Entities.Document;
import com.example.workflow.Repositories.ContratSoustraitanceRepo;
import com.example.workflow.Services.impl.IcontratService;
import com.example.workflow.Services.impl.ItransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/Contrat")
@Validated
public class ContratSoutraitanceController {

    @Autowired
    private IcontratService contratService;

    @Autowired
    private ItransactionService transactionService;

    @Autowired
    private ContratSoustraitanceRepo contratSoustraitanceRepo;

    @GetMapping
    public ResponseEntity<?> getAllContrats() {
        return contratService.getAllContrats();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getContratById(@PathVariable Long id) {
        return contratService.getContratById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateContrat(@PathVariable Long id, @RequestBody ContratSoutraitanceCreationDto facture) {
        return contratService.updateContrat(id, facture);
    }


    @GetMapping("/by-business-and-soustraitant")
    public ResponseEntity<?> getFacturesByBusinessAndSousTraitant(
            @RequestParam Long businessId, @RequestParam Long soustraitantId) {

        return ResponseEntity.ok(contratService.getContratByBusinessAndSousTraitant(businessId, soustraitantId));
    }


    @GetMapping("/getFacturesBySousTraitance/{id}")
    public ResponseEntity<?> getFacturesBySousTraitance(@PathVariable Long id) {

        return ResponseEntity.ok(contratService.getContratBySousTraitance(id));
    }


      @GetMapping("/MesContrat")
        public ResponseEntity<?> getContrat() {

            return ResponseEntity.ok(contratService.getContractsByUserRoleAndDivision());
        }
      @GetMapping("/MesContratAilleur")
        public ResponseEntity<?> getContractsByUserRoleAndDivisionAilleur() {

            return ResponseEntity.ok(contratService.getContractsByUserRoleAndDivisionAilleur());
        }


        @GetMapping("/MesContratValid")
        public ResponseEntity<?> getContractsByUserRoleAndDivisionValid() {

            return ResponseEntity.ok(contratService.getContractsByUserRoleAndDivisionValid());
        }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFacture(@PathVariable Long id) {
        return contratService.deleteContrat(id);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createContrat(@Valid @RequestBody ContratSoutraitanceCreationDto contrat) {

        return contratService.createContrat(contrat);
    }

    @PostMapping("/transitionContratToStep/{contratID}/{stepId}")
    public ResponseEntity<?> transitionContratToStep(@PathVariable("contratID") Long contratID,@PathVariable("stepId") Long newstepId,@RequestBody String commentaire) {
        try{
            this.transactionService.transitionContratToStep(contratID,newstepId,commentaire);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);

        }catch (Exception e){
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }


    }


    @PostMapping("/validateContrat/{contratID}")
    public ResponseEntity<?> validateContrat(@PathVariable("contratID") Long contratID,@RequestBody String commentaire) {
        try{
            this.transactionService.validateContrat(contratID,commentaire);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);

        }catch (Exception e){
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }


    }


    @PostMapping("/uploadFileReparation/{contratID}")
    public ResponseEntity<?> uploadFileReparation(@RequestParam(required = false, name = "files") List<MultipartFile> files, @PathVariable("contratID") Long id) throws IOException {
        System.out.println(id);
        String path = new ClassPathResource("static/images/").getFile().getAbsolutePath();

        if (files != null && !files.isEmpty()) {
            List<Document> documents = new ArrayList<>();

            for (MultipartFile file : files) {
                System.out.println(file.getOriginalFilename());
                System.out.println(file.getName());
                System.out.println(file.getContentType());
                System.out.println(file.getSize());

                String name = file.getOriginalFilename() + new Random().nextInt(10000) + "." + getFileExtension(file);
                String image = "images/" + name;

                Files.copy(file.getInputStream(), Paths.get(path + File.separator + name), StandardCopyOption.REPLACE_EXISTING);

                documents.add(Document.builder()
                        .size(file.getSize())
                        .originalFilename(file.getOriginalFilename())
                        .path(image)
                        .type(file.getContentType())
                        .build());
            }

            Optional<ContratSousTraitance> d = Optional.ofNullable(contratSoustraitanceRepo.findById(id).orElseThrow(() -> new RuntimeException("contrat Not Found")));
            ContratSousTraitance contrat = d.get();

            if (d.isPresent()) {
                if (contrat.getUploadedFiles() != null) {
                    contrat.getUploadedFiles().addAll(documents);
                } else {
                    contrat.setUploadedFiles(documents);
                }
            }

            return new ResponseEntity<>(this.contratSoustraitanceRepo.save(contrat), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public String getFileExtension(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null) {
            int lastIndex = originalFilename.lastIndexOf('.');
            if (lastIndex != -1) {
                return originalFilename.substring(lastIndex + 1);
            }
        }
        return null; // No file extension found
    }



}
