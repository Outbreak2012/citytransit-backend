package com.citytransit.dl.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Análisis de ocupación de vehículo usando Computer Vision
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OccupancyAnalysisRequest {
    private Long vehiculoId;
    private String imagenBase64; // Imagen en base64
    private String imagenUrl; // O URL de la imagen
    private Long rutaId;
}
