package com.citytransit.test;

import com.citytransit.dl.model.*;
import com.citytransit.dl.service.*;
import com.citytransit.ml.model.*;
import com.citytransit.ml.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Pruebas con datos reales de Santa Cruz de la Sierra, Bolivia
 * NOTA: Deshabilitado temporalmente porque los servicios DL están mockeados
 */
@Slf4j
//@Component  // DESHABILITADO: servicios DL no disponibles
@RequiredArgsConstructor
public class SantaCruzDataTester {

    private final DemandPredictionService demandPredictionService;
    private final LSTMPredictionService lstmPredictionService;
    private final SentimentAnalysisService sentimentAnalysisService;
    private final ComputerVisionService computerVisionService;

    @EventListener(ApplicationReadyEvent.class)
    public void runTests() {
        log.info("🇧🇴 ========================================");
        log.info("🇧🇴 Iniciando pruebas con datos de Santa Cruz de la Sierra");
        log.info("🇧🇴 ========================================");
        
        try {
            Thread.sleep(2000); // Esperar a que los modelos se entrenen
            
            testDemandPrediction();
            testLSTMPrediction();
            testSentimentAnalysis();
            testComputerVision();
            
            log.info("✅ Todas las pruebas completadas exitosamente");
            
        } catch (Exception e) {
            log.error("❌ Error en pruebas", e);
        }
    }

    /**
     * Prueba predicción de demanda con Random Forest
     * Contexto: Ruta al Plan 3000 (zona popular de Santa Cruz)
     */
    private void testDemandPrediction() {
        log.info("\n📊 === TEST 1: Predicción de Demanda (Random Forest) ===");
        log.info("📍 Ruta: Plan 3000 → Centro (Ruta más transitada de Santa Cruz)");
        
        // Escenario 1: Hora pico matutina (7am, lunes)
        DemandPredictionData morningRush = DemandPredictionData.builder()
                .rutaId(1L)
                .fechaHora(LocalDateTime.now().withHour(7).withMinute(30))
                .diaSemana(1) // Lunes
                .hora(7)
                .mes(11) // Noviembre
                .esFeriado(false)
                .esFinDeSemana(false)
                .temperatura(26.0) // Temperatura típica de Santa Cruz por la mañana
                .condicionClimatica("SOLEADO")
                .horaDelDia(7)
                .minutoDelDia(450)
                .esHoraPico(true)
                .build();
        
        DemandPredictionResult result1 = demandPredictionService.predictDemand(morningRush);
        log.info("🌅 Predicción 7:30 AM (Lunes):");
        log.info("   Pasajeros predichos: {} | Ocupación: {:.0%} | Nivel: {}", 
                result1.getPasajerosPredichos(), 
                result1.getOcupacionPredicha(), 
                result1.getNivelDemanda());
        log.info("   Recomendación: {}", result1.getRecomendacion());
        
        // Escenario 2: Mediodía (12pm, miércoles)
        DemandPredictionData lunchTime = DemandPredictionData.builder()
                .rutaId(1L)
                .fechaHora(LocalDateTime.now().withHour(12).withMinute(0))
                .diaSemana(3) // Miércoles
                .hora(12)
                .mes(11)
                .esFeriado(false)
                .esFinDeSemana(false)
                .temperatura(32.0) // Hora más calurosa en Santa Cruz
                .condicionClimatica("SOLEADO")
                .horaDelDia(12)
                .minutoDelDia(720)
                .esHoraPico(false)
                .build();
        
        DemandPredictionResult result2 = demandPredictionService.predictDemand(lunchTime);
        log.info("☀️ Predicción 12:00 PM (Miércoles):");
        log.info("   Pasajeros predichos: {} | Ocupación: {:.0%} | Nivel: {}", 
                result2.getPasajerosPredichos(), 
                result2.getOcupacionPredicha(), 
                result2.getNivelDemanda());
        
        // Escenario 3: Domingo tarde (clima lluvioso)
        DemandPredictionData sundayRain = DemandPredictionData.builder()
                .rutaId(1L)
                .fechaHora(LocalDateTime.now().withHour(15).withMinute(0))
                .diaSemana(7) // Domingo
                .hora(15)
                .mes(11)
                .esFeriado(false)
                .esFinDeSemana(true)
                .temperatura(24.0)
                .condicionClimatica("LLUVIOSO") // Época de lluvias en Santa Cruz
                .horaDelDia(15)
                .minutoDelDia(900)
                .esHoraPico(false)
                .build();
        
        DemandPredictionResult result3 = demandPredictionService.predictDemand(sundayRain);
        log.info("🌧️ Predicción 3:00 PM (Domingo lluvioso):");
        log.info("   Pasajeros predichos: {} | Ocupación: {:.0%} | Nivel: {}", 
                result3.getPasajerosPredichos(), 
                result3.getOcupacionPredicha(), 
                result3.getNivelDemanda());
    }

