package com.citytransit.model.clickhouse;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TransactionRecord {
    private long transactionId;
    private long cardId;
    private String transactionType;
    private double amount;
    private LocalDateTime transactionTime;
    private String vehicleId;
    private String routeId;
}
