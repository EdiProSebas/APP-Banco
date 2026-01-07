package com.edisonproa.banco_api.domain.mapper;

import com.edisonproa.banco_api.domain.dto.ClienteCreateRequest;
import com.edisonproa.banco_api.domain.dto.ClienteResponse;
import com.edisonproa.banco_api.domain.dto.ClienteUpdateRequest;
import com.edisonproa.banco_api.domain.entity.Cliente;

public final class ClienteMapper {

    private ClienteMapper() {}

    public static Cliente toEntity(ClienteCreateRequest r) {
        Cliente c = new Cliente();
        c.setClienteId(r.getClienteId());
        c.setContrasena(r.getContrasena()); // luego en service se encripta
        c.setEstado(r.getEstado());
        c.setNombre(r.getNombre());
        c.setGenero(r.getGenero());
        c.setEdad(r.getEdad());
        c.setIdentificacion(r.getIdentificacion());
        c.setDireccion(r.getDireccion());
        c.setTelefono(r.getTelefono());
        return c;
    }

    public static void applyUpdate(Cliente c, ClienteUpdateRequest r) {
        c.setEstado(r.getEstado());
        c.setNombre(r.getNombre());
        c.setGenero(r.getGenero());
        c.setEdad(r.getEdad());
        c.setDireccion(r.getDireccion());
        c.setTelefono(r.getTelefono());
    }

    public static ClienteResponse toResponse(Cliente c) {
        ClienteResponse r = new ClienteResponse();
        r.setId(c.getId());
        r.setClienteId(c.getClienteId());
        r.setEstado(c.getEstado());
        r.setNombre(c.getNombre());
        r.setGenero(c.getGenero());
        r.setEdad(c.getEdad());
        r.setIdentificacion(c.getIdentificacion());
        r.setDireccion(c.getDireccion());
        r.setTelefono(c.getTelefono());
        return r;
    }
}