import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';

import { MovimientosComponent } from './MovimientosComponent';

describe('Movimientos', () => {
  let httpMock: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MovimientosComponent],
      providers: [provideHttpClient(), provideHttpClientTesting()],
    }).compileComponents();

    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('debe crear el componente', () => {
    const fixture = TestBed.createComponent(MovimientosComponent);
    fixture.detectChanges();

    const req = httpMock.expectOne('http://localhost:8080/movimientos');
    expect(req.request.method).toBe('GET');
    req.flush([]);

    expect(fixture.componentInstance).toBeTruthy();
  });

  it('debe renderizar filas cuando el API devuelve movimientos', () => {
    const fixture = TestBed.createComponent(MovimientosComponent);
    fixture.detectChanges();

    const req = httpMock.expectOne('http://localhost:8080/movimientos');
    expect(req.request.method).toBe('GET');

    req.flush([
      {
        movimientoId: 10,
        fecha: '2026-01-06T00:00:00',
        tipoMovimiento: 'CREDITO',
        valor: 50,
        saldo: 150,
        cuentaId: 1,
        numeroCuenta: '00000001',
      },
    ]);

    fixture.detectChanges();

    const el: HTMLElement = fixture.nativeElement;
    expect(el.textContent).toContain('10');
    expect(el.textContent).toContain('CREDITO');
    expect(el.textContent).toContain('00000001');
  });
});
