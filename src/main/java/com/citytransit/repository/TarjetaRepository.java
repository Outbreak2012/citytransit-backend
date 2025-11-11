package com.citytransit.repository;

import com.citytransit.model.entity.Tarjeta;
import com.citytransit.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TarjetaRepository extends JpaRepository<Tarjeta, Long> {
    List<Tarjeta> findByUsuario(Usuario usuario);
    Optional<Tarjeta> findByNumeroTarjeta(String numeroTarjeta);
    Optional<Tarjeta> findByUidNfc(String uidNfc);
    List<Tarjeta> findByUsuarioUsuarioId(Long usuarioId);
}
