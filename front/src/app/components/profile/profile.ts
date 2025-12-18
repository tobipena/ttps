import { Component, OnInit, PLATFORM_ID, inject, ChangeDetectorRef } from '@angular/core';
import { isPlatformBrowser, CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { HttpClient } from '@angular/common/http';
import { EditableField } from './editable-field/editable-field';
import { LocationMap, LocationData } from '../location-map/location-map';

interface UserProfile {
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

interface Mascota {
  id: number;
  nombre: string;
  animal: string;
  color: string;
  descripcion: string;
  tamano: string;
  estado: string;
  imagenes?: Array<{
    id: number;
    datos: string;
    tipo: string;
  }>;
}

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, FormsModule, EditableField, LocationMap],
  templateUrl: './profile.html',
  styleUrl: './profile.css'
})
export class Profile implements OnInit {
  userProfile: UserProfile | null = null;
  mascotas: Mascota[] = [];
  editingField: string | null = null;
  tempValue: string = '';
  confirmPassword: string = '';
  isLoading = true;
  isLoadingMascotas = false;
  errorMessage = '';
  successMessage = '';
  showPassword = false;
  showConfirmPassword = false;
  showPasswordModal = false;
  showLocationModal = false;
  pendingPasswordUpdate: any = null;
  selectedCoordinates: { lat: number, lng: number } | null = null;
  selectedLocation: { ciudad: string, provincia: string } | null = null;
  isLoadingLocation = false;
  private map: any;
  private marker: any;

  private apiUrl = 'http://localhost:8080/ttps/usuarios';
  private mascotasUrl = 'http://localhost:8080/ttps/mascotas';
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
        this.cdr.markForCheck();
        this.cdr.detectChanges();
        setTimeout(() => this.cdr.detectChanges(), 0);

        this.loadMascotas(profile.id);
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

  loadMascotas(usuarioId: number): void {
    this.isLoadingMascotas = true;

    this.http.get<Mascota[]>(`${this.mascotasUrl}/usuario/${usuarioId}`).subscribe({
      next: (mascotas) => {
        this.mascotas = mascotas;
        this.isLoadingMascotas = false;
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error('Error al cargar mascotas:', error);
        this.isLoadingMascotas = false;
        this.cdr.detectChanges();
      }
    });
  }

  getPrimeraImagen(mascota: Mascota): string | null {
    if (mascota.imagenes && mascota.imagenes.length > 0) {
      return mascota.imagenes[0].datos;
    }
    return null;
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
    this.confirmPassword = '';
    this.showPassword = false;
    this.showConfirmPassword = false;
  }

  saveField(field: string): void {
    if (!this.userProfile) return;

    // Obtener el valor actual del campo
    const currentValue = this.userProfile[field as keyof UserProfile] as string;

    // Verificar si el valor cambió (excepto para contraseña que no tenemos el valor actual)
    if (field !== 'password' && this.tempValue === currentValue) {
      // No hubo cambios, cancelar la edición sin mostrar mensaje
      this.cancelEdit();
      return;
    }

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

    // Validaciones específicas para contraseña
    if (field === 'password') {
      if (this.tempValue.length < 6) {
        this.errorMessage = 'La contraseña debe tener al menos 6 caracteres';
        return;
      }
      if (this.tempValue !== this.confirmPassword) {
        this.errorMessage = 'Las contraseñas no coinciden';
        return;
      }
      // Mostrar modal de confirmación para cambio de contraseña
      this.showPasswordConfirmationModal(field);
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
        this.confirmPassword = '';
        this.showPassword = false;
        this.showConfirmPassword = false;
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

  toggleConfirmPasswordVisibility(): void {
    this.showConfirmPassword = !this.showConfirmPassword;
  }

  showPasswordConfirmationModal(field: string): void {
    this.pendingPasswordUpdate = field;
    this.showPasswordModal = true;
  }

  confirmPasswordChange(): void {
    this.showPasswordModal = false;
    if (this.pendingPasswordUpdate) {
      this.executePasswordUpdate(this.pendingPasswordUpdate);
      this.pendingPasswordUpdate = null;
    }
  }

  cancelPasswordChange(): void {
    this.showPasswordModal = false;
    this.pendingPasswordUpdate = null;
  }

  private executePasswordUpdate(field: string): void {
    if (!this.userProfile) return;

    const updateData: any = {
      nombre: this.userProfile.nombre,
      email: this.userProfile.email,
      telefono: this.userProfile.telefono,
      ciudad: this.userProfile.ciudad,
      provincia: this.userProfile.provincia,
      password: this.tempValue
    };

    this.http.put<UserProfile>(`${this.apiUrl}/me`, updateData).subscribe({
      next: (updatedProfile) => {
        this.userProfile = updatedProfile;
        this.successMessage = 'Contraseña actualizada correctamente';
        this.editingField = null;
        this.tempValue = '';
        this.confirmPassword = '';
        this.showPassword = false;
        this.showConfirmPassword = false;
        this.errorMessage = '';

        // Forzar detección de cambios para mostrar el mensaje inmediatamente
        this.cdr.markForCheck();
        this.cdr.detectChanges();

        setTimeout(() => {
          this.successMessage = '';
          this.cdr.detectChanges();
        }, 3000);
      },
      error: (error) => {
        if (typeof error.error === 'string') {
          this.errorMessage = error.error;
        } else if (error.error?.message) {
          this.errorMessage = error.error.message;
        } else {
          this.errorMessage = 'Error al actualizar la contraseña';
        }

        this.cdr.markForCheck();
        this.cdr.detectChanges();

        setTimeout(() => {
          this.errorMessage = '';
          this.cdr.detectChanges();
        }, 5000);
      }
    });
  }

  ngAfterViewInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      this.loadLeaflet();
    }
  }

