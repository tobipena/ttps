import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Imagen {
  id: number;
  datos: string;
  tipo: string;
}

export interface Mascota {
  id: number;
  nombre: string;
  animal: string;
  color: string;
  descripcion: string;
  tamano: string;
  estado: string;
  desaparicionId?: number;
  imagenes?: Imagen[];
}

@Injectable({
  providedIn: 'root'
})
export class MascotaService {
  private apiUrl = 'http://localhost:8080/ttps/mascotas';

  constructor(private http: HttpClient) {}

  // Obtener mascotas por usuario
  obtenerPorUsuario(usuarioId: number): Observable<Mascota[]> {
    return this.http.get<Mascota[]>(`${this.apiUrl}/usuario/${usuarioId}`);
  }

  // Obtener mascotas perdidas
  obtenerPerdidas(): Observable<Mascota[]> {
    return this.http.get<Mascota[]>(`${this.apiUrl}/perdidas`);
  }

  // Actualizar mascota
  actualizar(id: number, mascota: Partial<Mascota>): Observable<Mascota> {
    return this.http.put<Mascota>(`${this.apiUrl}/${id}`, mascota);
  }
}

