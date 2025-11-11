package com.citytransit.controller;

import com.citytransit.model.entity.Vehiculo;
import com.citytransit.model.enums.TipoVehiculo;
import com.citytransit.service.VehiculoService;
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
@RequestMapping("/api/vehiculos")
@RequiredArgsConstructor
@Tag(name = "Vehículos", description = "API para gestión de vehículos")
public class VehiculoController {

    private final VehiculoService vehiculoService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear nuevo vehículo", description = "Crea un nuevo vehículo en el sistema")
    public ResponseEntity<Vehiculo> crearVehiculo(@Valid @RequestBody Vehiculo vehiculo) {
        Vehiculo vehiculoCreado = vehiculoService.crearVehiculo(vehiculo);
        return ResponseEntity.status(HttpStatus.CREATED).body(vehiculoCreado);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener vehículo por ID", description = "Obtiene los detalles de un vehículo específico")
    public ResponseEntity<Vehiculo> obtenerVehiculoPorId(@PathVariable Long id) {
        Vehiculo vehiculo = vehiculoService.obtenerVehiculoPorId(id);
        return ResponseEntity.ok(vehiculo);
    }

    @GetMapping("/placa/{placa}")
    @Operation(summary = "Obtener vehículo por placa", description = "Busca un vehículo por su placa")
    public ResponseEntity<Vehiculo> obtenerVehiculoPorPlaca(@PathVariable String placa) {
        Vehiculo vehiculo = vehiculoService.obtenerVehiculoPorPlaca(placa);
        return ResponseEntity.ok(vehiculo);
    }

    @GetMapping("/gps/{dispositivoGpsId}")
    @Operation(summary = "Obtener vehículo por GPS", description = "Busca un vehículo por su dispositivo GPS")
    public ResponseEntity<Vehiculo> obtenerVehiculoPorGps(@PathVariable String dispositivoGpsId) {
        Vehiculo vehiculo = vehiculoService.obtenerVehiculoPorDispositivoGps(dispositivoGpsId);
        return ResponseEntity.ok(vehiculo);
    }

    @GetMapping
    @Operation(summary = "Listar todos los vehículos", description = "Obtiene la lista completa de vehículos")
    public ResponseEntity<List<Vehiculo>> listarTodosLosVehiculos() {
        List<Vehiculo> vehiculos = vehiculoService.listarTodosLosVehiculos();
        return ResponseEntity.ok(vehiculos);
    }

    @GetMapping("/tipo/{tipo}")
    @Operation(summary = "Listar vehículos por tipo", description = "Obtiene vehículos filtrados por tipo")
    public ResponseEntity<List<Vehiculo>> listarVehiculosPorTipo(@PathVariable TipoVehiculo tipo) {
        List<Vehiculo> vehiculos = vehiculoService.listarVehiculosPorTipo(tipo);
        return ResponseEntity.ok(vehiculos);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar vehículo", description = "Actualiza la información de un vehículo")
    public ResponseEntity<Vehiculo> actualizarVehiculo(
            @PathVariable Long id,
            @Valid @RequestBody Vehiculo vehiculo) {
        Vehiculo vehiculoActualizado = vehiculoService.actualizarVehiculo(id, vehiculo);
        return ResponseEntity.ok(vehiculoActualizado);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar vehículo", description = "Elimina un vehículo del sistema")
    public ResponseEntity<Void> eliminarVehiculo(@PathVariable Long id) {
        vehiculoService.eliminarVehiculo(id);
        return ResponseEntity.noContent().build();
    }
}
