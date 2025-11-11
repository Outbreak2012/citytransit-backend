package com.citytransit.repository;

import com.citytransit.model.entity.Pasaje;
import com.citytransit.model.enums.EstadoPasaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PasajeRepository extends JpaRepository<Pasaje, Long> {
    List<Pasaje> findByTarjetaTarjetaId(Long tarjetaId);
    List<Pasaje> findByTarjetaUsuarioUsuarioId(Long usuarioId);
    List<Pasaje> findByVehiculoVehiculoId(Long vehiculoId);
    List<Pasaje> findByRutaRutaId(Long rutaId);
    List<Pasaje> findByEstado(EstadoPasaje estado);
}
