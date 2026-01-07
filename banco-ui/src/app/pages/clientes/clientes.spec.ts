import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';

import { ClientesComponent } from './ClientesComponent';

describe('Clientes', () => {
  let httpMock: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ClientesComponent],
      providers: [provideHttpClient(), provideHttpClientTesting()],
    }).compileComponents();

    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => { 
    httpMock.verify();
  });

  it('debe crear el componente', () => {
    const fixture = TestBed.createComponent(ClientesComponent);

    // El componente dispara HTTP en ngOnInit al detectar cambios
    fixture.detectChanges();

    const req = httpMock.expectOne('http://localhost:8080/clientes');
    expect(req.request.method).toBe('GET');
    req.flush([]); // responde vacío

    expect(fixture.componentInstance).toBeTruthy();
  });

  it('debe renderizar filas cuando el API devuelve clientes', () => {
    const fixture = TestBed.createComponent(ClientesComponent);
    fixture.detectChanges();

    const req = httpMock.expectOne('http://localhost:8080/clientes');
    expect(req.request.method).toBe('GET');

    req.flush([
      {
        id: 1,
        clienteId: 'CLI-0001',
        estado: true,
        nombre: 'Juan Perez',
        genero: 'MASCULINO',
        edad: 30,
        identificacion: '1712345678',
        direccion: 'Av. Siempre Viva 123',
        telefono: '0999999999',
      },
    ]);

    // fuerza render tras recibir datos
    fixture.detectChanges();

    const el: HTMLElement = fixture.nativeElement;

    // Validamos que aparezca el cliente en el HTML
    expect(el.textContent).toContain('Nombre');
    expect(el.textContent).toContain('Juan Perez');
  });
});
