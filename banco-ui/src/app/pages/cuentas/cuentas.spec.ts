import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';

import { CuentasComponent } from './CuentasComponent';

describe('Cuentas', () => {
  let httpMock: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CuentasComponent],
      providers: [provideHttpClient(), provideHttpClientTesting()],
    }).compileComponents();

    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  function flushLookupClientes() {
    const reqClientes = httpMock.expectOne('http://localhost:8080/clientes');
    expect(reqClientes.request.method).toBe('GET');
    reqClientes.flush([
      { id: 1, clienteId: 'CLI-0001', nombre: 'Juan Perez', identificacion: '1712345678', estado: true },
    ]);
  }

  it('debe crear el componente', () => {
    const fixture = TestBed.createComponent(CuentasComponent);
    fixture.detectChanges();

    // 1) lookup clientes
    flushLookupClientes();

    // 2) listar cuentas
    const reqCuentas = httpMock.expectOne('http://localhost:8080/cuentas');
    expect(reqCuentas.request.method).toBe('GET');
    reqCuentas.flush([]);

    expect(fixture.componentInstance).toBeTruthy();
  });

  it('debe renderizar filas cuando el API devuelve cuentas', () => {
    const fixture = TestBed.createComponent(CuentasComponent);
    fixture.detectChanges();

    // 1) lookup clientes
    flushLookupClientes();

    // 2) listar cuentas
    const reqCuentas = httpMock.expectOne('http://localhost:8080/cuentas');
    expect(reqCuentas.request.method).toBe('GET');

    reqCuentas.flush([
      {
        cuentaId: 1,
        numeroCuenta: '00000001',
        tipoCuenta: 'AHORROS',
        saldoInicial: 100,
        estado: true,
        clientePk: 1,
        clienteId: 'CLI-0001',
      },
    ]);

    fixture.detectChanges();

    const el: HTMLElement = fixture.nativeElement;
    expect(el.textContent).toContain('00000001');
    // En tu UI ahora muestra nombre del cliente por lookup, no necesariamente clienteId
    expect(el.textContent).toContain('Juan Perez');
  });

  it('debe manejar error 404 en listar', () => {
    const fixture = TestBed.createComponent(CuentasComponent);
    fixture.detectChanges();

    // 1) lookup clientes
    flushLookupClientes();

    // 2) listar cuentas falla
    const reqCuentas = httpMock.expectOne('http://localhost:8080/cuentas');
    expect(reqCuentas.request.method).toBe('GET');

    reqCuentas.flush(
      { message: 'No se pudo cargar cuentas' },
      { status: 404, statusText: 'Not Found' }
    );

    fixture.detectChanges();

    // Debe setear error (según tu implementación)
    expect(fixture.componentInstance.error).toBeTruthy();
  });
});
