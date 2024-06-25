package com.file.processor.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.file.processor.model.Content;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileParserService {

    private final ObjectMapper objectMapper;

    public List<Content> parse(MultipartFile file) {
        List<Content> contentList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                contentList.add(processTxtFile(line));
            }
        } catch (IOException e) {
            log.error("Error while parsing file" + e.getMessage());
            throw new RuntimeException(e);
        }

        return contentList;
    }

    private Content processTxtFile(String line) {
        String[] fields = line.split("\\|");
        return new Content(fields[2], fields[4], new BigDecimal(fields[6].trim()));
    }

}
