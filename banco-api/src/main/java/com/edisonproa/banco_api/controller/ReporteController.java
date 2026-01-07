package com.edisonproa.banco_api.controller;

import com.edisonproa.banco_api.domain.dto.ReporteResponse;
import com.edisonproa.banco_api.service.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteService service;

    @GetMapping("/cuentas/{cuentaId}")
    public ReporteResponse reportePorCuenta(@PathVariable Long cuentaId) {
        return service.generarPorCuenta(cuentaId);
    }

    @GetMapping("/cuentas/{cuentaId}/pdf")
    public ResponseEntity<byte[]> reportePdf(@PathVariable Long cuentaId) {
        byte[] bytes = service.generarPdfPorCuenta(cuentaId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reporte-cuenta-" + cuentaId + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(bytes);
    }

}
