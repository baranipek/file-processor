package com.file.processor.helper;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

public class FileValidatorTests {

    private FileValidator fileValidator;

    @Mock
    private MultipartFile mockFile;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        fileValidator = new FileValidator();
    }

    @Test
    public void testEmptyFileValidation() {
        // Prepare mock file with isEmpty() returning true
        when(mockFile.isEmpty()).thenReturn(true);

        // Perform validation
        ResponseEntity<String> response = fileValidator.validate(mockFile);

        // Assert response
        assertEquals("Uploaded file is empty", response.getBody());
        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
    }

    @Test
    public void testInvalidFileTypeValidation() {

        when(mockFile.isEmpty()).thenReturn(false);

        // Prepare mock file with .pdf extension
        MockMultipartFile file = new MockMultipartFile("test.pdf", "test.pdf", "text/plain", "Mock PDF Content".getBytes()
        );

        // Perform validation
        ResponseEntity<String> response = fileValidator.validate(file);

        // Assert response
        assertEquals("Only .txt files are allowed", response.getBody());
        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
    }

    @Test
    public void testNoFileTypeValidation() {

        // Prepare mock file with .pdf extension
        MockMultipartFile file = new MockMultipartFile("test", "test", "text/plain", "Mock PDF Content".getBytes()
        );

        // Perform validation
        ResponseEntity<String> response = fileValidator.validate(file);

        // Assert response
        assertEquals("Only .txt files are allowed", response.getBody());
        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
    }

    @Test
    public void testValidFileValidation() {
        // Prepare mock file with .txt extension
        MockMultipartFile file = new MockMultipartFile("test.txt", "test.txt", "text/plain", "Mock TXT Content".getBytes());

        // Perform validation
        ResponseEntity<String> response = fileValidator.validate(file);

        // Assert response
        assertNull(response);
    }
}
