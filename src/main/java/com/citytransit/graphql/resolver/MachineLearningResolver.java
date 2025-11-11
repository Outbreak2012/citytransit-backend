package com.citytransit.graphql.resolver;

import com.citytransit.ml.model.DemandPredictionData;
import com.citytransit.ml.model.DemandPredictionResult;
import com.citytransit.ml.service.DemandPredictionService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class MachineLearningResolver {

    private final DemandPredictionService demandPredictionService;

    @QueryMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    public DemandPredictionResult predictDemand(
            @Argument Long rutaId,
            @Argument String fechaHora) {
        
        LocalDateTime targetTime = fechaHora != null ? 
                LocalDateTime.parse(fechaHora) : 
                LocalDateTime.now().plusHours(1);
        
        DemandPredictionData data = buildPredictionData(rutaId, targetTime);
        return demandPredictionService.predictDemand(data);
    }

    @QueryMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    public Boolean isMLModelTrained() {
        return demandPredictionService.isModelTrained();
    }

    private DemandPredictionData buildPredictionData(Long rutaId, LocalDateTime dt) {
        int hour = dt.getHour();
        boolean isRushHour = (hour >= 6 && hour <= 9) || (hour >= 17 && hour <= 20);
        
        return DemandPredictionData.builder()
                .rutaId(rutaId)
                .fechaHora(dt)
                .diaSemana(dt.getDayOfWeek().getValue())
                .hora(hour)
                .mes(dt.getMonthValue())
                .esFeriado(false)
                .esFinDeSemana(dt.getDayOfWeek().getValue() > 5)
                .temperatura(20.0)
                .condicionClimatica("SOLEADO")
                .horaDelDia(hour)
                .minutoDelDia(hour * 60 + dt.getMinute())
                .esHoraPico(isRushHour)
                .build();
    }
}
