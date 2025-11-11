package com.citytransit.repository;

import com.citytransit.model.entity.TelemetriaGPS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TelemetriaGPSRepository extends JpaRepository<TelemetriaGPS, Long> {
    List<TelemetriaGPS> findByVehiculoVehiculoIdAndTimestampAfterOrderByTimestampDesc(Long vehiculoId, LocalDateTime timestamp);
    Optional<TelemetriaGPS> findFirstByVehiculoVehiculoIdOrderByTimestampDesc(Long vehiculoId);
    // TODO: Este método está mal definido - Vehiculo no tiene la propiedad 'ruta'
    // List<TelemetriaGPS> findByVehiculoRutaRutaIdAndTimestampAfterOrderByTimestampDesc(Long rutaId, LocalDateTime timestamp);
}
