package com.citytransit.model.entity;

import com.citytransit.model.enums.EstadoTarjeta;
import com.citytransit.model.enums.TipoTarjeta;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tarjetas")
@EntityListeners(AuditingEntityListener.class)
public class Tarjeta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tarjeta_id")
    private Long tarjetaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = true)
    private Usuario usuario;

    @Column(name = "numero_tarjeta", unique = true, nullable = false, length = 20)
    private String numeroTarjeta;

    @Column(name = "uid_nfc", unique = true, length = 50)
    private String uidNfc;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_tarjeta", nullable = false, length = 20)
    private TipoTarjeta tipoTarjeta;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal saldo = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoTarjeta estado;

    @Column(name = "fecha_expiracion")
    private LocalDate fechaExpiracion;

    @Column(name = "suscripcion_activa")
    private Boolean suscripcionActiva = false;

    @Column(name = "plan_suscripcion", length = 50)
    private String planSuscripcion;

    @CreatedDate
    @Column(name = "fecha_emision", nullable = false, updatable = false)
    private LocalDateTime fechaEmision;

    @LastModifiedDate
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
}
