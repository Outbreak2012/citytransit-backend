package com.citytransit.repository;

import com.citytransit.model.entity.Vehiculo;
import com.citytransit.model.enums.TipoVehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {
    
    Optional<Vehiculo> findByPlaca(String placa);
    Optional<Vehiculo> findByDispositivoGpsId(String dispositivoGpsId);
    List<Vehiculo> findByTipoVehiculo(TipoVehiculo tipoVehiculo);
    List<Vehiculo> findByEstadoOperativo(String estadoOperativo);
}
