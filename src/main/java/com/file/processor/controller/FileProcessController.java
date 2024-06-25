package com.file.processor.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.file.processor.aspect.IpRetrievable;
import com.file.processor.helper.FileValidator;
import com.file.processor.model.Content;
import com.file.processor.service.FileParserService;
import com.file.processor.service.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FileProcessController {
    private final FileParserService fileParserService;
    private final FileValidator fileValidator;

    private final ObjectMapper objectMapper;

    private final LogService logService;

    @PostMapping("/uploadFile")
    @IpRetrievable
    public ResponseEntity<String> uploadTxtFile(@RequestParam("file") MultipartFile file) throws JsonProcessingException {

        ResponseEntity<String> fileValidation = fileValidator.validate(file);
        if (fileValidation != null) return fileValidation;

        List<Content> outcomeRecords = fileParserService.parse(file);

        String jsonContent = objectMapper.writeValueAsString(outcomeRecords);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=outcomeRecords.json");

        return new ResponseEntity<>(jsonContent, headers, HttpStatus.OK);
    }


}
