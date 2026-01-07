import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export type TipoMovimiento = 'DEBITO' | 'CREDITO';

export interface Movimiento {
  movimientoId: number;
  fecha: string; // ISO string
  tipoMovimiento: TipoMovimiento;
  valor: number;
  saldo: number;
  cuentaId: number;
  numeroCuenta: string;
}

export interface MovimientoCreateRequest {
  cuentaId: number;
  // fecha opcional: si no se envía, backend usa now()
  fecha?: string;
  tipoMovimiento: TipoMovimiento;
  valor: number;
}

@Injectable({ providedIn: 'root' })
export class MovimientosApi {
  private readonly baseUrl = 'http://localhost:8080/movimientos';

  constructor(private http: HttpClient) {}

  listar(cuentaId?: number): Observable<Movimiento[]> {
    if (cuentaId != null) {
      return this.http.get<Movimiento[]>(`${this.baseUrl}?cuentaId=${cuentaId}`);
    }
    return this.http.get<Movimiento[]>(this.baseUrl);
  }

  obtener(id: number): Observable<Movimiento> {
    return this.http.get<Movimiento>(`${this.baseUrl}/${id}`);
  }

  crear(payload: MovimientoCreateRequest): Observable<Movimiento> {
    return this.http.post<Movimiento>(this.baseUrl, payload);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
