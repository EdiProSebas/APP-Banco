package com.edisonproa.banco_api.domain.dto;

import com.edisonproa.banco_api.domain.entity.TipoCuenta;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CuentaUpdateRequest {

    @NotNull
    private TipoCuenta tipoCuenta;

    @NotNull
    @DecimalMin(value = "0.00")
    private BigDecimal saldoInicial;

    @NotNull
    private Boolean estado;
}
