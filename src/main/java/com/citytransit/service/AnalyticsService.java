package com.citytransit.service;

import com.citytransit.model.clickhouse.TransactionRecord;
import com.citytransit.repository.clickhouse.TransactionRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final TransactionRecordRepository transactionRecordRepository;
    private final JdbcTemplate clickHouseJdbcTemplate;

    public Map<String, Object> getDailyRevenue() {
        String sql = "SELECT toDate(transactionTime) as date, sum(amount) as revenue " +
                     "FROM transaction_records " +
                     "WHERE transactionTime >= today() - 30 " +
                     "GROUP BY date " +
                     "ORDER BY date";
        
        Map<String, Object> result = new HashMap<>();
        result.put("data", clickHouseJdbcTemplate.queryForList(sql));
        return result;
    }

    public Map<String, Object> getRevenueByRoute() {
        String sql = "SELECT routeId, sum(amount) as revenue " +
                     "FROM transaction_records " +
                     "WHERE transactionTime >= today() - 7 " +
                     "GROUP BY routeId " +
                     "ORDER BY revenue DESC";
        
        Map<String, Object> result = new HashMap<>();
        result.put("data", clickHouseJdbcTemplate.queryForList(sql));
        return result;
    }

    public Map<String, Object> getTransactionStats() {
        String sql = "SELECT " +
                     "count(*) as total, " +
                     "sum(amount) as totalAmount, " +
                     "avg(amount) as avgAmount " +
                     "FROM transaction_records " +
                     "WHERE transactionTime >= today()";
        
        return clickHouseJdbcTemplate.queryForMap(sql);
    }

    public void saveTransactionRecord(TransactionRecord record) {
        transactionRecordRepository.save(record);
    }
}
