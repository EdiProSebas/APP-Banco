import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export type Genero = 'MASCULINO' | 'FEMENINO' | 'OTRO';

export interface Cliente {
  id: number;
  clienteId: string;
  estado: boolean;
  nombre: string;
  genero: Genero;
  edad: number;
  identificacion: string;
  direccion: string;
  telefono: string;
}

export interface ClienteCreateRequest {
  clienteId: string;
  contrasena: string;
  estado: boolean;
  nombre: string;
  genero: Genero;
  edad: number;
  identificacion: string;
  direccion: string;
  telefono: string;
}

export interface ClienteUpdateRequest {
  estado: boolean;
  nombre: string;
  genero: Genero;
  edad: number;
  direccion: string;
  telefono: string;
}

@Injectable({ providedIn: 'root' })
export class ClientesApi {
  private readonly baseUrl = 'http://localhost:8080/clientes';

  constructor(private http: HttpClient) {}

  listar(): Observable<Cliente[]> {
    return this.http.get<Cliente[]>(this.baseUrl);
  }

  obtener(id: number): Observable<Cliente> {
    return this.http.get<Cliente>(`${this.baseUrl}/${id}`);
  }

  crear(payload: ClienteCreateRequest): Observable<Cliente> {
    return this.http.post<Cliente>(this.baseUrl, payload);
  }

  actualizar(id: number, payload: ClienteUpdateRequest): Observable<Cliente> {
    return this.http.put<Cliente>(`${this.baseUrl}/${id}`, payload);
  }

  patch(id: number, payload: Partial<ClienteUpdateRequest>): Observable<Cliente> {
    return this.http.patch<Cliente>(`${this.baseUrl}/${id}`, payload);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