    /**
     * Prueba predicción con LSTM
     * Contexto: Ruta Villa 1ro de Mayo → Universidad (ruta estudiantil)
     */
    private void testLSTMPrediction() {
        log.info("\n🧠 === TEST 2: Predicción LSTM (Series Temporales) ===");
        log.info("📍 Ruta: Villa 1ro de Mayo → Universidad Autónoma Gabriel René Moreno");
        
        TimeSeriesData data = TimeSeriesData.builder()
                .rutaId(2L)
                .timestamps(List.of(LocalDateTime.now()))
                .pasajeros(List.of(25, 28, 32, 30, 35)) // Histórico últimas 5 horas
                .temperaturas(List.of(26.0, 28.0, 30.0, 31.0, 29.0))
                .condicionesClimaticas(List.of("SOLEADO", "SOLEADO", "SOLEADO", "NUBLADO", "NUBLADO"))
                .windowSize(24)
                .predictionHorizon(6) // Próximas 6 horas
                .build();
        
        LSTMPredictionResult result = lstmPredictionService.predictFutureDemand(data);
        log.info("📈 Predicción próximas 6 horas:");
        log.info("   Tendencia: {}", result.getTendencia());
        log.info("   Pico máximo: {} pasajeros a las {}", 
                result.getPicoMaximo(), 
                result.getHoraPico().toLocalTime());
        log.info("   Confianza promedio: {:.0%}", result.getConfianzaPromedio());
        
        log.info("   Detalle por hora:");
        result.getPredicciones().stream().limit(3).forEach(pred -> 
            log.info("     {} - {} pasajeros (confianza: {:.0%})", 
                    pred.getTimestamp().toLocalTime(),
                    pred.getPasajerosPredichos(),
                    pred.getConfianza())
        );
    }

    /**
     * Prueba análisis de sentimientos
     * Contexto: Comentarios reales típicos de usuarios en Santa Cruz
     */
    private void testSentimentAnalysis() {
        log.info("\n💬 === TEST 3: Análisis de Sentimientos (NLP) ===");
        log.info("📝 Analizando feedback de usuarios cruceños...");
        
        // Comentario positivo típico
        String[] comentarios = {
                "Excelente servicio! El chofer muy amable y el bus llegó puntual. Gracias CityTransit!",
                "Pésimo servicio, el bus llegó tarde como siempre y estaba muy lleno. El conductor fue grosero.",
                "El precio está un poco caro pero el servicio es bueno",
                "Necesito saber cómo recargar mi tarjeta, no encuentro la opción en la app"
        };
        
        for (int i = 0; i < comentarios.length; i++) {
            SentimentAnalysisRequest request = SentimentAnalysisRequest.builder()
                    .texto(comentarios[i])
                    .contexto("FEEDBACK")
                    .rutaId(1L)
                    .build();
            
            SentimentAnalysisResult result = sentimentAnalysisService.analyzeSentiment(request);
            
            log.info("\n📝 Comentario {}: \"{}\"", i + 1, 
                    comentarios[i].length() > 60 ? comentarios[i].substring(0, 60) + "..." : comentarios[i]);
            log.info("   Sentimiento: {} ({:.0%} confianza)", result.getSentimiento(), result.getConfianza());
            log.info("   Emoción: {} | Categoría: {}", result.getEmocionPrincipal(), result.getCategoriaDetectada());
            log.info("   Prioridad: {}/5 | Requiere acción: {}", result.getPrioridad(), result.getRequiereAccion());
            log.info("   Respuesta sugerida: {}", 
                    result.getRespuestaSugerida().length() > 70 ? 
                    result.getRespuestaSugerida().substring(0, 70) + "..." : 
                    result.getRespuestaSugerida());
        }
    }

    /**
     * Prueba Computer Vision
     * Contexto: Análisis de ocupación en buses de Santa Cruz
     */
    private void testComputerVision() {
        log.info("\n👁️ === TEST 4: Computer Vision (Detección de Ocupación) ===");
        log.info("📷 Analizando ocupación de vehículos...");
        
        // Simular 3 escenarios diferentes
        String[] escenarios = {
                "Bus en hora pico - Plan 3000",
                "Bus en horario regular - Centro",
                "Bus domingo tarde - Zona Norte"
        };
        
        for (int i = 0; i < escenarios.length; i++) {
            // Simular imagen con hash diferente para cada escenario
            String simulatedImage = "data:image/jpeg;base64,scenario_" + i + "_" + System.currentTimeMillis();
            
            OccupancyAnalysisRequest request = OccupancyAnalysisRequest.builder()
                    .vehiculoId((long) (i + 1))
                    .imagenBase64(simulatedImage)
                    .rutaId(1L)
                    .build();
            
            OccupancyAnalysisResult result = computerVisionService.analyzeOccupancy(request);
            
            log.info("\n🚌 Escenario: {}", escenarios[i]);
            log.info("   Personas detectadas: {} / {}", 
                    result.getPersonasDetectadas(), 
                    result.getCapacidadMaxima());
            log.info("   Ocupación: {:.0%} - Nivel: {}", 
                    result.getPorcentajeOcupacion(), 
                    result.getNivelOcupacion());
            log.info("   Confianza detección: {:.0%}", result.getConfianzaDeteccion());
            
            if (result.getAlertaSeguridad() != null) {
                log.info("   ⚠️ {}", result.getAlertaSeguridad());
            }
            
            if (result.getRequiereVehiculoAdicional()) {
                log.info("   🚨 Recomendación: Agregar vehículo adicional");
            }
        }
    }
}
