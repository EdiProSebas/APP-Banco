package com.edisonproa.banco_api.controller;

import com.edisonproa.banco_api.domain.dto.ClienteCreateRequest;
import com.edisonproa.banco_api.domain.dto.ClienteResponse;
import com.edisonproa.banco_api.domain.dto.ClienteUpdateRequest;
import com.edisonproa.banco_api.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteResponse crear(@Valid @RequestBody ClienteCreateRequest request) {
        return service.crear(request);
    }

    @GetMapping("/{id}")
    public ClienteResponse obtener(@PathVariable Long id) {
        return service.obtenerPorId(id);
    }

    @GetMapping
    public List<ClienteResponse> listar() {
        return service.listar();
    }

    @PutMapping("/{id}")
    public ClienteResponse actualizar(@PathVariable Long id, @Valid @RequestBody ClienteUpdateRequest request) {
        return service.actualizar(id, request);
    }

    @PatchMapping("/{id}")
    public ClienteResponse patch(@PathVariable Long id, @RequestBody Map<String, Object> patch) {
        return service.actualizarParcial(id, patch);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}
