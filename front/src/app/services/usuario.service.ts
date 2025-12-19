import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Usuario {
  id: number;
  nombre: string;
  email: string;
  telefono: string;
  ciudad: string;
  provincia: string;
  puntos: number;
  password?: string;
  latitud?: number;
  longitud?: number;
}

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {
  private apiUrl = 'http://localhost:8080/ttps/usuarios';

  constructor(private http: HttpClient) {}

  // Obtener perfil del usuario actual
  obtenerPerfil(): Observable<Usuario> {
    return this.http.get<Usuario>(`${this.apiUrl}/me`);
  }

  // Actualizar perfil del usuario actual
  actualizarPerfil(usuario: Partial<Usuario>): Observable<Usuario> {
    return this.http.put<Usuario>(`${this.apiUrl}/me`, usuario);
  }
}

