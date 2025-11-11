package com.citytransit.service;

import com.citytransit.model.dto.response.TelemetriaGPSResponse;
import com.citytransit.model.entity.TelemetriaGPS;
import com.citytransit.repository.TelemetriaGPSRepository;
import com.citytransit.repository.VehiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TelemetriaGPSService {

    private final TelemetriaGPSRepository telemetriaGPSRepository;
    private final VehiculoRepository vehiculoRepository;

    public TelemetriaGPS save(TelemetriaGPS telemetriaGPS) {
        return telemetriaGPSRepository.save(telemetriaGPS);
    }

    public List<TelemetriaGPSResponse> getTelemetriaByVehiculo(Long vehiculoId) {
        LocalDateTime hace24Horas = LocalDateTime.now().minusHours(24);
        List<TelemetriaGPS> telemetrias = telemetriaGPSRepository
            .findByVehiculoVehiculoIdAndTimestampAfterOrderByTimestampDesc(vehiculoId, hace24Horas);
        return telemetrias.stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    public TelemetriaGPSResponse getUltimaTelemetria(Long vehiculoId) {
        TelemetriaGPS telemetria = telemetriaGPSRepository
            .findFirstByVehiculoVehiculoIdOrderByTimestampDesc(vehiculoId)
            .orElseThrow(() -> new RuntimeException("No hay datos de telemetría para este vehículo"));
        return toResponse(telemetria);
    }

    // TODO: Este método está deshabilitado porque Vehiculo no tiene la propiedad 'ruta'
    // Se necesita revisar el modelo de datos para implementar correctamente
    /*
    public List<TelemetriaGPSResponse> getTelemetriaByRuta(Long rutaId) {
        LocalDateTime hace1Hora = LocalDateTime.now().minusHours(1);
        List<TelemetriaGPS> telemetrias = telemetriaGPSRepository
            .findByVehiculoRutaRutaIdAndTimestampAfterOrderByTimestampDesc(rutaId, hace1Hora);
        return telemetrias.stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }
    */

    private TelemetriaGPSResponse toResponse(TelemetriaGPS telemetria) {
        return TelemetriaGPSResponse.builder()
            .telemetriaId(telemetria.getTelemetriaId())
            .vehiculoId(telemetria.getVehiculo().getVehiculoId())
            .latitud(telemetria.getLatitud())
            .longitud(telemetria.getLongitud())
            .timestamp(telemetria.getTimestamp())
            .velocidad(telemetria.getVelocidad())
            .build();
    }
}
