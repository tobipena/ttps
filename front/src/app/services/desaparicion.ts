import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs'


export interface Desaparicion {
  id?: number;
  comentario: string;
  coordenada: string;
  foto?: string; // Base64
  fecha: Date;
  mascota: {
    id: number;
    nombre: string;
  };
}
@Injectable({
  providedIn: 'root'
})
export class DesaparicionService {
  private apiUrl = 'http://localhost:8080/api/desapariciones';

  constructor(private http: HttpClient) {}

  crear(desaparicion: Desaparicion): Observable<Desaparicion> {
    return this.http.post<Desaparicion>(this.apiUrl, desaparicion);
  }

  obtenerPorUsuario(): Observable<Desaparicion[]> {
    return this.http.get<Desaparicion[]>(`${this.apiUrl}/usuario`);
  }
  obtenerTodas(): Observable<Desaparicion[]> {
    return this.http.get<Desaparicion[]>(`${this.apiUrl}`);
  }
  editar(id: number, desaparicion: Desaparicion): Observable<Desaparicion> {
    return this.http.put<Desaparicion>(`${this.apiUrl}/${id}`, desaparicion);
  }
  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
