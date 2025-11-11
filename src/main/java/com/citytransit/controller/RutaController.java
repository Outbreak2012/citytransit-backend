package com.citytransit.controller;

import com.citytransit.model.entity.Ruta;
import com.citytransit.service.RutaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/rutas")
@RequiredArgsConstructor
@Tag(name = "Rutas", description = "API para gestión de rutas de transporte")
public class RutaController {

    private final RutaService rutaService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear nueva ruta", description = "Crea una nueva ruta de transporte")
    public ResponseEntity<Ruta> crearRuta(@Valid @RequestBody Ruta ruta) {
        Ruta rutaCreada = rutaService.crearRuta(ruta);
        return ResponseEntity.status(HttpStatus.CREATED).body(rutaCreada);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener ruta por ID", description = "Obtiene los detalles de una ruta específica")
    public ResponseEntity<Ruta> obtenerRutaPorId(@PathVariable Long id) {
        Ruta ruta = rutaService.obtenerRutaPorId(id);
        return ResponseEntity.ok(ruta);
    }

    @GetMapping("/codigo/{codigo}")
    @Operation(summary = "Obtener ruta por código", description = "Busca una ruta por su código único")
    public ResponseEntity<Ruta> obtenerRutaPorCodigo(@PathVariable String codigo) {
        Ruta ruta = rutaService.obtenerRutaPorCodigo(codigo);
        return ResponseEntity.ok(ruta);
    }

    @GetMapping
    @Operation(summary = "Listar todas las rutas", description = "Obtiene la lista completa de rutas")
    public ResponseEntity<List<Ruta>> listarTodasLasRutas() {
        List<Ruta> rutas = rutaService.listarTodasLasRutas();
        return ResponseEntity.ok(rutas);
    }

    @GetMapping("/activas")
    @Operation(summary = "Listar rutas activas", description = "Obtiene solo las rutas activas")
    public ResponseEntity<List<Ruta>> listarRutasActivas() {
        List<Ruta> rutas = rutaService.listarRutasActivas();
        return ResponseEntity.ok(rutas);
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar rutas", description = "Busca rutas por nombre u origen/destino")
    public ResponseEntity<List<Ruta>> buscarRutas(@RequestParam String query) {
        List<Ruta> rutas = rutaService.buscarRutas(query);
        return ResponseEntity.ok(rutas);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar ruta", description = "Actualiza la información de una ruta")
    public ResponseEntity<Ruta> actualizarRuta(
            @PathVariable Long id,
            @Valid @RequestBody Ruta ruta) {
        Ruta rutaActualizada = rutaService.actualizarRuta(id, ruta);
        return ResponseEntity.ok(rutaActualizada);
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Cambiar estado de ruta", description = "Activa o desactiva una ruta")
    public ResponseEntity<Ruta> cambiarEstadoRuta(
            @PathVariable Long id,
            @RequestParam boolean activa) {
        Ruta ruta = rutaService.cambiarEstadoRuta(id, activa);
        return ResponseEntity.ok(ruta);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar ruta", description = "Elimina una ruta del sistema")
    public ResponseEntity<Void> eliminarRuta(@PathVariable Long id) {
        rutaService.eliminarRuta(id);
        return ResponseEntity.noContent().build();
    }
}
