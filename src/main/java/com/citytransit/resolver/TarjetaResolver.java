package com.citytransit.resolver;

import com.citytransit.model.dto.response.BalanceInfo;
import com.citytransit.model.dto.response.TransaccionInfo;
import com.citytransit.model.entity.Pasaje;
import com.citytransit.model.entity.Tarjeta;
import com.citytransit.model.entity.Usuario;
import com.citytransit.repository.PasajeRepository;
import com.citytransit.service.TarjetaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class TarjetaResolver {

    private final TarjetaService tarjetaService;
    private final PasajeRepository pasajeRepository;

    @QueryMapping
    public List<Tarjeta> misTarjetas(@AuthenticationPrincipal Usuario usuario) {
        return tarjetaService.getTarjetasByUsuarioId(usuario.getUsuarioId());
    }

    @QueryMapping
    public Tarjeta tarjeta(@Argument Long tarjetaId) {
        return tarjetaService.getTarjetaById(tarjetaId);
    }

    @QueryMapping
    public BalanceInfo balance(@Argument Long tarjetaId) {
        return tarjetaService.getBalance(tarjetaId);
    }

    @QueryMapping
    public List<TransaccionInfo> transacciones(
            @Argument Long tarjetaId,
            @Argument Integer limit,
            @Argument Integer offset
    ) {
        // TODO: Implementar consulta de transacciones
        return List.of();
    }
}
