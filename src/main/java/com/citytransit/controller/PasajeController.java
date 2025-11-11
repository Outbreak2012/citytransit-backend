package com.citytransit.controller;

import com.citytransit.model.entity.Pasaje;
import com.citytransit.model.enums.EstadoPasaje;
import com.citytransit.service.PasajeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pasajes")
@RequiredArgsConstructor
@Tag(name = "Pasajes", description = "API para gestión de pasajes y transacciones")
public class PasajeController {

    private final PasajeService pasajeService;

    @PostMapping("/registrar")
    @Operation(summary = "Registrar pasaje", description = "Registra una nueva transacción de pasaje")
    public ResponseEntity<Pasaje> registrarPasaje(@RequestBody Map<String, Object> request) {
        Long tarjetaId = Long.valueOf(request.get("tarjetaId").toString());
        Long rutaId = Long.valueOf(request.get("rutaId").toString());
        Long vehiculoId = Long.valueOf(request.get("vehiculoId").toString());
        String ubicacionOrigen = request.get("ubicacionOrigen") != null ? 
                request.get("ubicacionOrigen").toString() : "Desconocido";
        
        Pasaje pasaje = pasajeService.registrarPasaje(tarjetaId, rutaId, vehiculoId, ubicacionOrigen);
        return ResponseEntity.status(HttpStatus.CREATED).body(pasaje);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener pasaje por ID", description = "Obtiene los detalles de un pasaje específico")
    public ResponseEntity<Pasaje> obtenerPasajePorId(@PathVariable Long id) {
        Pasaje pasaje = pasajeService.obtenerPasajePorId(id);
        return ResponseEntity.ok(pasaje);
    }

    @GetMapping("/tarjeta/{tarjetaId}")
    @Operation(summary = "Listar pasajes por tarjeta", description = "Obtiene el historial de pasajes de una tarjeta")
    public ResponseEntity<List<Pasaje>> listarPasajesPorTarjeta(@PathVariable Long tarjetaId) {
        List<Pasaje> pasajes = pasajeService.listarPasajesPorTarjeta(tarjetaId);
        return ResponseEntity.ok(pasajes);
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Listar pasajes por usuario", description = "Obtiene el historial de pasajes de un usuario")
    public ResponseEntity<List<Pasaje>> listarPasajesPorUsuario(@PathVariable Long usuarioId) {
        List<Pasaje> pasajes = pasajeService.listarPasajesPorUsuario(usuarioId);
        return ResponseEntity.ok(pasajes);
    }

    @GetMapping("/vehiculo/{vehiculoId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CONDUCTOR')")
    @Operation(summary = "Listar pasajes por vehículo", description = "Obtiene el historial de pasajes de un vehículo")
    public ResponseEntity<List<Pasaje>> listarPasajesPorVehiculo(@PathVariable Long vehiculoId) {
        List<Pasaje> pasajes = pasajeService.listarPasajesPorVehiculo(vehiculoId);
        return ResponseEntity.ok(pasajes);
    }

    @GetMapping("/ruta/{rutaId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar pasajes por ruta", description = "Obtiene el historial de pasajes de una ruta")
    public ResponseEntity<List<Pasaje>> listarPasajesPorRuta(@PathVariable Long rutaId) {
        List<Pasaje> pasajes = pasajeService.listarPasajesPorRuta(rutaId);
        return ResponseEntity.ok(pasajes);
    }

    @GetMapping("/fecha")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar pasajes por fecha", description = "Obtiene pasajes en un rango de fechas")
    public ResponseEntity<List<Pasaje>> listarPasajesPorFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        List<Pasaje> pasajes = pasajeService.listarPasajesPorFecha(fechaInicio, fechaFin);
        return ResponseEntity.ok(pasajes);
    }

    @GetMapping("/estado/{estado}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar pasajes por estado", description = "Obtiene pasajes filtrados por estado")
    public ResponseEntity<List<Pasaje>> listarPasajesPorEstado(@PathVariable EstadoPasaje estado) {
        List<Pasaje> pasajes = pasajeService.listarPasajesPorEstado(estado);
        return ResponseEntity.ok(pasajes);
    }

    @GetMapping("/tarjeta/{tarjetaId}/total")
    @Operation(summary = "Calcular total gastado", description = "Calcula el total gastado en una tarjeta")
    public ResponseEntity<Map<String, Object>> calcularTotalPorTarjeta(@PathVariable Long tarjetaId) {
        BigDecimal total = pasajeService.calcularTotalPasajesPorTarjeta(tarjetaId);
        Long cantidad = pasajeService.contarPasajesPorTarjeta(tarjetaId);
        
        return ResponseEntity.ok(Map.of(
            "tarjetaId", tarjetaId,
            "totalGastado", total,
            "cantidadPasajes", cantidad
        ));
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar estado del pasaje", description = "Cambia el estado de un pasaje")
    public ResponseEntity<Pasaje> actualizarEstadoPasaje(
            @PathVariable Long id,
            @RequestParam EstadoPasaje nuevoEstado) {
        Pasaje pasaje = pasajeService.actualizarEstadoPasaje(id, nuevoEstado);
        return ResponseEntity.ok(pasaje);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar pasaje", description = "Elimina un pasaje del sistema")
    public ResponseEntity<Void> eliminarPasaje(@PathVariable Long id) {
        pasajeService.eliminarPasaje(id);
        return ResponseEntity.noContent().build();
    }
}
