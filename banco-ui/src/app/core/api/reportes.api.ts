import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ReporteMovimientoItem {
  fecha: string;
  tipoMovimiento: 'DEBITO' | 'CREDITO';
  valor: number;
  saldo: number;
}

export interface ReporteResponse {
  cuentaId: number;
  numeroCuenta: string;
  saldoFinal: number;
  movimientos: ReporteMovimientoItem[];
}

@Injectable({ providedIn: 'root' })
export class ReportesApi {
  private readonly baseUrl = 'http://localhost:8080/reportes';

  constructor(private http: HttpClient) {}

  obtenerPorCuenta(cuentaId: number): Observable<ReporteResponse> {
    return this.http.get<ReporteResponse>(`${this.baseUrl}/cuentas/${cuentaId}`);
  }

  descargarPdfPorCuenta(cuentaId: number): Observable<Blob> {
    return this.http.get(`${this.baseUrl}/cuentas/${cuentaId}/pdf`, {
      responseType: 'blob',
    });
  }
}
