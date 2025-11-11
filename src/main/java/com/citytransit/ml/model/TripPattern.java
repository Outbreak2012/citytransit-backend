package com.citytransit.ml.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Patrón de viaje para clustering
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TripPattern {
    private Long usuarioId;
    private Long tarjetaId;
    private LocalDateTime timestamp;
    private Integer diaSemana;
    private Integer hora;
    private Long rutaId;
    private Double latitudOrigen;
    private Double longitudOrigen;
    private Double latitudDestino;
    private Double longitudDestino;
    private Double distancia; // en km
    private Integer duracionMinutos;
    private Double costoViaje;
    private Integer frecuenciaSemanal; // Cuántas veces a la semana hace este viaje
}
