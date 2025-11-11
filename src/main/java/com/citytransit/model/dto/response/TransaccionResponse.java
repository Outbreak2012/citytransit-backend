package com.citytransit.model.dto.response;

import com.citytransit.model.enums.TipoTransaccion;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransaccionResponse {
    private Long transaccionId;
    private Long tarjetaId;
    private TipoTransaccion tipoTransaccion;
    private BigDecimal monto;
    private LocalDateTime fechaHora;
    private String descripcion;
}
