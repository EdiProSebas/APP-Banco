package com.edisonproa.banco_api.controller;

import com.edisonproa.banco_api.domain.dto.CuentaCreateRequest;
import com.edisonproa.banco_api.domain.dto.CuentaResponse;
import com.edisonproa.banco_api.domain.dto.CuentaUpdateRequest;
import com.edisonproa.banco_api.service.CuentaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cuentas")
@RequiredArgsConstructor
public class CuentaController {

    private final CuentaService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CuentaResponse crear(@Valid @RequestBody CuentaCreateRequest request) {
        return service.crear(request);
    }

    @GetMapping("/{id}")
    public CuentaResponse obtener(@PathVariable Long id) {
        return service.obtenerPorId(id);
    }

    @GetMapping
    public List<CuentaResponse> listar(@RequestParam(required = false) Long clientePk) {
        if (clientePk != null) return service.listarPorCliente(clientePk);
        return service.listar();
    }

    @PutMapping("/{id}")
    public CuentaResponse actualizar(@PathVariable Long id, @Valid @RequestBody CuentaUpdateRequest request) {
        return service.actualizar(id, request);
    }

    @PatchMapping("/{id}")
    public CuentaResponse patch(@PathVariable Long id, @RequestBody Map<String, Object> patch) {
        return service.actualizarParcial(id, patch);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}
