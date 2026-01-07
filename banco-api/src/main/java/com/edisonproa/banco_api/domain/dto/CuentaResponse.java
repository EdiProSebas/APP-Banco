package com.edisonproa.banco_api.domain.dto;

import com.edisonproa.banco_api.domain.entity.TipoCuenta;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CuentaResponse {
    private Long cuentaId;
    private String numeroCuenta;
    private TipoCuenta tipoCuenta;
    private BigDecimal saldoInicial;
    private Boolean estado;

    private Long clientePk;
    private String clienteId; // business key (opcional útil para UI)
}
