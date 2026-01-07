package com.edisonproa.banco_api.domain.mapper;

import com.edisonproa.banco_api.domain.dto.CuentaCreateRequest;
import com.edisonproa.banco_api.domain.dto.CuentaResponse;
import com.edisonproa.banco_api.domain.dto.CuentaUpdateRequest;
import com.edisonproa.banco_api.domain.entity.Cliente;
import com.edisonproa.banco_api.domain.entity.Cuenta;
import java.math.BigDecimal;

public final class CuentaMapper {

    private CuentaMapper() {}

    public static Cuenta toEntity(CuentaCreateRequest r, Cliente cliente) {
        Cuenta c = new Cuenta();
        c.setNumeroCuenta(r.getNumeroCuenta());
        c.setTipoCuenta(r.getTipoCuenta());

        // BigDecimal → Double para entidad
        c.setSaldoInicial(r.getSaldoInicial() != null ? r.getSaldoInicial().doubleValue() : 0.0);
        c.setEstado(r.getEstado());
        c.setCliente(cliente);
        return c;
    }

    public static void applyUpdate(Cuenta cuenta, CuentaUpdateRequest r) {
        cuenta.setTipoCuenta(r.getTipoCuenta());

        // BigDecimal → Double
        cuenta.setSaldoInicial(r.getSaldoInicial() != null ? r.getSaldoInicial().doubleValue() : cuenta.getSaldoInicial());
        cuenta.setEstado(r.getEstado());
    }

    public static CuentaResponse toResponse(Cuenta c) {
        CuentaResponse r = new CuentaResponse();
        r.setCuentaId(c.getCuentaId());
        r.setNumeroCuenta(c.getNumeroCuenta());
        r.setTipoCuenta(c.getTipoCuenta());

        // Double → BigDecimal para contrato monetario
        r.setSaldoInicial(c.getSaldoInicial() != null ? BigDecimal.valueOf(c.getSaldoInicial()) : BigDecimal.ZERO);
        r.setEstado(c.getEstado());

        if (c.getCliente() != null) {
            r.setClientePk(c.getCliente().getId());
            r.setClienteId(c.getCliente().getClienteId());
        }
        return r;
    }
}
