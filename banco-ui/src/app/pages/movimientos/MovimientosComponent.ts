import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators, FormGroup } from '@angular/forms';

import { MovimientosApi, Movimiento, TipoMovimiento, MovimientoCreateRequest } from '../../core/api/movimientos.api';

@Component({
  selector: 'app-movimientos',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './MovimientosComponent.html',
  styleUrl: './MovimientosComponent.css',
})
export class MovimientosComponent implements OnInit {
  movimientos: Movimiento[] = [];
  filtered: Movimiento[] = [];
  search = '';
  error = '';

  filtroCuentaId: number | null = null;

  isModalOpen = false;

  tipos: TipoMovimiento[] = ['CREDITO', 'DEBITO'];

  form: FormGroup;

  constructor(
    private api: MovimientosApi,
    private fb: FormBuilder,
    private cdr: ChangeDetectorRef
  ) {
    this.form = this.fb.group({
      cuentaId: [null, [Validators.required, Validators.min(1)]],
      tipoMovimiento: ['CREDITO' as TipoMovimiento, [Validators.required]],
      valor: [1, [Validators.required, Validators.min(0.01)]],
    });
  }

  ngOnInit(): void {
    this.reload();
  }

  reload(): void {
    this.error = '';
    this.api.listar(this.filtroCuentaId ?? undefined).subscribe({
      next: (data) => {
        this.movimientos = Array.isArray(data) ? data : [];
        this.filtered = [...this.movimientos];
        this.applyFilter();
        this.cdr.detectChanges();
      },
      error: (e) => {
        console.error('Error cargando movimientos:', e);
        this.error = 'No se pudo cargar movimientos';
        this.movimientos = [];
        this.filtered = [];
        this.cdr.detectChanges();
      },
    });
  }

  setFiltroCuentaId(value: string): void {
    const n = Number(value);
    this.filtroCuentaId = Number.isFinite(n) && n > 0 ? n : null;
  }

  aplicarFiltroCuenta(): void {
    this.reload();
  }

  limpiarFiltroCuenta(): void {
    this.filtroCuentaId = null;
    this.reload();
  }

  applyFilter(): void {
    const q = this.search.trim().toLowerCase();
    if (!q) {
      this.filtered = [...this.movimientos];
      return;
    }

    this.filtered = this.movimientos.filter((m) => {
      const numCuenta = (m.numeroCuenta ?? '').toLowerCase();
      const tipo = String(m.tipoMovimiento ?? '').toLowerCase();
      const fecha = String(m.fecha ?? '').toLowerCase();
      return numCuenta.includes(q) || tipo.includes(q) || fecha.includes(q);
    });
  }

  openCreate(): void {
    this.isModalOpen = true;

    this.form.reset({
      cuentaId: this.filtroCuentaId ?? null,
      tipoMovimiento: 'CREDITO' as TipoMovimiento,
      valor: 1,
    });
  }

  closeModal(): void {
    this.isModalOpen = false;
  }

  save(): void {
    this.error = '';

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const raw = this.form.getRawValue();

    const payload: MovimientoCreateRequest = {
      cuentaId: Number(raw.cuentaId),
      tipoMovimiento: raw.tipoMovimiento as TipoMovimiento,
      valor: Number(raw.valor),
    };

    this.api.crear(payload).subscribe({
      next: () => {
        this.closeModal();
        this.reload();
      },
      error: (e) => {
        // Si tu backend devuelve {message: "..."} lo mostramos; si no, fallback
        const msg = e?.error?.message ?? 'No se pudo crear el movimiento';
        this.error = msg;
        this.cdr.detectChanges();
      },
    });
  }

  remove(m: Movimiento): void {
    this.error = '';
    const ok = confirm(`¿Eliminar movimiento ${m.movimientoId}?`);
    if (!ok) return;

    this.api.eliminar(m.movimientoId).subscribe({
      next: () => this.reload(),
      error: () => {
        this.error = 'No se pudo eliminar el movimiento';
        this.cdr.detectChanges();
      },
    });
  }

  touchedInvalid(controlName: string): boolean {
    const ctrl = this.form.get(controlName);
    return !!(ctrl && ctrl.touched && ctrl.invalid);
  }
}
