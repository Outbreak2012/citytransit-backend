package com.citytransit.ml.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Clase para almacenar datos de entrenamiento de predicción de demanda
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DemandPredictionData {
    private Long rutaId;
    private LocalDateTime fechaHora;
    private Integer diaSemana; // 1=Lunes, 7=Domingo
    private Integer hora;
    private Integer mes;
    private Boolean esFeriado;
    private Boolean esFinDeSemana;
    private Double temperatura;
    private String condicionClimatica; // SOLEADO, LLUVIOSO, NUBLADO
    private Integer pasajerosValidados;
    private Double ocupacionPromedio;
    
    // Features derivados
    private Integer horaDelDia; // 0-23
    private Integer minutoDelDia; // 0-1439
    private Boolean esHoraPico; // 6-9am, 5-8pm
}
