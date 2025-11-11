package com.citytransit.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "telemetria_gps")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TelemetriaGPS {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long telemetriaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehiculo_id", nullable = false)
    private Vehiculo vehiculo;

    @Column(nullable = false)
    private Double latitud;

    @Column(nullable = false)
    private Double longitud;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    private Double velocidad;
}
