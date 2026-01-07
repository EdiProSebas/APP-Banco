package com.edisonproa.banco_api.service;

import com.edisonproa.banco_api.domain.dto.ReporteResponse;

public interface ReporteService {
    ReporteResponse generarPorCuenta(Long cuentaId);
    byte[] generarPdfPorCuenta(Long cuentaId);
}
