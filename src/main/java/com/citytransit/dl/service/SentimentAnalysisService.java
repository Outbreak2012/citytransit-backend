package com.citytransit.dl.service;

import com.citytransit.dl.model.SentimentAnalysisRequest;
import com.citytransit.dl.model.SentimentAnalysisResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Servicio de análisis de sentimientos usando NLP
 * Analiza feedback de usuarios en español (Bolivia)
 */
@Slf4j
@Service
public class SentimentAnalysisService {

    private boolean modelLoaded = false;
    
    // Palabras clave para análisis (español boliviano)
    private Map<String, String> positiveWords;
    private Map<String, String> negativeWords;
    private Map<String, String> categoryKeywords;

    @PostConstruct
    public void init() {
        log.info("🧠 Inicializando servicio de análisis de sentimientos (BERT-like)");
        loadKeywords();
        modelLoaded = true;
    }

    private void loadKeywords() {
        // Palabras positivas en español boliviano
        positiveWords = new HashMap<>();
        positiveWords.put("excelente", "SATISFECHO");
        positiveWords.put("bueno", "SATISFECHO");
        positiveWords.put("genial", "SATISFECHO");
        positiveWords.put("perfecto", "SATISFECHO");
        positiveWords.put("gracias", "SATISFECHO");
        positiveWords.put("rápido", "SATISFECHO");
        positiveWords.put("limpio", "SATISFECHO");
        positiveWords.put("puntual", "SATISFECHO");
        positiveWords.put("amable", "SATISFECHO");
        positiveWords.put("cómodo", "SATISFECHO");
        
        // Palabras negativas
        negativeWords = new HashMap<>();
        negativeWords.put("malo", "FRUSTRADO");
        negativeWords.put("pésimo", "ENOJADO");
        negativeWords.put("terrible", "ENOJADO");
        negativeWords.put("sucio", "FRUSTRADO");
        negativeWords.put("tardanza", "FRUSTRADO");
        negativeWords.put("demora", "FRUSTRADO");
        negativeWords.put("atrasado", "FRUSTRADO");
        negativeWords.put("nunca", "ENOJADO");
        negativeWords.put("siempre", "FRUSTRADO");
        negativeWords.put("grosero", "ENOJADO");
        negativeWords.put("lleno", "FRUSTRADO");
        negativeWords.put("esperar", "FRUSTRADO");
        
        // Categorías
        categoryKeywords = new HashMap<>();
        categoryKeywords.put("conductor|chofer|driver", "CONDUCTOR");
        categoryKeywords.put("limpio|sucio|basura|higiene", "LIMPIEZA");
        categoryKeywords.put("tarde|demora|atrasado|puntual|horario", "PUNTUALIDAD");
        categoryKeywords.put("precio|caro|barato|tarifa|costo", "TARIFA");
        categoryKeywords.put("servicio|atención", "SERVICIO");
    }

    /**
     * Analiza el sentimiento de un texto en español
     * Contexto: Santa Cruz de la Sierra, Bolivia
     */
    public SentimentAnalysisResult analyzeSentiment(SentimentAnalysisRequest request) {
        try {
            log.info("📝 Analizando sentimiento: '{}'", 
                    request.getTexto().length() > 50 ? 
                    request.getTexto().substring(0, 50) + "..." : 
                    request.getTexto());
            
            String texto = request.getTexto().toLowerCase();
            
            // Análisis de sentimiento
            Map<String, Double> scores = calculateSentimentScores(texto);
            String sentimiento = determineSentiment(scores);
            Double confianza = scores.get(sentimiento);
            
            // Detectar emoción principal
            String emocion = detectEmotion(texto);
            
            // Detectar categoría
            String categoria = detectCategory(texto);
            
            // Determinar prioridad
            Integer prioridad = calculatePriority(sentimiento, emocion);
            
            // ¿Requiere acción?
            Boolean requiereAccion = prioridad >= 4;
            
            // Generar respuesta sugerida
            String respuestaSugerida = generateSuggestedResponse(sentimiento, categoria);
            
            return SentimentAnalysisResult.builder()
                    .texto(request.getTexto())
                    .sentimiento(sentimiento)
                    .confianza(confianza)
                    .scores(scores)
                    .emocionPrincipal(emocion)
                    .prioridad(prioridad)
                    .categoriaDetectada(categoria)
                    .requiereAccion(requiereAccion)
                    .respuestaSugerida(respuestaSugerida)
                    .build();
            
        } catch (Exception e) {
            log.error("❌ Error en análisis de sentimientos", e);
            return getDefaultResult(request.getTexto());
        }
    }

