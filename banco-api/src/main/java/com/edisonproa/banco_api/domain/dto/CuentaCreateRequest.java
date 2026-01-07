package com.edisonproa.banco_api.domain.dto;

import com.edisonproa.banco_api.domain.entity.TipoCuenta;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CuentaCreateRequest {

    @NotBlank
    @Size(min = 6, max = 30)
    private String numeroCuenta;

    @NotNull
    private TipoCuenta tipoCuenta;

    @NotNull
    @DecimalMin(value = "0.00")
    private BigDecimal saldoInicial;

    @NotNull
    private Boolean estado;

    // PK técnica del cliente (Persona.id)
    @NotNull
    private Long clientePk;
}
