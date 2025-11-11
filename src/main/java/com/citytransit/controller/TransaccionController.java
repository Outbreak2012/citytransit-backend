package com.citytransit.controller;

import com.citytransit.model.dto.response.TransaccionResponse;
import com.citytransit.model.entity.Transaccion;
import com.citytransit.service.TransaccionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transacciones")
@RequiredArgsConstructor
public class TransaccionController {

    private final TransaccionService transaccionService;

    @GetMapping("/tarjeta/{tarjetaId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PASAJERO', 'OPERADOR')")
    public ResponseEntity<List<TransaccionResponse>> getTransaccionesByTarjeta(@PathVariable Long tarjetaId) {
        // Implementación pendiente
        return ResponseEntity.ok(List.of());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PASAJERO', 'OPERADOR')")
    public ResponseEntity<TransaccionResponse> getTransaccion(@PathVariable Long id) {
        // Implementación pendiente
        return ResponseEntity.ok(new TransaccionResponse());
    }
}
