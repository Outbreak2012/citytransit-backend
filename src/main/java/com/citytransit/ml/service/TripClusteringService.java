package com.citytransit.ml.service;

import com.citytransit.ml.model.ClusteringResult;
import com.citytransit.ml.model.ClusteringResult.UserCluster;
import com.citytransit.ml.model.TripPattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import smile.clustering.KMeans;

import jakarta.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Servicio de clustering de patrones de viaje usando K-means
 */
@Slf4j
@Service
public class TripClusteringService {

    private KMeans kmeansModel;
    private boolean modelTrained = false;
    private static final int DEFAULT_K = 4; // 4 clusters por defecto

    @PostConstruct
    public void init() {
        log.info("Inicializando servicio de clustering de viajes con K-means");
    }

    /**
     * Entrena el modelo K-means con patrones de viaje
     */
    public void trainModel(List<TripPattern> patterns) {
        try {
            log.info("Entrenando modelo K-means con {} patrones", patterns.size());

            if (patterns.size() < 50) {
                log.warn("Datos insuficientes para clustering (mínimo 50 registros)");
                return;
            }

            // Preparar datos
            int n = patterns.size();
            double[][] features = new double[n][8];

            for (int i = 0; i < n; i++) {
                features[i] = extractFeatures(patterns.get(i));
            }

            // Entrenar K-means
            // Parámetros: k=4 (número de clusters), maxIter=100, tol=1e-4
            kmeansModel = KMeans.fit(features, DEFAULT_K, 100, 1e-4);

            modelTrained = true;
            log.info("Modelo K-means entrenado exitosamente con {} clusters", DEFAULT_K);

        } catch (Exception e) {
            log.error("Error entrenando modelo K-means", e);
        }
    }

    /**
     * Agrupa usuarios por patrones de viaje
     */
    public ClusteringResult clusterUsers(List<TripPattern> patterns) {
        if (!modelTrained) {
            log.warn("Modelo no entrenado, usando clustering por defecto");
            return getDefaultClustering(patterns);
        }

        try {
            // Preparar datos
            Map<Integer, List<TripPattern>> clusterMap = new HashMap<>();
            
            for (TripPattern pattern : patterns) {
                double[] features = extractFeatures(pattern);
                int clusterId = kmeansModel.predict(features);
                
                clusterMap.computeIfAbsent(clusterId, k -> new ArrayList<>()).add(pattern);
            }

            // Analizar cada cluster
            List<UserCluster> clusters = new ArrayList<>();
            for (Map.Entry<Integer, List<TripPattern>> entry : clusterMap.entrySet()) {
                UserCluster cluster = analyzeCluster(entry.getKey(), entry.getValue());
                clusters.add(cluster);
            }

            return ClusteringResult.builder()
                    .totalClusters(clusters.size())
                    .clusters(clusters)
                    .build();

        } catch (Exception e) {
            log.error("Error en clustering de usuarios", e);
            return getDefaultClustering(patterns);
        }
    }

    /**
     * Predice el cluster de un patrón de viaje
     */
    public Integer predictCluster(TripPattern pattern) {
        if (!modelTrained) {
            return 0;
        }

        double[] features = extractFeatures(pattern);
        return kmeansModel.predict(features);
    }

    /**
     * Extrae features del patrón de viaje
     */
    private double[] extractFeatures(TripPattern pattern) {
        return new double[]{
                pattern.getDiaSemana().doubleValue(),                    // 1-7
                pattern.getHora().doubleValue(),                         // 0-23
                pattern.getDistancia() != null ? pattern.getDistancia() : 0.0, // km
                pattern.getDuracionMinutos().doubleValue(),              // minutos
                pattern.getCostoViaje(),                                 // precio
                pattern.getFrecuenciaSemanal().doubleValue(),            // viajes/semana
                calculateTimeScore(pattern.getHora()),                   // score de hora
                pattern.getDiaSemana() > 5 ? 1.0 : 0.0                  // fin de semana
        };
    }

    private double calculateTimeScore(Integer hora) {
        // Morning peak: 6-9 = 3, Afternoon peak: 17-20 = 2, Other = 1
        if (hora >= 6 && hora <= 9) return 3.0;
        if (hora >= 17 && hora <= 20) return 2.0;
        return 1.0;
    }

