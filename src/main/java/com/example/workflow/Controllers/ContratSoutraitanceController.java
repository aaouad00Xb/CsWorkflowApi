package com.example.workflow.Controllers;

import com.example.workflow.Dto.ContratSoutraitanceCreationDto;
import com.example.workflow.Entities.ContratSousTraitance;
import com.example.workflow.Entities.Document;
import com.example.workflow.Repositories.ContratSoustraitanceRepo;
import com.example.workflow.Services.FileService;
import com.example.workflow.Services.FileStorageService;
import com.example.workflow.Services.interfaces.IcontratService;
import com.example.workflow.Services.interfaces.ItransactionService;
import com.example.workflow.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
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
    private FileService fileService;

    @Autowired
    private ItransactionService transactionService;

    @Autowired
    private FileStorageService fileStorageService;

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



    @GetMapping("/All")
    public ResponseEntity<?> getAllContrat() {

        return ResponseEntity.ok(contratService.getContracts());
    }


    @GetMapping("/findAll")
    public ResponseEntity<?> findAll() {

        return ResponseEntity.ok(contratService.findAll());
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


    @GetMapping("/files")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@RequestParam("fileName") String fileName) throws IOException {
        Resource file = fileService.download(fileName);
        Path path = file.getFile()
                .toPath();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(path))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }




//    @GetMapping(value = "/images/{fileName}", produces = "application/vnd.openxmlformats-officedocument.wordprocessingml.document")
//    public ResponseEntity<?> getFileViaByteArrayResource(@PathVariable String fileName) throws IOException {
//        System.out.println(fileName);
//        return ResponseEntity.status(200).body(new Object());
//
//    }





    /*HttpHeaders respHeaders = new HttpHeaders();
    respHeaders.setContentLength(isr.length);
    respHeaders.setContentType(new MediaType("text", "json"));
    respHeaders.setCacheControl("must-revalidate, post-check=0, pre-check=0");
    respHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
    return new ResponseEntity<byte[]>(isr, respHeaders, HttpStatus.OK);*/




//    @GetMapping(value = "/download-file",produces = "application/vnd.openxmlformats-officedocument.wordprocessingml.document")
//    public ResponseEntity<Resource> downloadFile(@RequestParam("fileName") String fileName, HttpServletRequest request) throws IOException {
//        // Load file as Resource
//
//        System.out.println(fileName);
//        Resource resource = fileStorageService.loadFileAsResource(fileName);
//
//        // Try to determine file's content type
//        String contentType = null;
//        try {
//            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
//        } catch (IOException ex) {
//            System.out.print("Could not determine file type.");
//        }
//
//        // Fallback to the default content type if type could not be determined
//        if(contentType == null) {
//            contentType = "application/octet-stream";
//        }
//
//        System.out.println("contentType");
//        System.out.println(contentType);
//        System.out.println("resource");
//        System.out.println(resource);
//        return ResponseEntity.ok()
//                .contentType(MediaType.parseMediaType(contentType))
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
//                .body(resource);
//    }

//    @CrossOrigin(origins = "http://localhost:4200")
//    @PostMapping(value = "/images/{fileName}", produces = "application/vnd.openxmlformats-officedocument.wordprocessingml.document")
//    public Resource getFileViaByteArrayResource(@PathVariable String fileName) throws IOException {
//        // Construct the file path based on the file name
//        System.out.println("acjraf");
//        String filePath = "static/images/" + fileName;
//
//        // Read the file content as byte array
//        Path path = Paths.get(filePath);
//        byte[] fileContent = Files.readAllBytes(path);
//
//        // Create a ByteArrayResource from the file content
//        ByteArrayResource resource = new ByteArrayResource(fileContent);
//
//        // Return the resource
//        return resource;
//    }


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
