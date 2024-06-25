package com.file.processor.service;

import com.file.processor.model.Content;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class FileParserServiceTests {

    @Mock
    private MultipartFile mockFile;

    @InjectMocks
    private FileParserService fileParserService;

    @Test
    public void testParseFile() throws IOException {

        String fileContent = "18148426-89e1-11ee-b9d1-0242ac120002|1X1D14|John Smith|Likes Apricots|Rides A Bike|6.2|12.1\n" +
                "3ce2d17b-e66a-4c1e-bca3-40eb1c9222c7|2X2D24|Mike Smith|Likes Grape|Drives an SUV|35.0|95.5\n" +
                "1afb6f5d-a7c2-4311-a92d-974f3180ff5e|3X3D35|Jenny Walters|Likes Avocados|Rides A Scooter|8.5|15.3";

        // Create mock MultipartFile
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes());
        mockFile = new MockMultipartFile("test.txt", "test.txt", "text/plain", inputStream);

        // Perform parsing
        List<Content> parsedContent = fileParserService.parse(mockFile);

        // Assertions
        assertEquals(3, parsedContent.size());

        // Verify individual records
        Content firstRecord = parsedContent.get(0);
        assertEquals("John Smith", firstRecord.name());
        assertEquals("Rides A Bike", firstRecord.transport());
        assertEquals(new BigDecimal("12.1"), firstRecord.topSpeed());

    }

    @Test
    public void testWrongParseFile() throws IOException {

        String fileContent = "18148426-89e1-11ee-b9d1-0242ac120002|1X1D14Likes Apricots|Rides A Bike|6.2|12.1\n" +
                "3ce2d17b-e66a-4c1e-bca3-40eb1c9222c7|2X2D24|Mike Smith|Likes Grape|Drives an SUV|35.0|95.5\n" +
                "wrongContent";

        // Create mock MultipartFile
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes());
        mockFile = new MockMultipartFile("test.txt", "test.txt", "text/plain", inputStream);

        // Perform parsing and expect RuntimeException
        RuntimeException exception = assertThrows(RuntimeException.class, () -> fileParserService.parse(mockFile));

        // Verify the exception message (optional)
        assertNotNull(exception.getMessage());
    }

}

