package com.edisonproa.banco_api.controller;

import com.edisonproa.banco_api.domain.dto.CuentaCreateRequest;
import com.edisonproa.banco_api.domain.dto.CuentaResponse;
import com.edisonproa.banco_api.domain.entity.TipoCuenta;
import com.edisonproa.banco_api.service.CuentaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CuentaControllerTest {

    private final CuentaService cuentaService = Mockito.mock(CuentaService.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(
            new CuentaController(cuentaService)
    ).build();

    @Test
    void debeListarCuentas() throws Exception {
        Mockito.when(cuentaService.listar())
                .thenReturn(List.of());

        mockMvc.perform(get("/cuentas")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void debeCrearCuenta() throws Exception {
        CuentaCreateRequest req = new CuentaCreateRequest();
        req.setNumeroCuenta("00000001");
        req.setTipoCuenta(TipoCuenta.AHORROS);
        req.setSaldoInicial(new BigDecimal("100.00"));
        req.setEstado(true);
        req.setClientePk(1L);

        CuentaResponse mockResp = new CuentaResponse();
        mockResp.setCuentaId(1L);
        mockResp.setNumeroCuenta("00000001");
        mockResp.setTipoCuenta(TipoCuenta.AHORROS);
        mockResp.setSaldoInicial(new BigDecimal("100.00"));
        mockResp.setEstado(true);
        mockResp.setClientePk(1L);
        mockResp.setClienteId("CLI-0001");

        Mockito.when(cuentaService.crear(any())).thenReturn(mockResp);

        mockMvc.perform(post("/cuentas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(containsString("CLI-0001")));
    }
}
