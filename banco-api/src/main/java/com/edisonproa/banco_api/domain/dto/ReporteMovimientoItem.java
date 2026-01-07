package com.edisonproa.banco_api.domain.dto;

import com.edisonproa.banco_api.domain.entity.TipoMovimiento;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class ReporteMovimientoItem {
    private LocalDateTime fecha;
    private String numeroCuenta;
    private TipoMovimiento tipoMovimiento;
    private BigDecimal valor;
    private BigDecimal saldo;
}