    /**
     * Analiza un cluster y genera perfil de usuario
     */
    private UserCluster analyzeCluster(Integer clusterId, List<TripPattern> patterns) {
        // Calcular estadísticas del cluster
        double avgFrequency = patterns.stream()
                .mapToInt(TripPattern::getFrecuenciaSemanal)
                .average()
                .orElse(0.0);

        double avgCost = patterns.stream()
                .mapToDouble(TripPattern::getCostoViaje)
                .average()
                .orElse(0.0);

        // Obtener rutas más frecuentes
        Map<Long, Long> routeFrequency = patterns.stream()
                .collect(Collectors.groupingBy(TripPattern::getRutaId, Collectors.counting()));

        List<String> frequentRoutes = routeFrequency.entrySet().stream()
                .sorted(Map.Entry.<Long, Long>comparingByValue().reversed())
                .limit(3)
                .map(e -> "Ruta " + e.getKey())
                .collect(Collectors.toList());

        // Determinar hora preferida
        String preferredTime = determinePreferredTime(patterns);

        // Determinar perfil de usuario
        String userProfile = determineUserProfile(avgFrequency, patterns);

        // Generar recomendaciones
        List<String> recommendations = generateRecommendations(userProfile, avgFrequency, avgCost);

        return UserCluster.builder()
                .clusterId(clusterId)
                .perfilUsuario(userProfile)
                .cantidadUsuarios(patterns.size())
                .frecuenciaPromedio(avgFrequency)
                .rutasFrecuentes(frequentRoutes)
                .horaPreferida(preferredTime)
                .gastoPromedio(avgCost * avgFrequency)
                .recomendaciones(recommendations)
                .build();
    }

    private String determinePreferredTime(List<TripPattern> patterns) {
        Map<String, Long> timeDistribution = new HashMap<>();
        
        for (TripPattern pattern : patterns) {
            int hora = pattern.getHora();
            String timeSlot;
            if (hora >= 6 && hora < 12) timeSlot = "MORNING";
            else if (hora >= 12 && hora < 17) timeSlot = "AFTERNOON";
            else if (hora >= 17 && hora < 21) timeSlot = "EVENING";
            else timeSlot = "NIGHT";
            
            timeDistribution.merge(timeSlot, 1L, Long::sum);
        }

        return timeDistribution.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("UNKNOWN");
    }

    private String determineUserProfile(double avgFrequency, List<TripPattern> patterns) {
        // COMMUTER: Alta frecuencia, horarios regulares
        if (avgFrequency >= 8) {
            return "COMMUTER"; // Usuario diario (trabajo/estudio)
        }
        
        // STUDENT: Frecuencia moderada, horarios de estudio
        boolean hasStudentPattern = patterns.stream()
                .anyMatch(p -> (p.getHora() >= 7 && p.getHora() <= 9) || 
                               (p.getHora() >= 13 && p.getHora() <= 15));
        if (avgFrequency >= 4 && avgFrequency < 8 && hasStudentPattern) {
            return "STUDENT";
        }

        // OCCASIONAL: Baja frecuencia
        if (avgFrequency < 4) {
            return "OCCASIONAL";
        }

        // TOURIST: Patrones variables, fin de semana
        boolean hasWeekendPattern = patterns.stream()
                .anyMatch(p -> p.getDiaSemana() > 5);
        if (hasWeekendPattern) {
            return "TOURIST";
        }

        return "REGULAR";
    }

    private List<String> generateRecommendations(String profile, double frequency, double avgCost) {
        List<String> recommendations = new ArrayList<>();

        switch (profile) {
            case "COMMUTER":
                recommendations.add("💳 Suscripción mensual - Ahorra hasta 30%");
                recommendations.add("⏰ Viaja fuera de hora pico - Descuento 15%");
                recommendations.add("🎯 Plan corporativo disponible");
                break;
            case "STUDENT":
                recommendations.add("🎓 Tarjeta estudiantil - 50% descuento");
                recommendations.add("📚 Paquete semana académica");
                break;
            case "OCCASIONAL":
                recommendations.add("💰 Recarga $10 y recibe $2 extra");
                recommendations.add("🎫 Pases de día disponibles");
                break;
            case "TOURIST":
                recommendations.add("🌍 Pase turístico 3 días ilimitados");
                recommendations.add("🗺️ Rutas recomendadas para ti");
                break;
            default:
                recommendations.add("📱 Descarga la app para más beneficios");
        }

        // Recomendación basada en gasto
        if (avgCost * frequency > 50) {
            recommendations.add("💡 Una suscripción mensual te ahorraría dinero");
        }

        return recommendations;
    }

    private ClusteringResult getDefaultClustering(List<TripPattern> patterns) {
        // Clustering simple por frecuencia cuando el modelo no está entrenado
        Map<String, List<TripPattern>> simpleGroups = new HashMap<>();
        
        for (TripPattern pattern : patterns) {
            String group;
            int freq = pattern.getFrecuenciaSemanal();
            if (freq >= 8) group = "HIGH_FREQUENCY";
            else if (freq >= 4) group = "MEDIUM_FREQUENCY";
            else group = "LOW_FREQUENCY";
            
            simpleGroups.computeIfAbsent(group, k -> new ArrayList<>()).add(pattern);
        }

        List<UserCluster> clusters = new ArrayList<>();
        int clusterId = 0;
        for (Map.Entry<String, List<TripPattern>> entry : simpleGroups.entrySet()) {
            UserCluster cluster = analyzeCluster(clusterId++, entry.getValue());
            clusters.add(cluster);
        }

        return ClusteringResult.builder()
                .totalClusters(clusters.size())
                .clusters(clusters)
                .build();
    }

    public boolean isModelTrained() {
        return modelTrained;
    }
}
