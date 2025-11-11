package com.citytransit.model.entity;

import com.citytransit.model.enums.TipoVehiculo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vehiculos")
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vehiculo_id")
    private Long vehiculoId;

    @Column(nullable = false, length = 50)
    private String placa;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_vehiculo", nullable = false, length = 20)
    private TipoVehiculo tipoVehiculo;

    @Column(nullable = false, length = 100)
    private String modelo;

    @Column(nullable = false)
    private Integer capacidad;

    @Column(name = "anio_fabricacion")
    private Integer anioFabricacion;

    @Column(name = "dispositivo_gps_id", unique = true, length = 50)
    private String dispositivoGpsId;

    @Column(name = "estado_operativo", nullable = false, length = 20)
    private String estadoOperativo = "ACTIVO";

    @Column(name = "ultima_mantenimiento")
    private java.time.LocalDate ultimaMantenimiento;
}
