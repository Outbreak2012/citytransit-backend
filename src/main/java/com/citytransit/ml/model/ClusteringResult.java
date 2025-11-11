package com.citytransit.ml.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Resultado de clustering de patrones de viaje
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClusteringResult {
    private Integer totalClusters;
    private List<UserCluster> clusters;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserCluster {
        private Integer clusterId;
        private String perfilUsuario; // COMMUTER, OCCASIONAL, TOURIST, STUDENT
        private Integer cantidadUsuarios;
        private Double frecuenciaPromedio; // viajes por semana
        private List<String> rutasFrecuentes;
        private String horaPreferida; // MORNING, AFTERNOON, EVENING, NIGHT
        private Double gastoPromedio; // gasto semanal promedio
        private List<String> recomendaciones;
    }
}
