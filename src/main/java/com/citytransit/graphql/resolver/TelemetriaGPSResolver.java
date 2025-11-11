package com.citytransit.graphql.resolver;

import com.citytransit.model.dto.response.TelemetriaGPSResponse;
import com.citytransit.service.TelemetriaGPSService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class TelemetriaGPSResolver {

    private final TelemetriaGPSService telemetriaGPSService;

    @QueryMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    public List<TelemetriaGPSResponse> telemetriaByVehiculo(@Argument Long vehiculoId) {
        return telemetriaGPSService.getTelemetriaByVehiculo(vehiculoId);
    }

    @QueryMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR', 'CONDUCTOR')")
    public TelemetriaGPSResponse ultimaTelemetriaVehiculo(@Argument Long vehiculoId) {
        return telemetriaGPSService.getUltimaTelemetria(vehiculoId);
    }

    // TODO: Este método está deshabilitado temporalmente
    /*
    @QueryMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR', 'PASAJERO')")
    public List<TelemetriaGPSResponse> telemetriaByRuta(@Argument Long rutaId) {
        return telemetriaGPSService.getTelemetriaByRuta(rutaId);
    }
    */
}
