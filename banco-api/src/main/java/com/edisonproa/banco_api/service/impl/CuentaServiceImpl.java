package com.edisonproa.banco_api.service.impl;

import com.edisonproa.banco_api.domain.dto.CuentaCreateRequest;
import com.edisonproa.banco_api.domain.dto.CuentaResponse;
import com.edisonproa.banco_api.domain.dto.CuentaUpdateRequest;
import com.edisonproa.banco_api.domain.entity.Cliente;
import com.edisonproa.banco_api.domain.entity.Cuenta;
import com.edisonproa.banco_api.domain.entity.TipoCuenta;
import com.edisonproa.banco_api.domain.mapper.CuentaMapper;
import com.edisonproa.banco_api.exception.BadRequestException;
import com.edisonproa.banco_api.exception.NotFoundException;
import com.edisonproa.banco_api.repository.ClienteRepository;
import com.edisonproa.banco_api.repository.CuentaRepository;
import com.edisonproa.banco_api.service.CuentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class CuentaServiceImpl implements CuentaService {

    private final CuentaRepository cuentaRepo;
    private final ClienteRepository clienteRepo;

    @Override
    @Transactional(readOnly = true)
    public List<CuentaResponse> buscar(String q) {
        String query = q.trim().toLowerCase();
        return cuentaRepo.findAll().stream()
                .filter(c -> c.getNumeroCuenta().toLowerCase().contains(query)
                        || (c.getCliente() != null && c.getCliente().getNombre().toLowerCase().contains(query)))
                .map(CuentaMapper::toResponse)
                .toList();
    }

    @Override
    public CuentaResponse crear(CuentaCreateRequest request) {
        if (cuentaRepo.existsByNumeroCuenta(request.getNumeroCuenta())) {
            throw new BadRequestException("numeroCuenta ya existe");
        }

        Cliente cliente = clienteRepo.findById(request.getClientePk())
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado"));

        Cuenta cuenta = CuentaMapper.toEntity(request, cliente);
        Cuenta saved = cuentaRepo.save(cuenta);
        return CuentaMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public CuentaResponse obtenerPorId(Long id) {
        Cuenta cuenta = cuentaRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Cuenta no encontrada"));
        return CuentaMapper.toResponse(cuenta);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CuentaResponse> listar() {
        return cuentaRepo.findAll().stream().map(CuentaMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CuentaResponse> listarPorCliente(Long clientePk) {
        return cuentaRepo.findByCliente_Id(clientePk).stream().map(CuentaMapper::toResponse).toList();
    }

    @Override
    public CuentaResponse actualizar(Long id, CuentaUpdateRequest request) {
        Cuenta cuenta = cuentaRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Cuenta no encontrada"));

        CuentaMapper.applyUpdate(cuenta, request);
        return CuentaMapper.toResponse(cuenta);
    }

    @Override
    public CuentaResponse actualizarParcial(Long id, Map<String, Object> patch) {
        Cuenta cuenta = cuentaRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Cuenta no encontrada"));

        if (patch.containsKey("estado"))
            cuenta.setEstado((Boolean) patch.get("estado"));

        if (patch.containsKey("tipoCuenta")) {
            // Debe venir "AHORROS" o "CORRIENTE"
            cuenta.setTipoCuenta(TipoCuenta.valueOf((String) patch.get("tipoCuenta")));
        }
        if (patch.containsKey("saldoInicial")) {
            Object v = patch.get("saldoInicial");
            // si viene como number, Spring lo puede mapear a Integer/Double. Convierte a String:
            cuenta.setSaldoInicial(Double.valueOf(String.valueOf(v)));
        }

        return CuentaMapper.toResponse(cuenta);
    }

    @Override
    public void eliminar(Long id) {
        if (!cuentaRepo.existsById(id))
            throw new NotFoundException("Cuenta no encontrada");
        cuentaRepo.deleteById(id);
    }
}
