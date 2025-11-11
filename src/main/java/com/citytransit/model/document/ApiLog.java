package com.citytransit.model.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Map;

@Document(collection = "api_logs")
@Data
public class ApiLog {
    @Id
    private String id;
    private LocalDateTime timestamp;
    private String method;
    private String path;
    private int status;
    private long duration;
    private String ipAddress;
    private Map<String, String> headers;
    private String requestBody;
    private String responseBody;
}
