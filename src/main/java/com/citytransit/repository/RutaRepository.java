package com.citytransit.repository;

import com.citytransit.model.entity.Ruta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RutaRepository extends JpaRepository<Ruta, Long> {
    Optional<Ruta> findByCodigoRuta(String codigoRuta);
    List<Ruta> findByActivaTrue();
}
