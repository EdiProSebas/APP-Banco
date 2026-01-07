package com.edisonproa.banco_api.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class ReporteResponse {
    private Long cuentaId;
    private String numeroCuenta;
    private BigDecimal saldoFinal;
    private List<ReporteMovimientoItem> movimientos;
}
