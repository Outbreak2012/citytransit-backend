package com.citytransit.service;

import com.citytransit.model.document.ApiLog;
import com.citytransit.repository.mongo.ApiLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LoggingService {

    private final ApiLogRepository apiLogRepository;

    public void logApiRequest(String method, String path, int status, long duration, 
                              String ipAddress, Map<String, String> headers,
                              String requestBody, String responseBody) {
        ApiLog log = new ApiLog();
        log.setTimestamp(LocalDateTime.now());
        log.setMethod(method);
        log.setPath(path);
        log.setStatus(status);
        log.setDuration(duration);
        log.setIpAddress(ipAddress);
        log.setHeaders(headers);
        log.setRequestBody(requestBody);
        log.setResponseBody(responseBody);
        
        apiLogRepository.save(log);
    }

    public List<ApiLog> getRecentLogs(int limit) {
        return apiLogRepository.findAll().stream()
                .limit(limit)
                .toList();
    }
}
