package com.edisonproa.banco_api.service.impl;

import com.edisonproa.banco_api.domain.dto.ReporteMovimientoItem;
import com.edisonproa.banco_api.domain.dto.ReporteResponse;
import com.edisonproa.banco_api.domain.entity.Cuenta;
import com.edisonproa.banco_api.domain.entity.Movimiento;
import com.edisonproa.banco_api.exception.NotFoundException;
import com.edisonproa.banco_api.repository.CuentaRepository;
import com.edisonproa.banco_api.repository.MovimientoRepository;
import com.edisonproa.banco_api.service.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReporteServiceImpl implements ReporteService {

    private final CuentaRepository cuentaRepo;
    private final MovimientoRepository movRepo;

    @Override
    public ReporteResponse generarPorCuenta(Long cuentaId) {
        Cuenta cuenta = cuentaRepo.findById(cuentaId)
                .orElseThrow(() -> new NotFoundException("Cuenta no encontrada"));

        List<Movimiento> movs = movRepo.findByCuenta_CuentaIdOrderByFechaAsc(cuentaId);

        BigDecimal saldoFinal = movs.isEmpty()
                ? BigDecimal.valueOf(cuenta.getSaldoInicial())
                : BigDecimal.valueOf(movs.get(movs.size() - 1).getSaldo());

        List<ReporteMovimientoItem> items = movs.stream().map(m -> {
            ReporteMovimientoItem it = new ReporteMovimientoItem();
            it.setFecha(m.getFecha());
            it.setNumeroCuenta(cuenta.getNumeroCuenta());
            it.setTipoMovimiento(m.getTipoMovimiento());
            it.setValor(BigDecimal.valueOf(m.getValor()));
            it.setSaldo(BigDecimal.valueOf(m.getSaldo()));
            return it;
        }).toList();

        ReporteResponse r = new ReporteResponse();
        r.setCuentaId(cuenta.getCuentaId());
        r.setNumeroCuenta(cuenta.getNumeroCuenta());
        r.setSaldoFinal(saldoFinal);
        r.setMovimientos(items);
        return r;
    }

    @Override
    public byte[] generarPdfPorCuenta(Long cuentaId) {
        ReporteResponse r = generarPorCuenta(cuentaId);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            com.lowagie.text.Document doc = new com.lowagie.text.Document(com.lowagie.text.PageSize.A4);
            com.lowagie.text.pdf.PdfWriter.getInstance(doc, baos);

            doc.open();

            doc.add(new com.lowagie.text.Paragraph("REPORTE DE MOVIMIENTOS"));
            doc.add(new com.lowagie.text.Paragraph("Cuenta: " + r.getNumeroCuenta() + " (ID " + r.getCuentaId() + ")"));
            doc.add(new com.lowagie.text.Paragraph("Saldo final: " + r.getSaldoFinal()));
            doc.add(new com.lowagie.text.Paragraph(" "));

            com.lowagie.text.pdf.PdfPTable table = new com.lowagie.text.pdf.PdfPTable(4);
            table.setWidthPercentage(100);
            table.addCell("Fecha");
            table.addCell("Tipo");
            table.addCell("Valor");
            table.addCell("Saldo");

            for (var m : r.getMovimientos()) {
                table.addCell(String.valueOf(m.getFecha()));
                table.addCell(String.valueOf(m.getTipoMovimiento()));
                table.addCell(String.valueOf(m.getValor()));
                table.addCell(String.valueOf(m.getSaldo()));
            }

            doc.add(table);
            doc.close();

            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("No se pudo generar el PDF", e);
        }
    }

}
