package com.edisonproa.banco_api.repository;

import com.edisonproa.banco_api.domain.entity.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
    List<Movimiento> findByCuenta_CuentaIdOrderByFechaAsc(Long cuentaId);
}