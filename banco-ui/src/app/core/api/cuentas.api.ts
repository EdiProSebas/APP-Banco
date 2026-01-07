import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export type TipoCuenta = 'AHORROS' | 'CORRIENTE';

export interface Cuenta {
  cuentaId: number;
  numeroCuenta: string;
  tipoCuenta: TipoCuenta;
  saldoInicial: number;
  estado: boolean;
  clientePk: number;
  clienteId: string;
}

export interface CuentaCreateRequest {
  numeroCuenta: string;
  tipoCuenta: TipoCuenta;
  saldoInicial: number;
  estado: boolean;
  clientePk: number;
}

export interface CuentaUpdateRequest {
  tipoCuenta: TipoCuenta;
  saldoInicial: number;
  estado: boolean;
}

@Injectable({ providedIn: 'root' })
export class CuentasApi {
  private readonly baseUrl = 'http://localhost:8080/cuentas';

  constructor(private http: HttpClient) {}

  listar(clientePk?: number): Observable<Cuenta[]> {
    if (clientePk != null) {
      return this.http.get<Cuenta[]>(`${this.baseUrl}?clientePk=${clientePk}`);
    }
    return this.http.get<Cuenta[]>(this.baseUrl);
  }

  obtener(id: number): Observable<Cuenta> {
    return this.http.get<Cuenta>(`${this.baseUrl}/${id}`);
  }

  crear(payload: CuentaCreateRequest): Observable<Cuenta> {
    return this.http.post<Cuenta>(this.baseUrl, payload);
  }

  actualizar(id: number, payload: CuentaUpdateRequest): Observable<Cuenta> {
    return this.http.put<Cuenta>(`${this.baseUrl}/${id}`, payload);
  }

  patch(id: number, payload: Partial<CuentaUpdateRequest>): Observable<Cuenta> {
    return this.http.patch<Cuenta>(`${this.baseUrl}/${id}`, payload);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
