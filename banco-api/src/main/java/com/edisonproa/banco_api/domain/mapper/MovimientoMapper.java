package com.edisonproa.banco_api.domain.mapper;

import com.edisonproa.banco_api.domain.dto.MovimientoResponse;
import com.edisonproa.banco_api.domain.entity.Movimiento;
import java.math.BigDecimal;

public final class MovimientoMapper {

    private MovimientoMapper() {}

    public static MovimientoResponse toResponse(Movimiento m) {
        MovimientoResponse r = new MovimientoResponse();
        r.setMovimientoId(m.getMovimientoId());
        r.setFecha(m.getFecha());
        r.setTipoMovimiento(m.getTipoMovimiento());

        // Conversión segura Double → BigDecimal
        r.setValor(m.getValor() != null ? BigDecimal.valueOf(m.getValor()) : null);
        r.setSaldo(m.getSaldo() != null ? BigDecimal.valueOf(m.getSaldo()) : null);

        if (m.getCuenta() != null) {
            r.setCuentaId(m.getCuenta().getCuentaId());
            r.setNumeroCuenta(m.getCuenta().getNumeroCuenta());
        }
        return r;
    }
}
