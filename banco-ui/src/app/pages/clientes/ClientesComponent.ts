import { Component, OnInit, ChangeDetectorRef  } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators, FormGroup } from '@angular/forms';
import {
  ClientesApi,
  Cliente,
  Genero,
  ClienteCreateRequest,
  ClienteUpdateRequest,
} from '../../core/api/clientes.api';

@Component({
  selector: 'app-clientes',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './ClientesComponent.html',
  styleUrl: './ClientesComponent.css',
})
export class ClientesComponent implements OnInit {
  clientes: Cliente[] = [];
  filtered: Cliente[] = [];
  search = '';
  error = '';

  isModalOpen = false;
  isEdit = false;
  selectedId: number | null = null;

  generos: Genero[] = ['MASCULINO', 'FEMENINO', 'OTRO'];

  form: FormGroup;

  constructor(private api: ClientesApi, private fb: FormBuilder, private cdr: ChangeDetectorRef) {
    // IMPORTANTE: inicializar aquí para evitar "fb usado antes de inicialización"
    this.form = this.fb.group({
      clienteId: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(30)]],
      contrasena: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(100)]],
      estado: [true, [Validators.required]],
      nombre: ['', [Validators.required, Validators.maxLength(120)]],
      genero: ['MASCULINO' as Genero, [Validators.required]],
      edad: [18, [Validators.required, Validators.min(0), Validators.max(120)]],
      identificacion: [
        '',
        [Validators.required, Validators.minLength(5), Validators.maxLength(30)],
      ],
      direccion: ['', [Validators.required, Validators.maxLength(200)]],
      telefono: ['', [Validators.required, Validators.minLength(7), Validators.maxLength(20)]],
    });
  }

  ngOnInit(): void {
    this.reload();
  }

  reload(): void {
  this.error = '';
  this.api.listar().subscribe({
    next: (data) => {
      this.clientes = Array.isArray(data) ? data : [];
      this.filtered = [...this.clientes];
      this.cdr.detectChanges(); // <-- clave
    },
    error: (e) => {
      console.error('Error cargando clientes:', e);
      this.error = 'No se pudo cargar clientes';
      this.clientes = [];
      this.filtered = [];
      this.cdr.detectChanges();
    },
  });
}

  applyFilter(): void {
    const q = this.search.trim().toLowerCase();

    if (!q) {
      this.filtered = this.clientes;
      return;
    }

    this.filtered = this.clientes.filter((c) => {
      const clienteId = (c.clienteId ?? '').toLowerCase();
      const nombre = (c.nombre ?? '').toLowerCase();
      const identificacion = (c.identificacion ?? '').toLowerCase();

      return clienteId.includes(q) || nombre.includes(q) || identificacion.includes(q);
    });
  }

  openCreate(): void {
    this.isModalOpen = true;
    this.isEdit = false;
    this.selectedId = null;

    this.form.reset({
      clienteId: '',
      contrasena: '',
      estado: true,
      nombre: '',
      genero: 'MASCULINO' as Genero,
      edad: 18,
      identificacion: '',
      direccion: '',
      telefono: '',
    });

    // contraseña requerida en creación
    this.form
      .get('contrasena')
      ?.setValidators([Validators.required, Validators.minLength(8), Validators.maxLength(100)]);
    this.form.get('contrasena')?.updateValueAndValidity();
  }

  openEdit(c: Cliente): void {
    this.isModalOpen = true;
    this.isEdit = true;
    this.selectedId = c.id;

    this.form.reset({
      clienteId: c.clienteId,
      contrasena: '', // no se edita aquí
      estado: c.estado,
      nombre: c.nombre,
      genero: c.genero,
      edad: c.edad,
      identificacion: c.identificacion,
      direccion: c.direccion,
      telefono: c.telefono,
    });

    // contraseña no aplica en edición (PUT no la recibe)
    this.form.get('contrasena')?.clearValidators();
    this.form.get('contrasena')?.updateValueAndValidity();
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
      const payload: ClienteCreateRequest = {
        clienteId: raw.clienteId,
        contrasena: raw.contrasena,
        estado: !!raw.estado,
        nombre: raw.nombre,
        genero: raw.genero as Genero,
        edad: Number(raw.edad),
        identificacion: raw.identificacion,
        direccion: raw.direccion,
        telefono: raw.telefono,
      };

      this.api.crear(payload).subscribe({
        next: () => {
          this.closeModal();
          this.reload();
        },
        error: () => {
          this.error = 'No se pudo crear el cliente (revisa duplicados o validaciones)';
        },
      });
      return;
    }

    const payload: ClienteUpdateRequest = {
      estado: !!raw.estado,
      nombre: raw.nombre,
      genero: raw.genero as Genero,
      edad: Number(raw.edad),
      direccion: raw.direccion,
      telefono: raw.telefono,
    };

    this.api.actualizar(this.selectedId!, payload).subscribe({
      next: () => {
        this.closeModal();
        this.reload();
      },
      error: () => {
        this.error = 'No se pudo actualizar el cliente';
      },
    });
  }

  remove(c: Cliente): void {
    this.error = '';
    const ok = confirm(`¿Eliminar cliente ${c.clienteId}?`);
    if (!ok) return;

    this.api.eliminar(c.id).subscribe({
      next: () => this.reload(),
      error: () => (this.error = 'No se pudo eliminar el cliente'),
    });
  }

  touchedInvalid(controlName: string): boolean {
    const ctrl = this.form.get(controlName);
    return !!(ctrl && ctrl.touched && ctrl.invalid);
  }
}