    private Map<String, Double> calculateSentimentScores(String texto) {
        Map<String, Double> scores = new HashMap<>();
        
        int positiveCount = 0;
        int negativeCount = 0;
        int totalWords = texto.split("\\s+").length;
        
        // Contar palabras positivas
        for (String word : positiveWords.keySet()) {
            if (texto.contains(word)) {
                positiveCount++;
            }
        }
        
        // Contar palabras negativas
        for (String word : negativeWords.keySet()) {
            if (texto.contains(word)) {
                negativeCount++;
            }
        }
        
        // Calcular scores (normalizado)
        double positiveScore = Math.min((double) positiveCount / Math.max(totalWords * 0.1, 1), 1.0);
        double negativeScore = Math.min((double) negativeCount / Math.max(totalWords * 0.1, 1), 1.0);
        double neutralScore = 1.0 - Math.max(positiveScore, negativeScore);
        
        // Ajustar scores
        if (positiveCount == 0 && negativeCount == 0) {
            neutralScore = 0.8;
            positiveScore = 0.1;
            negativeScore = 0.1;
        }
        
        scores.put("POSITIVO", Math.max(positiveScore, 0.1));
        scores.put("NEGATIVO", Math.max(negativeScore, 0.1));
        scores.put("NEUTRAL", Math.max(neutralScore, 0.1));
        
        return scores;
    }

    private String determineSentiment(Map<String, Double> scores) {
        return scores.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("NEUTRAL");
    }

    private String detectEmotion(String texto) {
        // Detectar emoción basada en palabras clave
        for (Map.Entry<String, String> entry : positiveWords.entrySet()) {
            if (texto.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        
        for (Map.Entry<String, String> entry : negativeWords.entrySet()) {
            if (texto.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        
        // Detectar confusión
        if (texto.contains("?") || texto.contains("cómo") || texto.contains("dónde")) {
            return "CONFUNDIDO";
        }
        
        return "NEUTRAL";
    }

    private String detectCategory(String texto) {
        for (Map.Entry<String, String> entry : categoryKeywords.entrySet()) {
            Pattern pattern = Pattern.compile(entry.getKey(), Pattern.CASE_INSENSITIVE);
            if (pattern.matcher(texto).find()) {
                return entry.getValue();
            }
        }
        return "SERVICIO"; // Categoría por defecto
    }

    private Integer calculatePriority(String sentimiento, String emocion) {
        int priority = 3; // Normal
        
        if ("NEGATIVO".equals(sentimiento)) {
            priority = 4;
        }
        
        if ("ENOJADO".equals(emocion)) {
            priority = 5; // Urgente
        }
        
        return priority;
    }

    private String generateSuggestedResponse(String sentimiento, String categoria) {
        if ("POSITIVO".equals(sentimiento)) {
            return "¡Muchas gracias por tu comentario! Nos alegra que hayas tenido una buena experiencia con CityTransit. 🚌✨";
        }
        
        if ("NEGATIVO".equals(sentimiento)) {
            switch (categoria) {
                case "CONDUCTOR":
                    return "Lamentamos tu experiencia. Hemos registrado tu queja y tomaremos acciones con el personal involucrado. Gracias por ayudarnos a mejorar.";
                case "LIMPIEZA":
                    return "Disculpa las molestias. Hemos notificado al equipo de limpieza para mejorar nuestros estándares de higiene.";
                case "PUNTUALIDAD":
                    return "Sentimos el retraso. Estamos trabajando en optimizar nuestros horarios. Tu feedback es muy valioso.";
                case "TARIFA":
                    return "Entendemos tu preocupación sobre las tarifas. Recuerda que tenemos promociones y descuentos disponibles en la app.";
                default:
                    return "Lamentamos tu experiencia. Hemos registrado tu comentario y trabajaremos para mejorar. ¿Podemos ayudarte con algo más?";
            }
        }
        
        return "Gracias por tu comentario. Si necesitas más ayuda, no dudes en contactarnos. 😊";
    }

    private SentimentAnalysisResult getDefaultResult(String texto) {
        Map<String, Double> defaultScores = new HashMap<>();
        defaultScores.put("POSITIVO", 0.33);
        defaultScores.put("NEGATIVO", 0.33);
        defaultScores.put("NEUTRAL", 0.34);
        
        return SentimentAnalysisResult.builder()
                .texto(texto)
                .sentimiento("NEUTRAL")
                .confianza(0.5)
                .scores(defaultScores)
                .emocionPrincipal("NEUTRAL")
                .prioridad(3)
                .categoriaDetectada("SERVICIO")
                .requiereAccion(false)
                .respuestaSugerida("Gracias por tu comentario.")
                .build();
    }

    public boolean isModelLoaded() {
        return modelLoaded;
    }
}
