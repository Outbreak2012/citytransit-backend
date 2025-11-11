package com.citytransit.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración para deshabilitar Deep Learning temporalmente
 * debido a problemas con dependencias nativas en Windows
 */
@Configuration
@Slf4j
public class DeepLearningConfig {

    @Bean
    @ConditionalOnProperty(name = "app.dl.enabled", havingValue = "false", matchIfMissing = true)
    public String deepLearningDisabledNotice() {
        log.warn("========================================");
        log.warn("  DEEP LEARNING DESHABILITADO");
        log.warn("========================================");
        log.warn("Los servicios de Deep Learning están deshabilitados.");
        log.warn("Para habilitar, configurar: app.dl.enabled=true");
        log.warn("Nota: Requiere configuración especial de rutas nativas en Windows");
        log.warn("========================================");
        return "DL_DISABLED";
    }
}
