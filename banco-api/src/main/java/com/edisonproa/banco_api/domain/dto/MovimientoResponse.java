package com.edisonproa.banco_api.domain.dto;

import com.edisonproa.banco_api.domain.entity.TipoMovimiento;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class MovimientoResponse {
    private Long movimientoId;
    private LocalDateTime fecha;
    private TipoMovimiento tipoMovimiento;
    private BigDecimal valor;
    private BigDecimal saldo;

    private Long cuentaId;
    private String numeroCuenta;
}
