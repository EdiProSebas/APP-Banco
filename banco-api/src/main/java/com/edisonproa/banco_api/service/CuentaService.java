package com.edisonproa.banco_api.service;

import com.edisonproa.banco_api.domain.dto.CuentaCreateRequest;
import com.edisonproa.banco_api.domain.dto.CuentaResponse;
import com.edisonproa.banco_api.domain.dto.CuentaUpdateRequest;

import java.util.List;
import java.util.Map;

public interface CuentaService {
    CuentaResponse crear(CuentaCreateRequest request);
    CuentaResponse obtenerPorId(Long id);
    List<CuentaResponse> listar();
    List<CuentaResponse> listarPorCliente(Long clientePk);
    List<CuentaResponse> buscar(String q);
    CuentaResponse actualizar(Long id, CuentaUpdateRequest request); // PUT
    CuentaResponse actualizarParcial(Long id, Map<String, Object> patch); // PATCH
    void eliminar(Long id);
}