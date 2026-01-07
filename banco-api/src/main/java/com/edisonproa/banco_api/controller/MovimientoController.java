package com.edisonproa.banco_api.controller;

import com.edisonproa.banco_api.domain.dto.MovimientoCreateRequest;
import com.edisonproa.banco_api.domain.dto.MovimientoResponse;
import com.edisonproa.banco_api.service.MovimientoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movimientos")
@RequiredArgsConstructor
public class MovimientoController {

    private final MovimientoService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MovimientoResponse crear(@Valid @RequestBody MovimientoCreateRequest request) {
        return service.crear(request);
    }

    @GetMapping("/{id}")
    public MovimientoResponse obtener(@PathVariable Long id) {
        return service.obtenerPorId(id);
    }

    @GetMapping
    public List<MovimientoResponse> listar(@RequestParam(required = false) Long cuentaId) {
        if (cuentaId != null) return service.listarPorCuenta(cuentaId);
        return service.listar();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}
