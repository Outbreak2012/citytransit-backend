package com.citytransit.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransaccionInfo {
    private Long id;
    private String tipo; // RECARGA, PASAJE, TRASBORD, TRANSFER
    private BigDecimal monto;
    private LocalDateTime fecha;
    private String descripcion;
    private String origen;
    private String destino;
    private String numeroRuta;
    private String nombreRuta;
}
