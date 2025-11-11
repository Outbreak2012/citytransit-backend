package com.citytransit.service;

import com.citytransit.model.entity.Usuario;
import com.citytransit.model.enums.EstadoUsuario;
import com.citytransit.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario obtenerPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }

    @Transactional
    public Usuario actualizar(Long id, Usuario usuarioActualizado) {
        Usuario usuario = obtenerPorId(id);
        
        if (usuarioActualizado.getNombreCompleto() != null) {
            usuario.setNombreCompleto(usuarioActualizado.getNombreCompleto());
        }
        if (usuarioActualizado.getEmail() != null) {
            usuario.setEmail(usuarioActualizado.getEmail());
        }
        if (usuarioActualizado.getTelefono() != null) {
            usuario.setTelefono(usuarioActualizado.getTelefono());
        }
        if (usuarioActualizado.getRol() != null) {
            usuario.setRol(usuarioActualizado.getRol());
        }
        // Solo actualizar contraseña si se proporciona
        if (usuarioActualizado.getPassword() != null && !usuarioActualizado.getPassword().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(usuarioActualizado.getPassword()));
        }
        
        usuario.setFechaActualizacion(LocalDateTime.now());
        
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void eliminar(Long id) {
        Usuario usuario = obtenerPorId(id);
        usuarioRepository.delete(usuario);
        log.info("Usuario eliminado: {}", usuario.getEmail());
    }

    @Transactional
    public Usuario cambiarEstado(Long id, String estado) {
        Usuario usuario = obtenerPorId(id);
        usuario.setEstado(EstadoUsuario.valueOf(estado));
        usuario.setFechaActualizacion(LocalDateTime.now());
        return usuarioRepository.save(usuario);
    }
}
