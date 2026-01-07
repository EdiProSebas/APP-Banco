package com.edisonproa.banco_api.service;

import com.edisonproa.banco_api.domain.dto.MovimientoCreateRequest;
import com.edisonproa.banco_api.domain.dto.MovimientoResponse;

import java.util.List;

public interface MovimientoService {
    MovimientoResponse crear(MovimientoCreateRequest request);
    MovimientoResponse obtenerPorId(Long id);
    List<MovimientoResponse> listar();
    List<MovimientoResponse> listarPorCuenta(Long cuentaId);
    void eliminar(Long id);
}
