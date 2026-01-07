package com.edisonproa.banco_api.service.impl;

import com.edisonproa.banco_api.domain.dto.ClienteCreateRequest;
import com.edisonproa.banco_api.domain.dto.ClienteResponse;
import com.edisonproa.banco_api.domain.dto.ClienteUpdateRequest;
import com.edisonproa.banco_api.domain.entity.Cliente;
import com.edisonproa.banco_api.domain.mapper.ClienteMapper;
import com.edisonproa.banco_api.exception.BadRequestException;
import com.edisonproa.banco_api.exception.NotFoundException;
import com.edisonproa.banco_api.repository.ClienteRepository;
import com.edisonproa.banco_api.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository repo;

    @Override
    public ClienteResponse crear(ClienteCreateRequest request) {
        if (repo.existsByClienteId(request.getClienteId())) {
            throw new BadRequestException("clienteId ya existe");
        }
        if (repo.existsByIdentificacion(request.getIdentificacion())) {
            throw new BadRequestException("identificacion ya existe");
        }

        Cliente entity = ClienteMapper.toEntity(request);
        // Extra (luego): encriptar contraseña con BCrypt
        Cliente saved = repo.save(entity);
        return ClienteMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteResponse obtenerPorId(Long id) {
        Cliente c = repo.findById(id).orElseThrow(() -> new NotFoundException("Cliente no encontrado"));
        return ClienteMapper.toResponse(c);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteResponse> listar() {
        return repo.findAll().stream().map(ClienteMapper::toResponse).toList(); // lambda + stream
    }

    @Override
    public ClienteResponse actualizar(Long id, ClienteUpdateRequest request) {
        Cliente c = repo.findById(id).orElseThrow(() -> new NotFoundException("Cliente no encontrado"));
        ClienteMapper.applyUpdate(c, request);
        return ClienteMapper.toResponse(c);
    }

    @Override
    public ClienteResponse actualizarParcial(Long id, Map<String, Object> patch) {
        Cliente c = repo.findById(id).orElseThrow(() -> new NotFoundException("Cliente no encontrado"));

        // PATCH simple, controlado (evita reflection peligrosa)
        if (patch.containsKey("estado")) c.setEstado((Boolean) patch.get("estado"));
        if (patch.containsKey("direccion")) c.setDireccion((String) patch.get("direccion"));
        if (patch.containsKey("telefono")) c.setTelefono((String) patch.get("telefono"));
        if (patch.containsKey("nombre")) c.setNombre((String) patch.get("nombre"));

        return ClienteMapper.toResponse(c);
    }

    @Override
    public void eliminar(Long id) {
        if (!repo.existsById(id)) throw new NotFoundException("Cliente no encontrado");
        repo.deleteById(id);
    }
}
