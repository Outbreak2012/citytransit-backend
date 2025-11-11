package com.citytransit.service;

import com.citytransit.model.dto.request.LoginRequest;
import com.citytransit.model.dto.request.RegisterRequest;
import com.citytransit.model.dto.response.AuthResponse;
import com.citytransit.model.dto.response.UserResponse;
import com.citytransit.model.entity.Usuario;
import com.citytransit.model.enums.EstadoUsuario;
import com.citytransit.repository.UsuarioRepository;
import com.citytransit.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // Verificar si el usuario ya existe
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        // Crear el usuario
        Usuario usuario = Usuario.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .nombreCompleto(request.getNombreCompleto())
                .telefono(request.getTelefono())
                .rol(request.getRol())
                .estado(EstadoUsuario.ACTIVO)
                .idiomaPreferido(request.getIdiomaPreferido())
                .biometriaHabilitada(false)
                .notificacionesPush(true)
                .ultimoAcceso(LocalDateTime.now())
                .build();

        usuario = usuarioRepository.save(usuario);
        log.info("Usuario registrado: {}", usuario.getEmail());

        // Generar tokens
        String accessToken = jwtTokenProvider.generateToken(usuario);
        String refreshToken = jwtTokenProvider.generateRefreshToken(usuario);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(86400L)
                .user(mapToUserResponse(usuario))
                .build();
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        // Autenticar
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        Usuario usuario = (Usuario) authentication.getPrincipal();

        // Actualizar último acceso
        usuario.setUltimoAcceso(LocalDateTime.now());
        usuarioRepository.save(usuario);

        log.info("Usuario autenticado: {}", usuario.getEmail());

        // Generar tokens
        String accessToken = jwtTokenProvider.generateToken(usuario);
        String refreshToken = jwtTokenProvider.generateRefreshToken(usuario);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(86400L)
                .user(mapToUserResponse(usuario))
                .build();
    }

    private UserResponse mapToUserResponse(Usuario usuario) {
        return UserResponse.builder()
                .usuarioId(usuario.getUsuarioId())
                .email(usuario.getEmail())
                .nombreCompleto(usuario.getNombreCompleto())
                .telefono(usuario.getTelefono())
                .rol(usuario.getRol())
                .estado(usuario.getEstado())
                .fotoPerfilUrl(usuario.getFotoPerfilUrl())
                .biometriaHabilitada(usuario.getBiometriaHabilitada())
                .idiomaPreferido(usuario.getIdiomaPreferido())
                .notificacionesPush(usuario.getNotificacionesPush())
                .fechaCreacion(usuario.getFechaCreacion())
                .ultimoAcceso(usuario.getUltimoAcceso())
                .build();
    }
}
