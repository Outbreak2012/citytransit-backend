package com.citytransit.service;

import com.citytransit.model.dto.response.NotificacionResponse;
import com.citytransit.model.enums.EstadoNotificacion;
import com.citytransit.model.entity.Notificacion;
import com.citytransit.repository.NotificacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;

    public Notificacion save(Notificacion notificacion) {
        return notificacionRepository.save(notificacion);
    }

    public List<NotificacionResponse> getNotificacionesByUsuario(Long usuarioId) {
        List<Notificacion> notificaciones = notificacionRepository.findByUsuarioUsuarioIdOrderByFechaHoraEnvioDesc(usuarioId);
        return notificaciones.stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    @Transactional
    public void marcarComoLeida(Long notificacionId) {
        Notificacion notificacion = notificacionRepository.findById(notificacionId)
            .orElseThrow(() -> new RuntimeException("Notificación no encontrada"));
        notificacion.setEstado(EstadoNotificacion.LEIDO);
        notificacion.setFechaHoraLeido(LocalDateTime.now());
        notificacionRepository.save(notificacion);
    }

    public Long contarNoLeidas(Long usuarioId) {
        return notificacionRepository.countByUsuarioUsuarioIdAndEstado(usuarioId, EstadoNotificacion.NO_LEIDO);
    }

    private NotificacionResponse toResponse(Notificacion notificacion) {
        return NotificacionResponse.builder()
            .notificacionId(notificacion.getNotificacionId())
            .usuarioId(notificacion.getUsuario().getUsuarioId())
            .titulo(notificacion.getTitulo())
            .mensaje(notificacion.getMensaje())
            .fechaHoraEnvio(notificacion.getFechaHoraEnvio())
            .fechaHoraLeido(notificacion.getFechaHoraLeido())
            .estado(notificacion.getEstado())
            .build();
    }
}
