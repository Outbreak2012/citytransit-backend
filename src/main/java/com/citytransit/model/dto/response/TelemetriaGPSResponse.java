package com.citytransit.model.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TelemetriaGPSResponse {
    private Long telemetriaId;
    private Long vehiculoId;
    private Double latitud;
    private Double longitud;
    private LocalDateTime timestamp;
    private Double velocidad;
}
