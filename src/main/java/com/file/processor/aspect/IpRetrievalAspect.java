package com.file.processor.aspect;

import com.file.processor.model.RequestLog;
import com.file.processor.service.IpValidationService;
import com.file.processor.service.LogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Aspect
@Component
@RequiredArgsConstructor
public class IpRetrievalAspect {
    private final IpValidationService ipValidationService;
    private final HttpServletRequest request;

    @Value("${skipValidation:false}")
    private boolean skipValidation;

    private final LogService logService;

    private static final ThreadLocal<RequestLog> requestLogThreadLocal = new ThreadLocal<>();

    @Around("@annotation(com.file.processor.aspect.IpRetrievable)")
    public Object validateIp(ProceedingJoinPoint joinPoint) throws Throwable {

        RequestLog requestLog = new RequestLog();
        requestLog.setRequestId(UUID.randomUUID());
        requestLog.setStartTime(System.currentTimeMillis());
        requestLog.setRequestUri(request.getRequestURI());
        requestLog.setRequestTimestamp(Timestamp.from(Instant.now()));
        requestLogThreadLocal.set(requestLog);

        try {
            if (!skipValidation) {
                String ipAddress = request.getRemoteAddr();
                requestLog.setRequestIpAddress(ipAddress);

                if (!ipValidationService.isValidIp(ipAddress, requestLog)) {
                    requestLog.setHttpResponseCode(403);
                    requestLog.setTimeLapsed(System.currentTimeMillis() - requestLog.getStartTime());
                    logService.saveRequestLog(requestLog);
                    return new ResponseEntity<>("Access denied due to invalid IP address", HttpStatus.FORBIDDEN);
                }
            }

            Object result = joinPoint.proceed();

            requestLog.setHttpResponseCode(HttpStatus.OK.value());
            return result;

        } finally {
            // Ensure the log entry is saved and ThreadLocal is cleared
            requestLog.setTimeLapsed(System.currentTimeMillis() - requestLog.getStartTime());
            logService.saveRequestLog(requestLog);
            requestLogThreadLocal.remove();
        }
    }

    public static RequestLog getRequestLog() {
        return requestLogThreadLocal.get();
    }
}
