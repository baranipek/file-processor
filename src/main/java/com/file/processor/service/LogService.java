package com.file.processor.service;

import com.file.processor.model.RequestLog;
import com.file.processor.repository.RequestLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class LogService {

    private final RequestLogRepository requestLogRepository;

    public void saveRequestLog(RequestLog requestLog) {
        requestLogRepository.save(requestLog);
    }
}