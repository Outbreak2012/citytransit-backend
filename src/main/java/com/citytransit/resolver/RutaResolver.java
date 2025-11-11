package com.citytransit.resolver;

import com.citytransit.model.entity.Ruta;
import com.citytransit.service.RutaService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class RutaResolver {

    private final RutaService rutaService;

    @QueryMapping
    public List<Ruta> rutas() {
        return rutaService.getAllRutas();
    }

    @QueryMapping
    public List<Ruta> rutasActivas() {
        return rutaService.getRutasActivas();
    }

    @QueryMapping
    public Ruta ruta(@Argument Long rutaId) {
        return rutaService.getRutaById(rutaId);
    }
}
