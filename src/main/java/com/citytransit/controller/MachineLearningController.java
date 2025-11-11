package com.citytransit.controller;

import com.citytransit.ml.model.ClusteringResult;
import com.citytransit.ml.model.DemandPredictionData;
import com.citytransit.ml.model.DemandPredictionResult;
import com.citytransit.ml.service.DemandPredictionService;
import com.citytransit.ml.service.TripClusteringService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ml")
@RequiredArgsConstructor
public class MachineLearningController {

    private final DemandPredictionService demandPredictionService;
    private final TripClusteringService tripClusteringService;

    /**
     * Predice la demanda para una ruta específica
     */
    @PostMapping("/predict-demand")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    public ResponseEntity<DemandPredictionResult> predictDemand(
            @RequestBody DemandPredictionData predictionData) {
        
        // Enriquecer datos si faltan campos
        enrichPredictionData(predictionData);
        
        DemandPredictionResult result = demandPredictionService.predictDemand(predictionData);
        return ResponseEntity.ok(result);
    }

    /**
     * Predice la demanda para múltiples rutas
     */
    @PostMapping("/predict-demand/batch")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    public ResponseEntity<List<DemandPredictionResult>> predictDemandBatch(
            @RequestBody List<DemandPredictionData> predictions) {
        
        predictions.forEach(this::enrichPredictionData);
        List<DemandPredictionResult> results = demandPredictionService.predictMultipleRoutes(predictions);
        return ResponseEntity.ok(results);
    }

    /**
     * Obtiene el estado de los modelos de ML
     */
    @GetMapping("/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    public ResponseEntity<Map<String, Object>> getModelStatus() {
        Map<String, Object> status = Map.of(
                "demandPredictionModel", Map.of(
                        "trained", demandPredictionService.isModelTrained(),
                        "type", "Random Forest",
                        "description", "Predice demanda de pasajeros por ruta"
                ),
                "tripClusteringModel", Map.of(
                        "trained", tripClusteringService.isModelTrained(),
                        "type", "K-means",
                        "description", "Agrupa usuarios por patrones de viaje"
                )
        );
        return ResponseEntity.ok(status);
    }

    /**
     * Endpoint simplificado para predicción rápida
     */
    @GetMapping("/predict-demand/quick")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR', 'PASAJERO')")
    public ResponseEntity<DemandPredictionResult> quickPredict(
            @RequestParam Long rutaId,
            @RequestParam(required = false) LocalDateTime fechaHora) {
        
        LocalDateTime targetTime = fechaHora != null ? fechaHora : LocalDateTime.now().plusHours(1);
        
        DemandPredictionData data = DemandPredictionData.builder()
                .rutaId(rutaId)
                .fechaHora(targetTime)
                .diaSemana(targetTime.getDayOfWeek().getValue())
                .hora(targetTime.getHour())
                .mes(targetTime.getMonthValue())
                .esFeriado(false) // Simplificado
                .esFinDeSemana(targetTime.getDayOfWeek().getValue() > 5)
                .temperatura(20.0) // Valor por defecto
                .condicionClimatica("SOLEADO") // Valor por defecto
                .horaDelDia(targetTime.getHour())
                .minutoDelDia(targetTime.getHour() * 60 + targetTime.getMinute())
                .esHoraPico(isRushHour(targetTime.getHour()))
                .build();
        
        DemandPredictionResult result = demandPredictionService.predictDemand(data);
        return ResponseEntity.ok(result);
    }

    /**
     * Endpoint para obtener recomendaciones de usuario basadas en clustering
     */
    @GetMapping("/user-recommendations/{usuarioId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR', 'PASAJERO')")
    public ResponseEntity<Map<String, Object>> getUserRecommendations(@PathVariable Long usuarioId) {
        // TODO: Implementar cuando tengamos datos históricos del usuario
        Map<String, Object> recommendations = Map.of(
                "userId", usuarioId,
                "profile", "COMMUTER",
                "recommendations", List.of(
                        "💳 Suscripción mensual - Ahorra hasta 30%",
                        "⏰ Viaja fuera de hora pico - Descuento 15%"
                ),
                "estimatedMonthlySavings", 15.50
        );
        return ResponseEntity.ok(recommendations);
    }

    // Helper methods

    private void enrichPredictionData(DemandPredictionData data) {
        if (data.getFechaHora() == null) {
            data.setFechaHora(LocalDateTime.now());
        }
        
        LocalDateTime dt = data.getFechaHora();
        
        if (data.getDiaSemana() == null) {
            data.setDiaSemana(dt.getDayOfWeek().getValue());
        }
        if (data.getHora() == null) {
            data.setHora(dt.getHour());
        }
        if (data.getMes() == null) {
            data.setMes(dt.getMonthValue());
        }
        if (data.getEsFeriado() == null) {
            data.setEsFeriado(false); // Simplificado
        }
        if (data.getEsFinDeSemana() == null) {
            data.setEsFinDeSemana(dt.getDayOfWeek().getValue() > 5);
        }
        if (data.getHoraDelDia() == null) {
            data.setHoraDelDia(dt.getHour());
        }
        if (data.getMinutoDelDia() == null) {
            data.setMinutoDelDia(dt.getHour() * 60 + dt.getMinute());
        }
        if (data.getEsHoraPico() == null) {
            data.setEsHoraPico(isRushHour(dt.getHour()));
        }
        if (data.getTemperatura() == null) {
            data.setTemperatura(20.0); // Valor por defecto
        }
        if (data.getCondicionClimatica() == null) {
            data.setCondicionClimatica("SOLEADO");
        }
    }

    private boolean isRushHour(int hour) {
        return (hour >= 6 && hour <= 9) || (hour >= 17 && hour <= 20);
    }
}
