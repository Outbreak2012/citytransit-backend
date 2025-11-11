package com.citytransit.dl.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Resultado de análisis de ocupación
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OccupancyAnalysisResult {
    private Long vehiculoId;
    private Integer personasDetectadas;
    private Integer capacidadMaxima;
    private Double porcentajeOcupacion; // 0.0 - 1.0
    private String nivelOcupacion; // VACIO, BAJO, MEDIO, ALTO, LLENO, SOBRECARGADO
    private List<PersonLocation> ubicacionesPersonas;
    private String alertaSeguridad; // Si hay sobrecarga
    private Boolean requiereVehiculoAdicional;
    private Double confianzaDeteccion;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PersonLocation {
        private Integer x;
        private Integer y;
        private Integer width;
        private Integer height;
        private Double confidence;
    }
}
