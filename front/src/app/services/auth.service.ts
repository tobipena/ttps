import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { PlatformService } from './platform.service';

export interface Usuario {
  nombre: string;
  email?: string;
  telefono?: string;
}

export interface AuthResponse {
  token: string;
  expiresIn: number;
  nombre: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  email: string;
  password: string;
  nombre: string;
  telefono: string;
  latitud?: number;
  longitud?: number;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/ttps/auth';
  private currentUserSignal = signal<Usuario | null>(null);
  
  currentUser = this.currentUserSignal.asReadonly();

  constructor(
    private readonly http: HttpClient,
    private readonly platformService: PlatformService
  ) {
    this.loadUserFromStorage();
  }

  private loadUserFromStorage(): void {
    const userStr = this.platformService.getLocalStorage('currentUser');
    if (userStr) {
      this.currentUserSignal.set(JSON.parse(userStr));
    }
  }

  login(credentials: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, credentials).pipe(
      tap(response => {
        this.saveAuthData(response);
      })
    );
  }

  register(userData: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/register`, userData).pipe(
      tap(response => {
        this.saveAuthData(response);
      })
    );
  }

  private saveAuthData(response: AuthResponse): void {
    const user: Usuario = {
      nombre: response.nombre
    };
    
    this.currentUserSignal.set(user);
    
    // Calcular timestamp de expiración (expiresIn está en segundos)
    const expiresAt = Date.now() + (response.expiresIn * 1000);
    
    this.platformService.setLocalStorage('currentUser', JSON.stringify(user));
    this.platformService.setLocalStorage('authData', JSON.stringify({
      token: response.token,
      expiresIn: response.expiresIn,
      expiresAt: expiresAt
    }));
  }

  logout(): void {
    this.currentUserSignal.set(null);
    this.platformService.removeLocalStorage('currentUser');
    this.platformService.removeLocalStorage('authData');
  }

  updateCurrentUser(nombre: string): void {
    const currentUser = this.currentUserSignal();
    if (currentUser) {
      const updatedUser: Usuario = {
        ...currentUser,
        nombre: nombre
      };
      this.currentUserSignal.set(updatedUser);
      this.platformService.setLocalStorage('currentUser', JSON.stringify(updatedUser));
    }
  }

  isAuthenticated(): boolean {
    // Verificar tanto en el signal como en localStorage
    const hasSignal = this.currentUser() !== null;
    const hasLocalStorage = this.platformService.getLocalStorage('currentUser') !== null && 
                           this.platformService.getLocalStorage('authData') !== null;
    
    // Si hay datos en localStorage pero no en signal, cargarlos
    if (!hasSignal && hasLocalStorage) {
      this.loadUserFromStorage();
    }
    
    return hasSignal || hasLocalStorage;
  }

  getToken(): string | null {
    const authData = this.platformService.getLocalStorage('authData');
    if (authData) {
      return JSON.parse(authData).token;
    }
    return null;
  }

  isTokenExpired(): boolean {
    const authData = this.platformService.getLocalStorage('authData');
    if (!authData) {
      return true;
    }
    
    const { expiresAt } = JSON.parse(authData);
    return Date.now() >= expiresAt;
  }
}
