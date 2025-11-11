package com.citytransit.websocket;

import com.citytransit.model.dto.response.NotificacionResponse;
import com.citytransit.model.dto.response.TelemetriaGPSResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    public void enviarNotificacion(Long usuarioId, NotificacionResponse notificacion) {
        messagingTemplate.convertAndSend("/topic/notificaciones/" + usuarioId, notificacion);
    }

    public void enviarTelemetria(Long vehiculoId, TelemetriaGPSResponse telemetria) {
        messagingTemplate.convertAndSend("/topic/telemetria/" + vehiculoId, telemetria);
    }

    public void enviarTelemetriaRuta(Long rutaId, TelemetriaGPSResponse telemetria) {
        messagingTemplate.convertAndSend("/topic/ruta/" + rutaId, telemetria);
    }

    public void enviarActualizacionTransaccion(Long tarjetaId, Object transaccion) {
        messagingTemplate.convertAndSend("/topic/transacciones/" + tarjetaId, transaccion);
    }
}
