import { Component, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators, FormGroup } from '@angular/forms';

import { ReportesApi, ReporteResponse } from '../../core/api/reportes.api';

@Component({
  selector: 'app-reportes',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './ReportesComponent.html',
  styleUrl: './ReportesComponent.css',
})
export class ReportesComponent {
  error = '';
  reporte: ReporteResponse | null = null;

  form: FormGroup;

  constructor(
    private api: ReportesApi,
    private fb: FormBuilder,
    private cdr: ChangeDetectorRef
  ) {
    this.form = this.fb.group({
      cuentaId: [null, [Validators.required, Validators.min(1)]],
    });
  }

  consultar(): void {
    this.error = '';
    this.reporte = null;

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const cuentaId = Number(this.form.getRawValue().cuentaId);

    this.api.obtenerPorCuenta(cuentaId).subscribe({
      next: (r) => {
        this.reporte = r;
        this.cdr.detectChanges();
      },
      error: (e) => {
        console.error('Error reporte:', e);
        this.error = e?.error?.message ?? 'No se pudo generar el reporte';
        this.cdr.detectChanges();
      },
    });
  }

  descargarPdf(): void {
    this.error = '';

    if (!this.reporte) {
      this.error = 'Primero consulta el reporte';
      this.cdr.detectChanges();
      return;
    }

    const cuentaId = this.reporte.cuentaId;

    this.api.descargarPdfPorCuenta(cuentaId).subscribe({
      next: (blob) => {
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `reporte-cuenta-${cuentaId}.pdf`;
        a.click();
        URL.revokeObjectURL(url);
      },
      error: (e) => {
        console.error('Error PDF:', e);
        this.error = 'No se pudo descargar el PDF';
        this.cdr.detectChanges();
      },
    });
  }

  touchedInvalid(): boolean {
    const ctrl = this.form.get('cuentaId');
    return !!(ctrl && ctrl.touched && ctrl.invalid);
  }
}
