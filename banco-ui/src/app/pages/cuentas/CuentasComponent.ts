import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators, FormGroup } from '@angular/forms';

import { CuentasApi, Cuenta, TipoCuenta, CuentaCreateRequest, CuentaUpdateRequest } from '../../core/api/cuentas.api';
import { ClientesApi, Cliente } from '../../core/api/clientes.api';

@Component({
  selector: 'app-cuentas',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './CuentasComponent.html',
  styleUrl: './CuentasComponent.css',
})
export class CuentasComponent implements OnInit {
  cuentas: Cuenta[] = [];
  filtered: Cuenta[] = [];
  search = '';
  error = '';

  // Mapa clientePk -> nombre
  clienteNombreByPk = new Map<number, string>();

  isModalOpen = false;
  isEdit = false;
  selectedId: number | null = null;

  tiposCuenta: TipoCuenta[] = ['AHORROS', 'CORRIENTE'];

  form: FormGroup;

  constructor(
    private api: CuentasApi,
    private clientesApi: ClientesApi,
    private fb: FormBuilder,
    private cdr: ChangeDetectorRef
  ) {
    this.form = this.fb.group({
      numeroCuenta: ['', [Validators.required, Validators.minLength(6), Validators.maxLength(30)]],
      tipoCuenta: ['AHORROS' as TipoCuenta, [Validators.required]],
      saldoInicial: [0, [Validators.required, Validators.min(0)]],
      estado: [true, [Validators.required]],
      clientePk: [null, [Validators.required, Validators.min(1)]],
    });
  }

  ngOnInit(): void {
    this.cargarClientesLookup();
    this.reload();
  }

  cargarClientesLookup(): void {
    this.clientesApi.listar().subscribe({
      next: (clientes: Cliente[]) => {
        this.clienteNombreByPk.clear();
        for (const c of clientes ?? []) {
          this.clienteNombreByPk.set(c.id, c.nombre);
        }
        this.cdr.detectChanges();
      },
      error: () => {
        // No bloquea la pantalla, solo no mostrará nombres
        console.warn('No se pudo cargar lookup de clientes');
      },
    });
  }

  clienteNombre(c: Cuenta): string {
    return this.clienteNombreByPk.get(c.clientePk) ?? '(Sin nombre)';
  }

  reload(): void {
    this.error = '';
    this.api.listar().subscribe({
      next: (data) => {
        this.cuentas = Array.isArray(data) ? data : [];
        this.filtered = [...this.cuentas];
        this.applyFilter();
        this.cdr.detectChanges();
      },
      error: (e) => {
        console.error('Error cargando cuentas:', e);
        this.error = 'No se pudo cargar cuentas';
        this.cuentas = [];
        this.filtered = [];
        this.cdr.detectChanges();
      },
    });
  }

  applyFilter(): void {
    const q = this.search.trim().toLowerCase();
    if (!q) {
      this.filtered = [...this.cuentas];
      return;
    }

    this.filtered = this.cuentas.filter((c) => {
      const numero = (c.numeroCuenta ?? '').toLowerCase();
      const tipo = String(c.tipoCuenta ?? '').toLowerCase();
      const clienteNombre = (this.clienteNombreByPk.get(c.clientePk) ?? '').toLowerCase();
      return numero.includes(q) || tipo.includes(q) || clienteNombre.includes(q);
    });
  }

  openCreate(): void {
    this.isModalOpen = true;
    this.isEdit = false;
    this.selectedId = null;

    this.form.reset({
      numeroCuenta: '',
      tipoCuenta: 'AHORROS' as TipoCuenta,
      saldoInicial: 0,
      estado: true,
      clientePk: null,
    });

    this.form.get('numeroCuenta')?.enable();
    this.form.get('clientePk')?.enable();
  }

  openEdit(c: Cuenta): void {
    this.isModalOpen = true;
    this.isEdit = true;
    this.selectedId = c.cuentaId;

    this.form.reset({
      numeroCuenta: c.numeroCuenta,
      tipoCuenta: c.tipoCuenta,
      saldoInicial: c.saldoInicial,
      estado: c.estado,
      clientePk: c.clientePk,
    });

    this.form.get('numeroCuenta')?.disable();
    this.form.get('clientePk')?.disable();
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

    if (!this.isEdit) {
      const payload: CuentaCreateRequest = {
        numeroCuenta: String(raw.numeroCuenta),
        tipoCuenta: raw.tipoCuenta as TipoCuenta,
        saldoInicial: Number(raw.saldoInicial),
        estado: !!raw.estado,
        clientePk: Number(raw.clientePk),
      };

      this.api.crear(payload).subscribe({
        next: () => {
          this.closeModal();
          this.reload();
        },
        error: () => {
          this.error = 'No se pudo crear la cuenta';
          this.cdr.detectChanges();
        },
      });
      return;
    }

    const payload: CuentaUpdateRequest = {
      tipoCuenta: raw.tipoCuenta as TipoCuenta,
      saldoInicial: Number(raw.saldoInicial),
      estado: !!raw.estado,
    };

    this.api.actualizar(this.selectedId!, payload).subscribe({
      next: () => {
        this.closeModal();
        this.reload();
      },
      error: () => {
        this.error = 'No se pudo actualizar la cuenta';
        this.cdr.detectChanges();
      },
    });
  }

  remove(c: Cuenta): void {
    this.error = '';
    const ok = confirm(`¿Eliminar cuenta ${c.numeroCuenta}?`);
    if (!ok) return;

    this.api.eliminar(c.cuentaId).subscribe({
      next: () => this.reload(),
      error: () => {
        this.error = 'No se pudo eliminar la cuenta';
        this.cdr.detectChanges();
      },
    });
  }

  touchedInvalid(controlName: string): boolean {
    const ctrl = this.form.get(controlName);
    return !!(ctrl && ctrl.touched && ctrl.invalid);
  }
}
