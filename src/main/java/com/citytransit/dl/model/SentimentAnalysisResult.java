package com.citytransit.dl.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Resultado de análisis de sentimiento
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SentimentAnalysisResult {
    private String texto;
    private String sentimiento; // POSITIVO, NEGATIVO, NEUTRAL
    private Double confianza; // 0.0 - 1.0
    private Map<String, Double> scores; // Scores por categoría
    private String emocionPrincipal; // SATISFECHO, FRUSTRADO, ENOJADO, CONFUNDIDO
    private Integer prioridad; // 1-5 (5 = urgente)
    private String categoriaDetectada; // SERVICIO, LIMPIEZA, PUNTUALIDAD, CONDUCTOR, TARIFA
    private Boolean requiereAccion; // Si necesita atención inmediata
    private String respuestaSugerida; // Respuesta automática sugerida
}
