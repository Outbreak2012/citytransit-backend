package com.citytransit.controller;

import com.citytransit.model.entity.Tarjeta;
import com.citytransit.service.TarjetaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tarjetas")
@RequiredArgsConstructor
@Tag(name = "Tarjetas", description = "Gestión de tarjetas de transporte")
public class TarjetaController {

    private final TarjetaService tarjetaService;

    @GetMapping
    @Operation(summary = "Listar todas las tarjetas", description = "Lista todas las tarjetas del sistema")
    public ResponseEntity<List<Tarjeta>> listarTarjetas() {
        return ResponseEntity.ok(tarjetaService.listarTodas());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Crear tarjeta", description = "Crea una nueva tarjeta de transporte")
    public ResponseEntity<Tarjeta> crearTarjeta(@RequestBody Tarjeta tarjeta) {
        return ResponseEntity.ok(tarjetaService.crearTarjeta(tarjeta));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Actualizar tarjeta", description = "Actualiza una tarjeta existente")
    public ResponseEntity<Tarjeta> actualizarTarjeta(@PathVariable Long id, @RequestBody Tarjeta tarjeta) {
        return ResponseEntity.ok(tarjetaService.actualizar(id, tarjeta));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Eliminar tarjeta", description = "Elimina una tarjeta del sistema")
    public ResponseEntity<Void> eliminarTarjeta(@PathVariable Long id) {
        tarjetaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener tarjeta", description = "Obtiene una tarjeta por ID")
    public ResponseEntity<Tarjeta> obtenerTarjeta(@PathVariable Long id) {
        return ResponseEntity.ok(tarjetaService.obtenerTarjetaPorId(id));
    }

    @GetMapping("/numero/{numeroTarjeta}")
    @Operation(summary = "Obtener tarjeta por número", description = "Obtiene una tarjeta por número")
    public ResponseEntity<Tarjeta> obtenerTarjetaPorNumero(@PathVariable String numeroTarjeta) {
        return ResponseEntity.ok(tarjetaService.obtenerTarjetaPorNumero(numeroTarjeta));
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Listar tarjetas por usuario", description = "Lista todas las tarjetas de un usuario")
    public ResponseEntity<List<Tarjeta>> listarTarjetasPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(tarjetaService.listarTarjetasPorUsuario(usuarioId));
    }

    @PostMapping("/{id}/recargar")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Recargar saldo", description = "Recarga saldo en una tarjeta")
    public ResponseEntity<Map<String, Object>> recargarSaldo(
            @PathVariable Long id,
            @RequestParam BigDecimal monto) {
        
        Tarjeta tarjeta = tarjetaService.recargarSaldo(id, monto);
        
        return ResponseEntity.ok(Map.of(
            "message", "Recarga exitosa",
            "tarjetaId", tarjeta.getTarjetaId(),
            "saldoActual", tarjeta.getSaldo()
        ));
    }

    @PostMapping("/{id}/descontar")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    @Operation(summary = "Descontar saldo", description = "Descuenta saldo de una tarjeta")
    public ResponseEntity<Map<String, Object>> descontarSaldo(
            @PathVariable Long id,
            @RequestParam BigDecimal monto) {
        
        Tarjeta tarjeta = tarjetaService.descontarSaldo(id, monto);
        
        return ResponseEntity.ok(Map.of(
            "message", "Descuento exitoso",
            "tarjetaId", tarjeta.getTarjetaId(),
            "saldoRestante", tarjeta.getSaldo()
        ));
    }

    @GetMapping("/{id}/saldo")
    @Operation(summary = "Consultar saldo", description = "Consulta el saldo disponible de una tarjeta")
    public ResponseEntity<Map<String, Object>> consultarSaldo(@PathVariable Long id) {
        Tarjeta tarjeta = tarjetaService.obtenerTarjetaPorId(id);
        
        return ResponseEntity.ok(Map.of(
            "tarjetaId", tarjeta.getTarjetaId(),
            "saldo", tarjeta.getSaldo(),
            "estado", tarjeta.getEstado()
        ));
    }
}
