package com.citytransit.graphql.subscription;

import com.citytransit.model.dto.response.NotificacionResponse;
import com.citytransit.model.dto.response.TelemetriaGPSResponse;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Controller
public class RealtimeSubscriptionResolver {

    @SubscriptionMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PASAJERO', 'CONDUCTOR')")
    public Flux<NotificacionResponse> nuevaNotificacion(@Argument Long usuarioId) {
        // Este flujo debería conectarse a un sistema de mensajería real (Redis Pub/Sub, RabbitMQ, etc.)
        // Por ahora retornamos un flujo vacío que se completará cuando haya notificaciones reales
        return Flux.empty();
    }

    @SubscriptionMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR', 'PASAJERO')")
    public Flux<TelemetriaGPSResponse> telemetriaActualizada(@Argument Long vehiculoId) {
        // Este flujo debería conectarse al sistema de telemetría GPS en tiempo real
        // Por ahora retornamos un flujo vacío que se completará cuando haya datos GPS reales
        return Flux.empty();
    }
}
