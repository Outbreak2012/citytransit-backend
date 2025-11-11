package com.citytransit.model.entity;

import com.citytransit.model.enums.EstadoPasaje;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pasajes")
@EntityListeners(AuditingEntityListener.class)
public class Pasaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pasaje_id")
    private Long pasajeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tarjeta_id", nullable = false)
    private Tarjeta tarjeta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehiculo_id", nullable = false)
    private Vehiculo vehiculo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ruta_id")
    private Ruta ruta;

    @CreatedDate
    @Column(name = "fecha_hora_validacion", nullable = false, updatable = false)
    private LocalDateTime fechaHoraValidacion;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoPasaje estado;

    @Column(name = "latitud_validacion")
    private Double latitudValidacion;

    @Column(name = "longitud_validacion")
    private Double longitudValidacion;

    @Column(name = "metodo_pago", length = 20)
    private String metodoPago = "NFC";

    @Column(name = "descuento_aplicado", precision = 10, scale = 2)
    private BigDecimal descuentoAplicado = BigDecimal.ZERO;
}