  private loadLeaflet(): void {
    if (typeof (window as any).L === 'undefined') {
      const leafletCss = document.createElement('link');
      leafletCss.rel = 'stylesheet';
      leafletCss.href = 'https://unpkg.com/leaflet@1.9.4/dist/leaflet.css';
      document.head.appendChild(leafletCss);

      const leafletScript = document.createElement('script');
      leafletScript.src = 'https://unpkg.com/leaflet@1.9.4/dist/leaflet.js';
      document.head.appendChild(leafletScript);
    }
  }

  openLocationModal(): void {
    this.showLocationModal = true;
    this.errorMessage = '';
    this.selectedLocation = null;
  }

  closeLocationModal(): void {
    this.showLocationModal = false;
    this.selectedLocation = null;
  }

  onLocationSelected(location: LocationData): void {
    this.selectedLocation = { ciudad: location.ciudad, provincia: location.provincia };
    this.selectedCoordinates = { lat: location.lat, lng: location.lng };
  }

  onLocationCleared(): void {
    this.errorMessage = 'La ubicación seleccionada debe estar dentro de Argentina';
    this.selectedLocation = null;
    this.selectedCoordinates = null;
  }

  saveLocation(): void {
    if (!this.selectedCoordinates || !this.selectedLocation || !this.userProfile) {
      this.errorMessage = 'Por favor, seleccione una ubicación válida en el mapa';
      return;
    }

    const updateData: any = {
      nombre: this.userProfile.nombre,
      email: this.userProfile.email,
      telefono: this.userProfile.telefono,
      ciudad: this.selectedLocation.ciudad,
      provincia: this.selectedLocation.provincia,
      password: this.userProfile.password || '',
      latitud: this.selectedCoordinates.lat,
      longitud: this.selectedCoordinates.lng
    };

    this.http.put<UserProfile>(`${this.apiUrl}/me`, updateData).subscribe({
      next: (updatedProfile) => {
        this.userProfile = updatedProfile;
        this.successMessage = 'Ubicación actualizada correctamente';
        this.closeLocationModal();
        
        this.cdr.markForCheck();
        this.cdr.detectChanges();

        setTimeout(() => {
          this.successMessage = '';
          this.cdr.detectChanges();
        }, 3000);
      },
      error: (error) => {
        if (typeof error.error === 'string') {
          this.errorMessage = error.error;
        } else if (error.error?.message) {
          this.errorMessage = error.error.message;
        } else {
          this.errorMessage = 'Error al actualizar la ubicación';
        }

        this.cdr.markForCheck();
        this.cdr.detectChanges();

        setTimeout(() => {
          this.errorMessage = '';
          this.cdr.detectChanges();
        }, 5000);
      }
    });
  }

  getLocationDisplay(): string {
    if (this.userProfile?.ciudad && this.userProfile?.provincia) {
      return `${this.userProfile.ciudad}, ${this.userProfile.provincia}`;
    }
    return 'No especificada';
  }
}

