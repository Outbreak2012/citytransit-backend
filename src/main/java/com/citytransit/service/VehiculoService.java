package com.citytransit.service;

import com.citytransit.model.entity.Vehiculo;
import com.citytransit.model.enums.TipoVehiculo;
import com.citytransit.repository.VehiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehiculoService {

    private final VehiculoRepository vehiculoRepository;

    @Transactional
    public Vehiculo crearVehiculo(Vehiculo vehiculo) {
        // Validar que no exista vehiculo con la misma placa
        if (vehiculoRepository.findByPlaca(vehiculo.getPlaca()).isPresent()) {
            throw new RuntimeException("Ya existe un vehículo con la placa: " + vehiculo.getPlaca());
        }

        // Validar que no exista vehiculo con el mismo GPS
        if (vehiculo.getDispositivoGpsId() != null && 
            vehiculoRepository.findByDispositivoGpsId(vehiculo.getDispositivoGpsId()).isPresent()) {
            throw new RuntimeException("Ya existe un vehículo con el dispositivo GPS: " + vehiculo.getDispositivoGpsId());
        }

        return vehiculoRepository.save(vehiculo);
    }

    public Vehiculo obtenerVehiculoPorId(Long id) {
        return vehiculoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado con ID: " + id));
    }

    public Vehiculo obtenerVehiculoPorPlaca(String placa) {
        return vehiculoRepository.findByPlaca(placa)
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado con placa: " + placa));
    }

    public Vehiculo obtenerVehiculoPorDispositivoGps(String gpsId) {
        return vehiculoRepository.findByDispositivoGpsId(gpsId)
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado con dispositivo GPS: " + gpsId));
    }

    public List<Vehiculo> listarTodosLosVehiculos() {
        return vehiculoRepository.findAll();
    }

    public List<Vehiculo> listarVehiculosPorTipo(TipoVehiculo tipo) {
        return vehiculoRepository.findByTipoVehiculo(tipo);
    }

    @Transactional
    public Vehiculo actualizarVehiculo(Long id, Vehiculo vehiculoActualizado) {
        Vehiculo vehiculo = obtenerVehiculoPorId(id);

        // Validar placa única si se actualiza
        if (vehiculoActualizado.getPlaca() != null && 
            !vehiculoActualizado.getPlaca().equals(vehiculo.getPlaca())) {
            if (vehiculoRepository.findByPlaca(vehiculoActualizado.getPlaca()).isPresent()) {
                throw new RuntimeException("Ya existe un vehículo con la placa: " + vehiculoActualizado.getPlaca());
            }
            vehiculo.setPlaca(vehiculoActualizado.getPlaca());
        }

        // Validar GPS único si se actualiza
        if (vehiculoActualizado.getDispositivoGpsId() != null &&
            !vehiculoActualizado.getDispositivoGpsId().equals(vehiculo.getDispositivoGpsId())) {
            if (vehiculoRepository.findByDispositivoGpsId(vehiculoActualizado.getDispositivoGpsId()).isPresent()) {
                throw new RuntimeException("Ya existe un vehículo con el dispositivo GPS: " + vehiculoActualizado.getDispositivoGpsId());
            }
            vehiculo.setDispositivoGpsId(vehiculoActualizado.getDispositivoGpsId());
        }

        // Actualizar otros campos
        if (vehiculoActualizado.getTipoVehiculo() != null) {
            vehiculo.setTipoVehiculo(vehiculoActualizado.getTipoVehiculo());
        }

        if (vehiculoActualizado.getModelo() != null) {
            vehiculo.setModelo(vehiculoActualizado.getModelo());
        }

        if (vehiculoActualizado.getCapacidad() != null) {
            vehiculo.setCapacidad(vehiculoActualizado.getCapacidad());
        }

        if (vehiculoActualizado.getEstadoOperativo() != null) {
            vehiculo.setEstadoOperativo(vehiculoActualizado.getEstadoOperativo());
        }

        return vehiculoRepository.save(vehiculo);
    }

    @Transactional
    public void eliminarVehiculo(Long id) {
        Vehiculo vehiculo = obtenerVehiculoPorId(id);
        vehiculoRepository.delete(vehiculo);
    }
}
