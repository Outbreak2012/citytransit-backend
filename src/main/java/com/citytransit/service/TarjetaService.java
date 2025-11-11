package com.citytransit.service;

import com.citytransit.model.dto.response.BalanceInfo;
import com.citytransit.model.entity.Tarjeta;
import com.citytransit.model.entity.Usuario;
import com.citytransit.model.enums.EstadoTarjeta;
import com.citytransit.repository.PasajeRepository;
import com.citytransit.repository.TarjetaRepository;
import com.citytransit.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TarjetaService {

    private final TarjetaRepository tarjetaRepository;
    private final PasajeRepository pasajeRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public List<Tarjeta> getTarjetasByUsuarioId(Long usuarioId) {
        return tarjetaRepository.findByUsuarioUsuarioId(usuarioId);
    }

    @Transactional(readOnly = true)
    public Tarjeta getTarjetaById(Long tarjetaId) {
        return tarjetaRepository.findById(tarjetaId)
                .orElseThrow(() -> new RuntimeException("Tarjeta no encontrada"));
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "balances", key = "#tarjetaId")
    public BalanceInfo getBalance(Long tarjetaId) {
        Tarjeta tarjeta = getTarjetaById(tarjetaId);
        
        return BalanceInfo.builder()
                .tarjetaId(tarjetaId)
                .saldo(tarjeta.getSaldo())
                .ultimoMovimiento(tarjeta.getFechaActualizacion() != null 
                    ? tarjeta.getFechaActualizacion().toString() 
                    : null)
                .build();
    }

    @Transactional
    public Tarjeta recargarSaldo(Long tarjetaId, BigDecimal monto) {
        Tarjeta tarjeta = getTarjetaById(tarjetaId);
        
        if (monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("El monto de recarga debe ser mayor a cero");
        }
        
        tarjeta.setSaldo(tarjeta.getSaldo().add(monto));
        return tarjetaRepository.save(tarjeta);
    }

    public Tarjeta crearTarjeta(Tarjeta tarjeta) {
        // Si la tarjeta tiene un usuario asignado, cargar el usuario completo
        if (tarjeta.getUsuario() != null && tarjeta.getUsuario().getUsuarioId() != null) {
            Usuario usuario = usuarioRepository.findById(tarjeta.getUsuario().getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + tarjeta.getUsuario().getUsuarioId()));
            tarjeta.setUsuario(usuario);
        }
        return tarjetaRepository.save(tarjeta);
    }

    public Tarjeta obtenerTarjetaPorId(Long id) {
        return getTarjetaById(id);
    }

    public Tarjeta obtenerTarjetaPorNumero(String numeroTarjeta) {
        return tarjetaRepository.findByNumeroTarjeta(numeroTarjeta)
                .orElseThrow(() -> new RuntimeException("Tarjeta no encontrada con número: " + numeroTarjeta));
    }

    public List<Tarjeta> listarTarjetasPorUsuario(Long usuarioId) {
        return getTarjetasByUsuarioId(usuarioId);
    }

    public List<Tarjeta> listarTarjetasActivasPorUsuario(Long usuarioId) {
        return getTarjetasByUsuarioId(usuarioId).stream()
                .filter(t -> t.getEstado() == EstadoTarjeta.ACTIVA)
                .collect(java.util.stream.Collectors.toList());
    }

    public Tarjeta descontarSaldo(Long tarjetaId, BigDecimal monto) {
        Tarjeta tarjeta = getTarjetaById(tarjetaId);
        
        if (monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("El monto de descuento debe ser mayor a cero");
        }
        
        if (tarjeta.getSaldo().compareTo(monto) < 0) {
            throw new RuntimeException("Saldo insuficiente");
        }
        
        tarjeta.setSaldo(tarjeta.getSaldo().subtract(monto));
        return tarjetaRepository.save(tarjeta);
    }

    public Tarjeta cambiarEstadoTarjeta(Long id, EstadoTarjeta nuevoEstado) {
        Tarjeta tarjeta = getTarjetaById(id);
        tarjeta.setEstado(nuevoEstado);
        return tarjetaRepository.save(tarjeta);
    }

    public Tarjeta actualizarTarjeta(Long id, Tarjeta tarjetaActualizada) {
        Tarjeta tarjeta = getTarjetaById(id);
        // Actualizar campos según sea necesario
        return tarjetaRepository.save(tarjeta);
    }

    public void eliminarTarjeta(Long id) {
        Tarjeta tarjeta = getTarjetaById(id);
        tarjetaRepository.delete(tarjeta);
    }

    public List<Tarjeta> listarTodas() {
        return tarjetaRepository.findAll();
    }

    @Transactional
    public Tarjeta actualizar(Long id, Tarjeta tarjetaActualizada) {
        Tarjeta tarjeta = getTarjetaById(id);

        if (tarjetaActualizada.getNumeroTarjeta() != null) {
            tarjeta.setNumeroTarjeta(tarjetaActualizada.getNumeroTarjeta());
        }
        if (tarjetaActualizada.getTipoTarjeta() != null) {
            tarjeta.setTipoTarjeta(tarjetaActualizada.getTipoTarjeta());
        }
        if (tarjetaActualizada.getEstado() != null) {
            tarjeta.setEstado(tarjetaActualizada.getEstado());
        }
        if (tarjetaActualizada.getSaldo() != null) {
            tarjeta.setSaldo(tarjetaActualizada.getSaldo());
        }
        
        // Si viene un usuario nuevo, cargar el usuario completo
        if (tarjetaActualizada.getUsuario() != null && tarjetaActualizada.getUsuario().getUsuarioId() != null) {
            Usuario usuario = usuarioRepository.findById(tarjetaActualizada.getUsuario().getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + tarjetaActualizada.getUsuario().getUsuarioId()));
            tarjeta.setUsuario(usuario);
        }

        return tarjetaRepository.save(tarjeta);
    }    @Transactional
    public void eliminar(Long id) {
        Tarjeta tarjeta = getTarjetaById(id);
        tarjetaRepository.delete(tarjeta);
    }
}
