package com.edisonproa.banco_api.service;

import com.edisonproa.banco_api.domain.dto.ClienteCreateRequest;
import com.edisonproa.banco_api.domain.dto.ClienteResponse;
import com.edisonproa.banco_api.domain.dto.ClienteUpdateRequest;

import java.util.List;
import java.util.Map;

public interface ClienteService {
    ClienteResponse crear(ClienteCreateRequest request);
    ClienteResponse obtenerPorId(Long id);
    List<ClienteResponse> listar();
    ClienteResponse actualizar(Long id, ClienteUpdateRequest request); // PUT
    ClienteResponse actualizarParcial(Long id, Map<String, Object> patch); // PATCH
    void eliminar(Long id);
}
