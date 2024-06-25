package com.file.processor.helper;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Component
public class FileValidator {
    public ResponseEntity<String> validate(MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Uploaded file is empty");
        }
        if (!Objects.requireNonNull(file.getOriginalFilename()).endsWith(".txt") || file.getOriginalFilename() == null) {
            return ResponseEntity.badRequest().body("Only .txt files are allowed");
        }

        return null;
    }
}
