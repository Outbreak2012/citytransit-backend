package com.citytransit.service;

import com.citytransit.model.entity.Pasaje;
import com.citytransit.model.entity.Ruta;
import com.citytransit.model.entity.Tarjeta;
import com.citytransit.model.entity.Vehiculo;
import com.citytransit.model.enums.EstadoPasaje;
import com.citytransit.repository.PasajeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PasajeService {

    private final PasajeRepository pasajeRepository;
    private final TarjetaService tarjetaService;
    private final RutaService rutaService;
    private final VehiculoService vehiculoService;

    @Transactional
    public Pasaje registrarPasaje(Long tarjetaId, Long rutaId, Long vehiculoId, String ubicacionOrigen) {
        // Obtener entidades
        Tarjeta tarjeta = tarjetaService.obtenerTarjetaPorId(tarjetaId);
        Ruta ruta = rutaService.obtenerRutaPorId(rutaId);
        Vehiculo vehiculo = vehiculoService.obtenerVehiculoPorId(vehiculoId);

        // Validar saldo
        BigDecimal precioPasaje = ruta.getPrecioBase() != null ? ruta.getPrecioBase() : new BigDecimal("2500.00");
        if (tarjeta.getSaldo().compareTo(precioPasaje) < 0) {
            throw new RuntimeException("Saldo insuficiente. Saldo actual: " + tarjeta.getSaldo() + ", Precio pasaje: " + precioPasaje);
        }

        // Crear pasaje usando builder
        Pasaje pasaje = Pasaje.builder()
                .tarjeta(tarjeta)
                .vehiculo(vehiculo)
                .ruta(ruta)
                .monto(precioPasaje)
                .fechaHoraValidacion(LocalDateTime.now())
                .estado(EstadoPasaje.VALIDADO)
                .latitudValidacion(0.0)  // Por implementar con GPS real
                .longitudValidacion(0.0) // Por implementar con GPS real
                .build();

        // Guardar pasaje
        Pasaje pasajeGuardado = pasajeRepository.save(pasaje);

        // Descontar saldo de tarjeta
        tarjetaService.descontarSaldo(tarjetaId, precioPasaje);

        return pasajeGuardado;
    }

    public Pasaje obtenerPasajePorId(Long id) {
        return pasajeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pasaje no encontrado con ID: " + id));
    }

    public List<Pasaje> listarPasajesPorTarjeta(Long tarjetaId) {
        return pasajeRepository.findByTarjetaTarjetaId(tarjetaId);
    }

    public List<Pasaje> listarPasajesPorUsuario(Long usuarioId) {
        return pasajeRepository.findByTarjetaUsuarioUsuarioId(usuarioId);
    }

    public List<Pasaje> listarPasajesPorVehiculo(Long vehiculoId) {
        return pasajeRepository.findByVehiculoVehiculoId(vehiculoId);
    }

    public List<Pasaje> listarPasajesPorRuta(Long rutaId) {
        return pasajeRepository.findByRutaRutaId(rutaId);
    }

    public List<Pasaje> listarTodosLosPasajes() {
        return pasajeRepository.findAll();
    }

    public BigDecimal calcularTotalPasajesPorTarjeta(Long tarjetaId) {
        List<Pasaje> pasajes = listarPasajesPorTarjeta(tarjetaId);
        return pasajes.stream()
                .map(Pasaje::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Long contarPasajesPorTarjeta(Long tarjetaId) {
        return (long) listarPasajesPorTarjeta(tarjetaId).size();
    }

    @Transactional
    public Pasaje actualizarEstadoPasaje(Long id, EstadoPasaje nuevoEstado) {
        Pasaje pasaje = obtenerPasajePorId(id);
        pasaje.setEstado(nuevoEstado);
        return pasajeRepository.save(pasaje);
    }

    public List<Pasaje> listarPasajesPorFecha(LocalDateTime inicio, LocalDateTime fin) {
        return pasajeRepository.findAll().stream()
                .filter(p -> p.getFechaHoraValidacion().isAfter(inicio) && p.getFechaHoraValidacion().isBefore(fin))
                .collect(java.util.stream.Collectors.toList());
    }

    public List<Pasaje> listarPasajesPorEstado(EstadoPasaje estado) {
        return pasajeRepository.findByEstado(estado);
    }

    @Transactional
    public void eliminarPasaje(Long id) {
        Pasaje pasaje = obtenerPasajePorId(id);
        pasajeRepository.delete(pasaje);
    }
}
