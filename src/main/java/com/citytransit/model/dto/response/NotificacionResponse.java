package com.citytransit.model.dto.response;

import com.citytransit.model.enums.EstadoNotificacion;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class NotificacionResponse {
    private Long notificacionId;
    private Long usuarioId;
    private String titulo;
    private String mensaje;
    private LocalDateTime fechaHoraEnvio;
    private LocalDateTime fechaHoraLeido;
    private EstadoNotificacion estado;
}
