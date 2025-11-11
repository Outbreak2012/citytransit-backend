package com.citytransit.ml.service;

import com.citytransit.ml.model.DemandPredictionData;
import com.citytransit.ml.model.TripPattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Servicio para entrenar modelos de ML con datos sintéticos
 * En producción, esto usaría datos reales de la base de datos
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MLTrainingService {

    private final DemandPredictionService demandPredictionService;
    private final TripClusteringService tripClusteringService;
    private static final Random random = new Random(42); // Seed para reproducibilidad

    @EventListener(ApplicationReadyEvent.class)
    public void trainModelsOnStartup() {
        log.info("🤖 Iniciando entrenamiento de modelos de Machine Learning...");
        
        // Entrenar modelo de predicción de demanda
        trainDemandPredictionModel();
        
        // Entrenar modelo de clustering
        trainClusteringModel();
        
        log.info("✅ Entrenamiento de modelos completado");
    }

    /**
     * Entrena el modelo de predicción de demanda con datos sintéticos
     */
    public void trainDemandPredictionModel() {
        try {
            log.info("📊 Generando datos de entrenamiento para predicción de demanda...");
            
            List<DemandPredictionData> trainingData = generateDemandTrainingData(500);
            
            log.info("🧠 Entrenando modelo Random Forest...");
            demandPredictionService.trainModel(trainingData);
            
            if (demandPredictionService.isModelTrained()) {
                log.info("✅ Modelo de predicción de demanda entrenado exitosamente");
                
                // Evaluar modelo
                List<DemandPredictionData> testData = generateDemandTrainingData(100);
                double accuracy = demandPredictionService.evaluateModel(testData);
                log.info("📈 Precisión del modelo: {}", String.format("%.2f%%", accuracy * 100));
            }
            
        } catch (Exception e) {
            log.error("❌ Error entrenando modelo de predicción de demanda", e);
        }
    }

    /**
     * Entrena el modelo de clustering con datos sintéticos
     */
    public void trainClusteringModel() {
        try {
            log.info("📊 Generando datos de entrenamiento para clustering...");
            
            List<TripPattern> trainingData = generateTripPatterns(200);
            
            log.info("🧠 Entrenando modelo K-means...");
            tripClusteringService.trainModel(trainingData);
            
            if (tripClusteringService.isModelTrained()) {
                log.info("✅ Modelo de clustering entrenado exitosamente");
            }
            
        } catch (Exception e) {
            log.error("❌ Error entrenando modelo de clustering", e);
        }
    }

    /**
     * Genera datos sintéticos de demanda
     */
    private List<DemandPredictionData> generateDemandTrainingData(int size) {
        List<DemandPredictionData> data = new ArrayList<>();
        
        LocalDateTime baseDate = LocalDateTime.now().minusMonths(3);
        
        for (int i = 0; i < size; i++) {
            LocalDateTime dateTime = baseDate.plusHours(random.nextInt(2160)); // 3 meses
            int dayOfWeek = dateTime.getDayOfWeek().getValue();
            int hour = dateTime.getHour();
            boolean isWeekend = dayOfWeek > 5;
            boolean isHoliday = random.nextDouble() < 0.05; // 5% probabilidad de feriado
            boolean isRushHour = (hour >= 6 && hour <= 9) || (hour >= 17 && hour <= 20);
            
            // Calcular pasajeros basado en patrones
            int basePassengers = 10;
            
            // Factor hora pico
            if (isRushHour) basePassengers += 25;
            
            // Factor fin de semana (menos pasajeros)
            if (isWeekend) basePassengers -= 8;
            
            // Factor feriado (menos pasajeros)
            if (isHoliday) basePassengers -= 12;
            
            // Factor por hora del día
            if (hour >= 12 && hour <= 14) basePassengers += 8; // Hora de almuerzo
            if (hour >= 21 || hour <= 5) basePassengers -= 7;  // Noche/madrugada
            
            // Agregar variación aleatoria ±20%
            int variation = (int) (basePassengers * 0.2 * (random.nextDouble() - 0.5) * 2);
            int passengers = Math.max(1, basePassengers + variation);
            
            // Temperatura simulada
            double temperature = 15 + random.nextDouble() * 20; // 15-35°C
            
            // Condición climática
            String weather = getRandomWeather();
            if (weather.equals("LLUVIOSO")) {
                passengers += 5; // Más gente usa transporte público cuando llueve
            }
            
            DemandPredictionData record = DemandPredictionData.builder()
                    .rutaId((long) (1 + random.nextInt(5))) // 5 rutas
                    .fechaHora(dateTime)
                    .diaSemana(dayOfWeek)
                    .hora(hour)
                    .mes(dateTime.getMonthValue())
                    .esFeriado(isHoliday)
                    .esFinDeSemana(isWeekend)
                    .temperatura(temperature)
                    .condicionClimatica(weather)
                    .pasajerosValidados(passengers)
                    .ocupacionPromedio(passengers / 40.0)
                    .horaDelDia(hour)
                    .minutoDelDia(hour * 60 + dateTime.getMinute())
                    .esHoraPico(isRushHour)
                    .build();
            
            data.add(record);
        }
        
        log.info("✅ Generados {} registros de datos de demanda", data.size());
        return data;
    }

    /**
     * Genera patrones de viaje sintéticos
     */
    private List<TripPattern> generateTripPatterns(int size) {
        List<TripPattern> patterns = new ArrayList<>();
        
        // Generar diferentes tipos de usuarios
        int numUsers = size / 4; // 4 viajes por usuario en promedio
        
        for (int userId = 1; userId <= numUsers; userId++) {
            // Determinar tipo de usuario
            String userType = getUserType(random.nextDouble());
            
            // Generar viajes según el tipo
            int numTrips = getNumTripsForUserType(userType);
            
            for (int trip = 0; trip < numTrips; trip++) {
                TripPattern pattern = generateTripForUserType(userId, userType);
                patterns.add(pattern);
            }
        }
        
        log.info("✅ Generados {} patrones de viaje", patterns.size());
        return patterns;
    }

    private String getUserType(double random) {
        if (random < 0.4) return "COMMUTER";      // 40% commuters
        if (random < 0.6) return "STUDENT";       // 20% students
        if (random < 0.85) return "REGULAR";      // 25% regular
        return "OCCASIONAL";                       // 15% occasional
    }

    private int getNumTripsForUserType(String userType) {
        return switch (userType) {
            case "COMMUTER" -> 4 + random.nextInt(3);  // 4-6 viajes
            case "STUDENT" -> 3 + random.nextInt(2);   // 3-4 viajes
            case "REGULAR" -> 2 + random.nextInt(2);   // 2-3 viajes
            case "OCCASIONAL" -> 1 + random.nextInt(2); // 1-2 viajes
            default -> 2;
        };
    }

    private TripPattern generateTripForUserType(int userId, String userType) {
        LocalDateTime now = LocalDateTime.now();
        
        int dayOfWeek;
        int hour;
        int frequency;
        
        switch (userType) {
            case "COMMUTER":
                // Viajes de lunes a viernes, horas pico
                dayOfWeek = 1 + random.nextInt(5); // Lunes-Viernes
                hour = random.nextBoolean() ? 
                        (7 + random.nextInt(2)) :    // 7-8am
                        (17 + random.nextInt(2));    // 5-6pm
                frequency = 10; // 10 viajes por semana (ida y vuelta diario)
                break;
            
            case "STUDENT":
                // Viajes de lunes a viernes, horarios académicos
                dayOfWeek = 1 + random.nextInt(5);
                hour = random.nextBoolean() ?
                        (8 + random.nextInt(2)) :    // 8-9am
                        (14 + random.nextInt(2));    // 2-3pm
                frequency = 6; // 6 viajes por semana
                break;
            
            case "REGULAR":
                // Viajes variados
                dayOfWeek = 1 + random.nextInt(7);
                hour = 9 + random.nextInt(10); // 9am-6pm
                frequency = 4; // 4 viajes por semana
                break;
            
            case "OCCASIONAL":
                // Viajes esporádicos, incluye fines de semana
                dayOfWeek = 1 + random.nextInt(7);
                hour = 10 + random.nextInt(8); // 10am-5pm
                frequency = 2; // 2 viajes por semana
                break;
            
            default:
                dayOfWeek = 1 + random.nextInt(7);
                hour = random.nextInt(24);
                frequency = 3;
        }
        
        // Coordenadas simuladas (Bogotá aproximadamente)
        double baseLat = 4.6 + (random.nextDouble() - 0.5) * 0.2;
        double baseLon = -74.08 + (random.nextDouble() - 0.5) * 0.2;
        
        double distance = 2 + random.nextDouble() * 15; // 2-17 km
        int duration = (int) (distance * 3 + random.nextInt(20)); // minutos
        double cost = 1.5 + (distance * 0.2); // costo basado en distancia
        
        return TripPattern.builder()
                .usuarioId((long) userId)
                .tarjetaId((long) userId) // Simplificado
                .timestamp(now.minusDays(random.nextInt(30)))
                .diaSemana(dayOfWeek)
                .hora(hour)
                .rutaId((long) (1 + random.nextInt(5)))
                .latitudOrigen(baseLat)
                .longitudOrigen(baseLon)
                .latitudDestino(baseLat + (random.nextDouble() - 0.5) * 0.1)
                .longitudDestino(baseLon + (random.nextDouble() - 0.5) * 0.1)
                .distancia(distance)
                .duracionMinutos(duration)
                .costoViaje(cost)
                .frecuenciaSemanal(frequency)
                .build();
    }

    private String getRandomWeather() {
        double rand = random.nextDouble();
        if (rand < 0.6) return "SOLEADO";
        if (rand < 0.85) return "NUBLADO";
        return "LLUVIOSO";
    }
}
