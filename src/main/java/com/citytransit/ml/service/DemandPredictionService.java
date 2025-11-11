package com.citytransit.ml.service;

import com.citytransit.ml.model.DemandPredictionData;
import com.citytransit.ml.model.DemandPredictionResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio de Machine Learning para predicción de demanda usando Regresión Lineal
 * (Simplificado para compatibilidad con Apache Commons Math)
 */
@Slf4j
@Service
public class DemandPredictionService {

    private OLSMultipleLinearRegression model;
    private boolean modelTrained = false;

    @PostConstruct
    public void init() {
        log.info("Inicializando servicio de predicción de demanda con ML");
        // El modelo se entrenará cuando haya suficientes datos
    }

    /**
     * Entrena el modelo con datos históricos
     */
    public void trainModel(List<DemandPredictionData> trainingData) {
        try {
            log.info("Entrenando modelo ML con {} registros", trainingData.size());

            if (trainingData.size() < 50) {
                log.warn("Datos insuficientes para entrenar el modelo (mínimo 50 registros)");
                return;
            }

            // Preparar datos de entrenamiento
            int n = trainingData.size();
            double[][] features = new double[n][10];
            double[] targets = new double[n];

            for (int i = 0; i < n; i++) {
                DemandPredictionData data = trainingData.get(i);
                features[i] = extractFeatures(data);
                targets[i] = data.getPasajerosValidados().doubleValue();
            }

            // Entrenar modelo de regresión múltiple
            model = new OLSMultipleLinearRegression();
            model.newSampleData(targets, features);

            modelTrained = true;
            log.info("✅ Modelo ML entrenado exitosamente con {} features", features[0].length);

        } catch (Exception e) {
            log.error("Error entrenando modelo ML", e);
        }
    }

    /**
     * Predice la demanda para una ruta en un momento específico
     */
    public DemandPredictionResult predictDemand(DemandPredictionData predictionData) {
        if (!modelTrained) {
            log.warn("Modelo no entrenado, usando predicción basada en reglas");
            return getDefaultPrediction(predictionData);
        }

        try {
            double[] features = extractFeatures(predictionData);
            
            // Realizar predicción
            double prediction = model.estimateRegressionParameters()[0]; // intercepto
            double[] coefficients = model.estimateRegressionParameters();
            
            for (int i = 0; i < features.length && i + 1 < coefficients.length; i++) {
                prediction += coefficients[i + 1] * features[i];
            }
            
            int predictedPassengers = Math.max(0, (int) Math.round(prediction));

            // Calcular nivel de confianza basado en R²
            double confidence = Math.max(0.6, Math.min(0.95, model.calculateRSquared()));


            // Calcular ocupación predicha (asumiendo capacidad de 40 pasajeros por bus)
            double predictedOccupancy = Math.min(predictedPassengers / 40.0, 1.0);

            // Determinar nivel de demanda
            String demandLevel = getDemandLevel(predictedPassengers);

            // Generar recomendación
            String recommendation = generateRecommendation(predictedPassengers, predictedOccupancy);

            return DemandPredictionResult.builder()
                    .rutaId(predictionData.getRutaId())
                    .pasajerosPredichos(predictedPassengers)
                    .confianza(confidence)
                    .ocupacionPredicha(predictedOccupancy)
                    .nivelDemanda(demandLevel)
                    .recomendacion(recommendation)
                    .build();

        } catch (Exception e) {
            log.error("Error en predicción de demanda", e);
            return getDefaultPrediction(predictionData);
        }
    }

    /**
     * Predice la demanda para múltiples rutas
     */
    public List<DemandPredictionResult> predictMultipleRoutes(List<DemandPredictionData> predictions) {
        List<DemandPredictionResult> results = new ArrayList<>();
        for (DemandPredictionData data : predictions) {
            results.add(predictDemand(data));
        }
        return results;
    }

    /**
     * Extrae features del dato para el modelo
     */
    private double[] extractFeatures(DemandPredictionData data) {
        return new double[]{
                data.getDiaSemana().doubleValue(),           // 1-7
                data.getHora().doubleValue(),                // 0-23
                data.getMes().doubleValue(),                 // 1-12
                data.getEsFeriado() ? 1.0 : 0.0,            // 0 o 1
                data.getEsFinDeSemana() ? 1.0 : 0.0,        // 0 o 1
                data.getTemperatura() != null ? data.getTemperatura() : 20.0, // temperatura
                getClimaValue(data.getCondicionClimatica()), // 0-2
                data.getEsHoraPico() ? 1.0 : 0.0,          // 0 o 1
                data.getHoraDelDia().doubleValue(),         // 0-23
                data.getMinutoDelDia().doubleValue() / 1440.0 // normalizado 0-1
        };
    }

    private double getClimaValue(String condicion) {
        if (condicion == null) return 0.0;
        return switch (condicion.toUpperCase()) {
            case "SOLEADO" -> 0.0;
            case "NUBLADO" -> 1.0;
            case "LLUVIOSO" -> 2.0;
            default -> 0.0;
        };
    }

    private String getDemandLevel(int passengers) {
        if (passengers < 10) return "BAJA";
        if (passengers < 25) return "MEDIA";
        if (passengers < 40) return "ALTA";
        return "MUY_ALTA";
    }

    private String generateRecommendation(int passengers, double occupancy) {
        if (occupancy > 0.9) {
            return "⚠️ AUMENTAR FRECUENCIA - Ocupación muy alta";
        } else if (occupancy > 0.75) {
            return "⚠️ CONSIDERAR AUMENTAR FRECUENCIA - Ocupación alta";
        } else if (occupancy < 0.3) {
            return "💡 CONSIDERAR REDUCIR FRECUENCIA - Ocupación baja";
        } else {
            return "✅ FRECUENCIA NORMAL - Ocupación óptima";
        }
    }

    private DemandPredictionResult getDefaultPrediction(DemandPredictionData data) {
        // Predicción por defecto basada en reglas simples
        int baseDemand = 15;
        
        if (data.getEsHoraPico()) baseDemand += 20;
        if (data.getEsFinDeSemana()) baseDemand -= 5;
        if (data.getEsFeriado()) baseDemand -= 10;

        return DemandPredictionResult.builder()
                .rutaId(data.getRutaId())
                .pasajerosPredichos(baseDemand)
                .confianza(0.5)
                .ocupacionPredicha(baseDemand / 40.0)
                .nivelDemanda(getDemandLevel(baseDemand))
                .recomendacion("Modelo no entrenado - Predicción por reglas")
                .build();
    }

    /**
     * Obtiene el estado del modelo
     */
    public boolean isModelTrained() {
        return modelTrained;
    }

    /**
     * Evalúa la precisión del modelo con datos de prueba
     */
    public double evaluateModel(List<DemandPredictionData> testData) {
        if (!modelTrained) {
            return 0.0;
        }

        int correct = 0;
        int total = testData.size();

        for (DemandPredictionData data : testData) {
            DemandPredictionResult prediction = predictDemand(data);
            int predicted = prediction.getPasajerosPredichos();
            int actual = data.getPasajerosValidados();

            // Considerar correcto si está dentro de un margen del 20%
            double error = Math.abs(predicted - actual) / (double) actual;
            if (error <= 0.2) {
                correct++;
            }
        }

        return (double) correct / total;
    }
}
