package com.citytransit.graphql.resolver;

import com.citytransit.model.dto.response.NotificacionResponse;
import com.citytransit.service.NotificacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class NotificacionResolver {

    private final NotificacionService notificacionService;

    @QueryMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PASAJERO', 'CONDUCTOR')")
    public List<NotificacionResponse> notificacionesByUsuario(@Argument Long usuarioId) {
        return notificacionService.getNotificacionesByUsuario(usuarioId);
    }

    @QueryMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PASAJERO', 'CONDUCTOR')")
    public Long contarNotificacionesNoLeidas(@Argument Long usuarioId) {
        return notificacionService.contarNoLeidas(usuarioId);
    }

    @MutationMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PASAJERO', 'CONDUCTOR')")
    public Boolean marcarNotificacionLeida(@Argument Long notificacionId) {
        notificacionService.marcarComoLeida(notificacionId);
        return true;
    }
}
