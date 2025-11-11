package com.citytransit.controller;

import com.citytransit.model.dto.response.TelemetriaGPSResponse;
import com.citytransit.service.TelemetriaGPSService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/telemetria")
@RequiredArgsConstructor
public class TelemetriaGPSController {

    private final TelemetriaGPSService telemetriaGPSService;

    @GetMapping("/vehiculo/{vehiculoId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    public ResponseEntity<List<TelemetriaGPSResponse>> getTelemetriaByVehiculo(@PathVariable Long vehiculoId) {
        List<TelemetriaGPSResponse> telemetria = telemetriaGPSService.getTelemetriaByVehiculo(vehiculoId);
        return ResponseEntity.ok(telemetria);
    }

    @GetMapping("/vehiculo/{vehiculoId}/ultima")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR', 'CONDUCTOR')")
    public ResponseEntity<TelemetriaGPSResponse> getUltimaTelemetria(@PathVariable Long vehiculoId) {
        TelemetriaGPSResponse telemetria = telemetriaGPSService.getUltimaTelemetria(vehiculoId);
        return ResponseEntity.ok(telemetria);
    }

    // TODO: Este método está deshabilitado temporalmente
    /*
    @GetMapping("/ruta/{rutaId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR', 'PASAJERO')")
    public ResponseEntity<List<TelemetriaGPSResponse>> getTelemetriaByRuta(@PathVariable Long rutaId) {
        List<TelemetriaGPSResponse> telemetria = telemetriaGPSService.getTelemetriaByRuta(rutaId);
        return ResponseEntity.ok(telemetria);
    }
    */
}
