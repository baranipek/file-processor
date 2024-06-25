package com.file.processor.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.file.processor.helper.FileValidator;
import com.file.processor.service.FileParserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class FileProcessControllerTest {

    @InjectMocks
    private FileProcessController fileProcessController;

    @Mock
    private FileParserService fileParserService;

    @Mock
    private FileValidator fileValidator;

    @Mock
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void uploadTxtFile_invalidFile_returnsErrorResponse() throws JsonProcessingException {
        MultipartFile mockFile = new MockMultipartFile("file", "test.txt", "text/plain", "test content".getBytes());
        ResponseEntity<String> expectedResponse = new ResponseEntity<>("Invalid file", HttpStatus.BAD_REQUEST);

        when(fileValidator.validate(mockFile)).thenReturn(expectedResponse);

        ResponseEntity<String> response = fileProcessController.uploadTxtFile(mockFile);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid file", response.getBody());
    }
}
