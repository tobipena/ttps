import { Component, OnInit, PLATFORM_ID, inject, ChangeDetectorRef } from '@angular/core';
import { isPlatformBrowser, CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { HttpClient } from '@angular/common/http';

interface UserProfile {
  id: number;
  nombre: string;
  email: string;
  telefono: string;
  ciudad: string;
  provincia: string;
  puntos: number;
  password?: string;
}

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './profile.html',
  styleUrl: './profile.css'
})
export class Profile implements OnInit {
  userProfile: UserProfile | null = null;
  editingField: string | null = null;
  tempValue: string = '';
  isLoading = true;
  errorMessage = '';
  successMessage = '';
  showPassword = false;

  private apiUrl = 'http://localhost:8080/ttps/usuarios';
  private platformId = inject(PLATFORM_ID);
  private cdr = inject(ChangeDetectorRef);

  constructor(
    private readonly http: HttpClient,
    private readonly authService: AuthService,
    private readonly router: Router
  ) {}

  ngOnInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      // Forzar que Angular detecte que el componente está listo
      Promise.resolve().then(() => {
        this.loadUserProfile();
      });
    }
  }

  loadUserProfile(): void {
    this.isLoading = true;
    
    this.http.get<UserProfile>(`${this.apiUrl}/me`).subscribe({
      next: (profile) => {
        this.userProfile = profile;
        this.isLoading = false;
        // Forzar múltiples ciclos de detección
        this.cdr.markForCheck();
        this.cdr.detectChanges();
        setTimeout(() => this.cdr.detectChanges(), 0);
      },
      error: (error) => {
        this.isLoading = false;
        this.errorMessage = 'Error al cargar el perfil';
        this.cdr.markForCheck();
        this.cdr.detectChanges();
        console.error('Error loading profile:', error);
      }
    });
  }

  startEdit(field: string): void {
    if (field === 'puntos') return; // Los puntos no se pueden editar
    
    this.editingField = field;
    this.tempValue = this.userProfile ? (this.userProfile[field as keyof UserProfile] as string) : '';
    this.errorMessage = '';
    this.successMessage = '';
  }

  cancelEdit(): void {
    this.editingField = null;
    this.tempValue = '';
    this.showPassword = false;
  }

  saveField(field: string): void {
    if (!this.userProfile) return;

    // Validaciones
    if (!this.tempValue || this.tempValue.trim() === '') {
      this.errorMessage = 'El campo no puede estar vacío';
      return;
    }

    if (field === 'email') {
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      if (!emailRegex.test(this.tempValue)) {
        this.errorMessage = 'El email debe tener un formato válido';
        return;
      }
    }

    if (field === 'password' && this.tempValue.length < 6) {
      this.errorMessage = 'La contraseña debe tener al menos 6 caracteres';
      return;
    }

    // Crear un DTO con todos los campos actuales más el campo editado
    const updateData: any = {
      nombre: this.userProfile.nombre,
      email: this.userProfile.email,
      telefono: this.userProfile.telefono,
      ciudad: this.userProfile.ciudad,
      provincia: this.userProfile.provincia,
      password: field === 'password' ? this.tempValue : this.userProfile.password || ''
    };

    // Actualizar el campo específico que se está editando
    updateData[field] = this.tempValue;

    this.http.put<UserProfile>(`${this.apiUrl}/me`, updateData).subscribe({
      next: (updatedProfile) => {
        this.userProfile = updatedProfile;
        this.successMessage = `${this.getFieldLabel(field)} actualizado correctamente`;
        this.editingField = null;
        this.tempValue = '';
        this.showPassword = false;
        this.errorMessage = '';
        
        // Si se actualizó el nombre, actualizar también en el AuthService para que se refleje en el layout
        if (field === 'nombre') {
          this.authService.updateCurrentUser(updatedProfile.nombre);
        }
        
        // Limpiar mensaje de éxito después de 3 segundos
        setTimeout(() => {
          this.successMessage = '';
        }, 3000);
      },
      error: (error) => {
        // Si el backend devuelve un string en error.error, usarlo directamente
        if (typeof error.error === 'string') {
          this.errorMessage = error.error;
        } else if (error.error?.message) {
          this.errorMessage = error.error.message;
        } else {
          this.errorMessage = 'Error al actualizar el campo';
        }
        
        // Forzar detección de cambios para mostrar el error inmediatamente
        this.cdr.markForCheck();
        this.cdr.detectChanges();
        
        // Limpiar mensaje de error después de 5 segundos
        setTimeout(() => {
          this.errorMessage = '';
          this.cdr.detectChanges();
        }, 5000);
      }
    });
  }

  getFieldLabel(field: string): string {
    const labels: { [key: string]: string } = {
      nombre: 'Nombre',
      email: 'Email',
      telefono: 'Teléfono',
      ciudad: 'Ciudad',
      provincia: 'Provincia',
      password: 'Contraseña'
    };
    return labels[field] || field;
  }

  togglePasswordVisibility(): void {
    this.showPassword = !this.showPassword;
  }
}
