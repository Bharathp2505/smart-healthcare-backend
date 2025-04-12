package io.bvb.smarthealthcare.backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileStorageService.class);
    private final String UPLOAD_DIR = "uploads/";

    public String storeFile(MultipartFile file) {
        try {
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR, filename);
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, file.getBytes());
            return "/images/" + filename; // URL used to serve images
        } catch (IOException e) {
            LOGGER.error("Failed to upload the file :: {}", e.getMessage());
            throw new RuntimeException("Failed to store file", e);
        }
    }
}

