package com.citytransit.dl.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Serie temporal para predicción con LSTM
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimeSeriesData {
    private Long rutaId;
    private List<LocalDateTime> timestamps;
    private List<Integer> pasajeros; // Histórico de pasajeros
    private List<Double> temperaturas;
    private List<String> condicionesClimaticas;
    private Integer windowSize; // Ventana de tiempo para predicción
    private Integer predictionHorizon; // Horizonte de predicción (en horas)
}
