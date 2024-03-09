package com.example.workflow.Controllers;

import com.example.workflow.Dto.ContratSoutraitanceCreationDto;
import com.example.workflow.Dto.FactureCreationDto;
import com.example.workflow.Entities.ContratSousTraitance;

import com.example.workflow.Entities.Document;
import com.example.workflow.Entities.Facture;
import com.example.workflow.Repositories.FactureRepository;
import com.example.workflow.Services.interfaces.IFTransactionService;
import com.example.workflow.Services.interfaces.IFactureService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/factures")
public class FactureController {

    @Autowired
    private IFactureService factureService;
    
    @Autowired
    private FactureRepository factureRepository;

    @Autowired
    private IFTransactionService transactionService;

    @GetMapping
    public ResponseEntity<?> getAllFactures() {
        return factureService.getAllFactures();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFactureById(@PathVariable Long id) {
        return factureService.getFactureById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateFacture(@PathVariable Long id, @RequestBody FactureCreationDto facture) {
        return factureService.updateFacture(id, facture);
    }


//    @GetMapping("/by-business-and-soustraitant")
//    public ResponseEntity<?> getFacturesByBusinessAndSousTraitant(
//            @RequestParam Long businessId, @RequestParam Long soustraitantId) {
//
//        return ResponseEntity.ok(factureService.get(businessId, soustraitantId));
//    }


    @GetMapping("/getFacturesBySousTraitance/{id}")
    public ResponseEntity<?> getFacturesBySousTraitance(@PathVariable Long id) {

        return ResponseEntity.ok(factureService.getFactureBySousTraitance(id));
    }


    @GetMapping("/MesContrat")
    public ResponseEntity<?> getContrat() {

        return ResponseEntity.ok(factureService.getFacturesByUserRoleAndDivision());
    }


    @GetMapping("/All")
    public ResponseEntity<?> getAll() {

        return ResponseEntity.ok(factureService.getFacturesDivision());
    }

    @GetMapping("/AllFactures")
    public ResponseEntity<?> AllFactures() {

        return ResponseEntity.ok(factureService.getFacturesByUserRoleAndDivision());
    }


    @GetMapping("/MesContratAilleur")
    public ResponseEntity<?> getFacturesByUserRoleAndDivisionAilleur() {

        return ResponseEntity.ok(factureService.getFacturesByUserRoleAndDivisionAilleur());
    }


    @GetMapping("/MesContratValid")
    public ResponseEntity<?> getFacturesByUserRoleAndDivisionValid() {

        return ResponseEntity.ok(factureService.getFacturesByUserRoleAndDivisionValid());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFacture(@PathVariable Long id) {
        return factureService.deleteFacture(id);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createFacture(@Valid @RequestBody FactureCreationDto contrat) {

        return factureService.createFacture(contrat);
    }

    @PostMapping("/transitionContratToStep/{contratID}/{stepId}")
    public ResponseEntity<?> transitionContratToStep(@PathVariable("contratID") Long contratID,@PathVariable("stepId") Long newstepId,@RequestBody String commentaire) {
        try{
            this.transactionService.transitionFactureToStep(contratID,newstepId,commentaire);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);

        }catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/validateFacture/{contratID}")
    public ResponseEntity<?> validateFacture(@PathVariable("contratID") Long contratID,@RequestBody String commentaire) {
        try{
            this.transactionService.validateFacture(contratID,commentaire);
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

            Optional<Facture> d = Optional.ofNullable(factureRepository.findById(id).orElseThrow(() -> new RuntimeException("contrat Not Found")));
            Facture contrat = d.get();

            if (d.isPresent()) {
                if (contrat.getUploadedFiles() != null) {
                    contrat.getUploadedFiles().addAll(documents);
                } else {
                    contrat.setUploadedFiles(documents);
                }
            }

            return new ResponseEntity<>(this.factureRepository.save(contrat), HttpStatus.OK);
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