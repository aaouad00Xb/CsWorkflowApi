package com.example.workflow.Services;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.example.workflow.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;


@Service
public class FileStorageService {





    public Resource loadFileAsResource(String fileName) throws IOException {
        // Construct the file path relative to the static/images directory

        String path = new ClassPathResource("static/").getFile().getAbsolutePath();

        System.out.println("path");
        System.out.println(path);

        Path filePath = Paths.get(path + File.separator+ fileName);
        System.out.println("filePath");
        System.out.println(filePath);

        // Check if the file exists
        if (Files.exists(filePath)) {
            return new ClassPathResource("static/" + fileName);
        } else {
            System.out.println("File not found: " + fileName);
            return null;
        }
    }


}