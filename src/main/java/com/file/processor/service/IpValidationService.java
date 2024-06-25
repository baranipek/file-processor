package com.file.processor.service;

import com.file.processor.model.RequestLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class IpValidationService {

    private static final List<String> BLOCKED_COUNTRIES = Arrays.asList("China", "Spain", "United States");
    private static final List<String> BLOCKED_ISPS = Arrays.asList("Amazon", "Google", "Microsoft");

    private final RestTemplate restTemplate;

    public boolean isValidIp(String ipAddress, RequestLog requestLog) {

        String apiUrl = "http://ip-api.com/json/" + ipAddress;
        Map<String, String> response = restTemplate.getForObject(apiUrl, Map.class);

        if (response == null || response.get("status").equals("fail")) {
            return false;
        }

        String country = response.get("country");
        String isp = response.get("isp");
        requestLog.setRequestCountryCode(country);
        requestLog.setRequestIpProvider(isp);

        if (BLOCKED_COUNTRIES.contains(country)) {
            return false;
        }

        for (String blockedIsp : BLOCKED_ISPS) {
            if (isp != null && isp.toLowerCase().contains(blockedIsp.toLowerCase())) {
                return false;
            }
        }
        return true;
    }
}
