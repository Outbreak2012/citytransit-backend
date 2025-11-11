package com.citytransit.service;

import com.citytransit.model.entity.Ruta;
import com.citytransit.repository.RutaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RutaService {

    private final RutaRepository rutaRepository;

    @Transactional(readOnly = true)
    @Cacheable(value = "rutas")
    public List<Ruta> getAllRutas() {
        return rutaRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "rutasActivas")
    public List<Ruta> getRutasActivas() {
        return rutaRepository.findByActivaTrue();
    }

    @Transactional(readOnly = true)
    public Ruta getRutaById(Long rutaId) {
        return rutaRepository.findById(rutaId)
                .orElseThrow(() -> new RuntimeException("Ruta no encontrada con id: " + rutaId));
    }

    public Ruta crearRuta(Ruta ruta) {
        return rutaRepository.save(ruta);
    }

    public Ruta obtenerRutaPorId(Long id) {
        return getRutaById(id);
    }

    public Ruta obtenerRutaPorCodigo(String codigo) {
        return getAllRutas().stream()
                .filter(r -> codigo.equals(r.getCodigoRuta()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Ruta no encontrada con código: " + codigo));
    }

    public List<Ruta> listarTodasLasRutas() {
        return getAllRutas();
    }

    public List<Ruta> listarRutasActivas() {
        return getRutasActivas();
    }

    public List<Ruta> buscarRutas(String query) {
        return getAllRutas().stream()
                .filter(r -> r.getNombre().toLowerCase().contains(query.toLowerCase()))
                .collect(java.util.stream.Collectors.toList());
    }

    public Ruta actualizarRuta(Long id, Ruta rutaActualizada) {
        Ruta ruta = getRutaById(id);
        if (rutaActualizada.getNombre() != null) {
            ruta.setNombre(rutaActualizada.getNombre());
        }
        if (rutaActualizada.getDescripcion() != null) {
            ruta.setDescripcion(rutaActualizada.getDescripcion());
        }
        return rutaRepository.save(ruta);
    }

    public Ruta cambiarEstadoRuta(Long id, boolean activa) {
        Ruta ruta = getRutaById(id);
        ruta.setActiva(activa);
        return rutaRepository.save(ruta);
    }

    public void eliminarRuta(Long id) {
        Ruta ruta = getRutaById(id);
        rutaRepository.delete(ruta);
    }
}
