package com.citytransit.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BalanceInfo {
    private Long tarjetaId;
    private java.math.BigDecimal saldo;
    private String ultimoMovimiento;
}
