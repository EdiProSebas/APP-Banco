import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';

import { ReportesComponent } from './ReportesComponent';

describe('Reportes', () => {
  let httpMock: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReportesComponent],
      providers: [provideHttpClient(), provideHttpClientTesting()],
    }).compileComponents();

    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('debe crear el componente', () => {
    const fixture = TestBed.createComponent(ReportesComponent);
    expect(fixture.componentInstance).toBeTruthy();
  });

  it('debe consultar y renderizar reporte', () => {
    const fixture = TestBed.createComponent(ReportesComponent);
    fixture.detectChanges();

    const comp = fixture.componentInstance;

    // set cuentaId = 1
    comp.form.patchValue({ cuentaId: 1 });

    // ejecuta consulta
    comp.consultar();

    const req = httpMock.expectOne('http://localhost:8080/reportes/cuentas/1');
    expect(req.request.method).toBe('GET');

    req.flush({
      cuentaId: 1,
      numeroCuenta: '00000001',
      saldoFinal: 150,
      movimientos: [
        { fecha: '2026-01-06T00:00:00', tipoMovimiento: 'CREDITO', valor: 50, saldo: 150 },
      ],
    });

    fixture.detectChanges();

    const el: HTMLElement = fixture.nativeElement;
    expect(el.textContent).toContain('00000001');
    expect(el.textContent).toContain('Saldo final');
    expect(el.textContent).toContain('CREDITO');
  });
});
