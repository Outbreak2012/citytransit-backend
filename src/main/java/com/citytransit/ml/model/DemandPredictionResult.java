package com.citytransit.ml.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Resultado de predicción de demanda
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DemandPredictionResult {
    private Long rutaId;
    private Integer pasajerosPredichos;
    private Double confianza; // 0.0 - 1.0
    private Double ocupacionPredicha; // 0.0 - 1.0
    private String nivelDemanda; // BAJA, MEDIA, ALTA, MUY_ALTA
    private String recomendacion; // Aumentar frecuencia, Normal, Reducir frecuencia
}
