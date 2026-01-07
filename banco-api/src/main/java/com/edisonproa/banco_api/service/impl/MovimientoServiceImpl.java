package com.edisonproa.banco_api.service.impl;

import com.edisonproa.banco_api.domain.dto.MovimientoCreateRequest;
import com.edisonproa.banco_api.domain.dto.MovimientoResponse;
import com.edisonproa.banco_api.domain.entity.Cuenta;
import com.edisonproa.banco_api.domain.entity.Movimiento;
import com.edisonproa.banco_api.domain.entity.TipoMovimiento;
import com.edisonproa.banco_api.domain.mapper.MovimientoMapper;
import com.edisonproa.banco_api.exception.BadRequestException;
import com.edisonproa.banco_api.exception.NotFoundException;
import com.edisonproa.banco_api.repository.CuentaRepository;
import com.edisonproa.banco_api.repository.MovimientoRepository;
import com.edisonproa.banco_api.service.MovimientoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MovimientoServiceImpl implements MovimientoService {

    private final MovimientoRepository movRepo;
    private final CuentaRepository cuentaRepo;

    @Override
    public MovimientoResponse crear(MovimientoCreateRequest request) {
        Cuenta cuenta = cuentaRepo.findById(request.getCuentaId())
                .orElseThrow(() -> new NotFoundException("Cuenta no encontrada"));

        if (Boolean.FALSE.equals(cuenta.getEstado())) {
            throw new BadRequestException("La cuenta está inactiva");
        }

        Double ultimoSaldo = obtenerSaldoActual(cuenta);
        Double valor = request.getValor() != null ? request.getValor().doubleValue() : null;

        if (valor == null || valor <= 0) {
            throw new BadRequestException("El valor debe ser mayor a 0");
        }

        Double nuevoSaldo = calcularNuevoSaldo(request.getTipoMovimiento(), ultimoSaldo, valor);

        if (nuevoSaldo < 0) {
            throw new BadRequestException("Saldo no disponible");
        }

        Movimiento m = new Movimiento();
        m.setCuenta(cuenta);
        m.setFecha(request.getFecha() != null ? request.getFecha() : LocalDateTime.now());
        m.setTipoMovimiento(request.getTipoMovimiento());
        m.setValor(valor);
        m.setSaldo(nuevoSaldo);

        Movimiento saved = movRepo.save(m);
        return MovimientoMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public MovimientoResponse obtenerPorId(Long id) {
        Movimiento m = movRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Movimiento no encontrado"));
        return MovimientoMapper.toResponse(m);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoResponse> listar() {
        return movRepo.findAll().stream().map(MovimientoMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoResponse> listarPorCuenta(Long cuentaId) {
        return movRepo.findByCuenta_CuentaIdOrderByFechaAsc(cuentaId).stream()
                .map(MovimientoMapper::toResponse)
                .toList();
    }

    @Override
    public void eliminar(Long id) {
        if (!movRepo.existsById(id)) throw new NotFoundException("Movimiento no encontrado");
        movRepo.deleteById(id);
    }

    private Double obtenerSaldoActual(Cuenta cuenta) {
        List<Movimiento> movimientos = movRepo.findByCuenta_CuentaIdOrderByFechaAsc(cuenta.getCuentaId());
        if (movimientos.isEmpty()) return cuenta.getSaldoInicial(); // Double en entidad
        return movimientos.get(movimientos.size() - 1).getSaldo();   // Double en entidad
    }

    private Double calcularNuevoSaldo(TipoMovimiento tipo, Double saldoActual, Double valor) {
        if (tipo == TipoMovimiento.CREDITO) return saldoActual + valor;
        if (tipo == TipoMovimiento.DEBITO) return saldoActual - valor;
        throw new BadRequestException("TipoMovimiento inválido");
    }
}
