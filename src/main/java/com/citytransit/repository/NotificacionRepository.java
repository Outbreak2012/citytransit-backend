package com.citytransit.repository;

import com.citytransit.model.entity.Notificacion;
import com.citytransit.model.enums.EstadoNotificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    List<Notificacion> findByUsuarioUsuarioIdOrderByFechaHoraEnvioDesc(Long usuarioId);
    Long countByUsuarioUsuarioIdAndEstado(Long usuarioId, EstadoNotificacion estado);
}
