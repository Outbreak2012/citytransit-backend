package com.citytransit.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rutas")
public class Ruta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ruta_id")
    private Long rutaId;

    @Column(name = "codigo_ruta", unique = true, nullable = false, length = 20)
    private String codigoRuta;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "paradas_json", columnDefinition = "JSONB")
    @JdbcTypeCode(SqlTypes.JSON)
    private String paradasJson;

    @Column(name = "horario_inicio")
    private java.time.LocalTime horarioInicio;

    @Column(name = "horario_fin")
    private java.time.LocalTime horarioFin;

    @Column(name = "frecuencia_minutos")
    private Integer frecuenciaMinutos;

    @Column(name = "precio_base", precision = 10, scale = 2)
    private java.math.BigDecimal precioBase = new java.math.BigDecimal("2500.00");

    @Column(nullable = false)
    private Boolean activa = true;
}
