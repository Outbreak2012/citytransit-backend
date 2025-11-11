package com.citytransit.model.dto.response;

import com.citytransit.model.enums.EstadoUsuario;
import com.citytransit.model.enums.RolUsuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long usuarioId;
    private String email;
    private String nombreCompleto;
    private String telefono;
    private RolUsuario rol;
    private EstadoUsuario estado;
    private String fotoPerfilUrl;
    private Boolean biometriaHabilitada;
    private String idiomaPreferido;
    private Boolean notificacionesPush;
    private LocalDateTime fechaCreacion;
    private LocalDateTime ultimoAcceso;
}
