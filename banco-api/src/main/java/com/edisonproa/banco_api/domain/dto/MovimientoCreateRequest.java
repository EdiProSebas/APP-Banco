package com.edisonproa.banco_api.domain.dto;

import com.edisonproa.banco_api.domain.entity.TipoMovimiento;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class MovimientoCreateRequest {

    @NotNull
    private Long cuentaId;

    // si no envías fecha, la ponemos en server
    private LocalDateTime fecha;

    @NotNull
    private TipoMovimiento tipoMovimiento;

    @NotNull
    private BigDecimal valor;
}
