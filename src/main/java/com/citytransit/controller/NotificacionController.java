package com.citytransit.controller;

import com.citytransit.model.dto.response.NotificacionResponse;
import com.citytransit.service.NotificacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")
@RequiredArgsConstructor
public class NotificacionController {

    private final NotificacionService notificacionService;

    @GetMapping("/usuario/{usuarioId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PASAJERO', 'CONDUCTOR')")
    public ResponseEntity<List<NotificacionResponse>> getNotificacionesByUsuario(@PathVariable Long usuarioId) {
        List<NotificacionResponse> notificaciones = notificacionService.getNotificacionesByUsuario(usuarioId);
        return ResponseEntity.ok(notificaciones);
    }

    @PutMapping("/{id}/marcar-leida")
    @PreAuthorize("hasAnyRole('ADMIN', 'PASAJERO', 'CONDUCTOR')")
    public ResponseEntity<Void> marcarComoLeida(@PathVariable Long id) {
        notificacionService.marcarComoLeida(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/no-leidas/{usuarioId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PASAJERO', 'CONDUCTOR')")
    public ResponseEntity<Long> contarNoLeidas(@PathVariable Long usuarioId) {
        Long count = notificacionService.contarNoLeidas(usuarioId);
        return ResponseEntity.ok(count);
    }
}
