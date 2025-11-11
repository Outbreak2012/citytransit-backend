package com.citytransit.repository.clickhouse;

import com.citytransit.model.clickhouse.TransactionRecord;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TransactionRecordRepository {

    private final JdbcTemplate clickHouseJdbcTemplate;

    public TransactionRecordRepository(JdbcTemplate clickHouseJdbcTemplate) {
        this.clickHouseJdbcTemplate = clickHouseJdbcTemplate;
    }

    public void save(TransactionRecord record) {
        String sql = "INSERT INTO transaction_records (transactionId, cardId, transactionType, amount, transactionTime, vehicleId, routeId) VALUES (?, ?, ?, ?, ?, ?, ?)";
        clickHouseJdbcTemplate.update(sql,
                record.getTransactionId(),
                record.getCardId(),
                record.getTransactionType(),
                record.getAmount(),
                record.getTransactionTime(),
                record.getVehicleId(),
                record.getRouteId());
    }
}
