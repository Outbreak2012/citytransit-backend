package com.citytransit.dl.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Resultado de predicción LSTM
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LSTMPredictionResult {
    private Long rutaId;
    private LocalDateTime fechaPrediccion;
    private List<PredictionPoint> predicciones;
    private Double confianzaPromedio;
    private String tendencia; // INCREASING, DECREASING, STABLE
    private Integer picoMaximo;
    private LocalDateTime horaPico;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PredictionPoint {
        private LocalDateTime timestamp;
        private Integer pasajerosPredichos;
        private Double confianza;
        private Double varianza;
    }
}
