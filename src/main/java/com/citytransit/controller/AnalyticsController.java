package com.citytransit.controller;

import com.citytransit.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/revenue/daily")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    public ResponseEntity<Map<String, Object>> getDailyRevenue() {
        return ResponseEntity.ok(analyticsService.getDailyRevenue());
    }

    @GetMapping("/revenue/by-route")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    public ResponseEntity<Map<String, Object>> getRevenueByRoute() {
        return ResponseEntity.ok(analyticsService.getRevenueByRoute());
    }

    @GetMapping("/transactions/stats")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    public ResponseEntity<Map<String, Object>> getTransactionStats() {
        return ResponseEntity.ok(analyticsService.getTransactionStats());
    }
}
