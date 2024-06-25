package com.file.processor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.file.processor.helper.FileValidator;
import com.file.processor.service.FileParserService;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@WireMockTest
public class FileProcessControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private FileParserService fileParserService;

    @Mock
    private FileValidator fileValidator;

    @Mock
    private ObjectMapper objectMapper;

    @Test
    public void testUploadFile_ValidIp() throws Exception {
        WireMock.stubFor(get(WireMock.urlEqualTo("/json/24.48.0.5"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{ \"status\": \"success\", \"country\": \"Some Country\", \"isp\": \"Some ISP\" }")));


        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "18148426-89e1-11ee-b9d1-0242ac120002|1X1D14|John Smith|Likes Apricots|Rides A Bike|6.2|12.1".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/uploadFile")
                        .file(file)
                        .with(request -> {
                            request.setRemoteAddr("24.48.0.5");
                            return request;
                        }))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(header().string("Content-Disposition", "attachment; filename=outcomeRecords.json"));
    }

    @Test
    public void testUploadFile_InValidIp_China() throws Exception {
        WireMock.stubFor(get(WireMock.urlEqualTo("/json/1.0.15.255"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{ \"status\": \"success\", \"country\": \"Some Country\", \"isp\": \"Some ISP\" }")));


        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "18148426-89e1-11ee-b9d1-0242ac120002|1X1D14|John Smith|Likes Apricots|Rides A Bike|6.2|12.1".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/uploadFile")
                        .file(file)
                        .with(request -> {
                            request.setRemoteAddr("1.0.15.255");
                            return request;
                        }))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testUploadFile_InValidIp_Aws_Isp() throws Exception {
        WireMock.stubFor(get(WireMock.urlEqualTo("/json/13.52.0.0"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{ \"status\": \"success\", \"country\": \"Some Country\", \"isp\": \"Some ISP\" }")));


        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "18148426-89e1-11ee-b9d1-0242ac120002|1X1D14|John Smith|Likes Apricots|Rides A Bike|6.2|12.1".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/uploadFile")
                        .file(file)
                        .with(request -> {
                            request.setRemoteAddr("13.52.0.0");
                            return request;
                        }))
                .andExpect(status().isForbidden());
    }
}
