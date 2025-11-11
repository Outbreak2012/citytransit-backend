package com.citytransit.dl.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Análisis de sentimiento de feedback de usuarios
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SentimentAnalysisRequest {
    private String texto;
    private Long usuarioId;
    private Long rutaId;
    private String contexto; // QUEJA, SUGERENCIA, FELICITACION, PREGUNTA
}
