import { Component, OnInit, PLATFORM_ID, Inject } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { AuthService, RegisterRequest } from '../../services/auth.service';
import { LocationMap, LocationData } from '../location-map/location-map';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, LocationMap],
  templateUrl: './register.html',
  styleUrl: './register.css'
})
export class Register implements OnInit {
  userData: RegisterRequest = {
    email: '',
    password: '',
    nombre: '',
    telefono: '',
    latitud: undefined,
    longitud: undefined
  };
  confirmPassword = '';
  errorMessage = '';
  isLoading = false;
  selectedLocation: { ciudad: string, provincia: string } | null = null;

  constructor(
    private readonly authService: AuthService,
    private readonly router: Router,
    @Inject(PLATFORM_ID) private readonly platformId: Object
  ) {}

  ngOnInit(): void {
  }

  onLocationSelected(location: LocationData): void {
    this.selectedLocation = { ciudad: location.ciudad, provincia: location.provincia };
    this.userData.latitud = location.lat;
    this.userData.longitud = location.lng;
  }

  onLocationCleared(): void {
    this.selectedLocation = null;
    this.userData.latitud = undefined;
    this.userData.longitud = undefined;
  }

  private validateForm(): boolean {
    // Validar campos obligatorios
    if (!this.userData.nombre || this.userData.nombre.trim() === '') {
      this.errorMessage = 'El nombre es obligatorio';
      return false;
    }

    if (!this.userData.email || this.userData.email.trim() === '') {
      this.errorMessage = 'El email es obligatorio';
      return false;
    }

    // Validar formato de email
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(this.userData.email)) {
      this.errorMessage = 'El email debe tener un formato válido (ejemplo: usuario@dominio.com)';
      return false;
    }

    if (!this.userData.telefono || this.userData.telefono.trim() === '') {
      this.errorMessage = 'El teléfono es obligatorio';
      return false;
    }

    if (!this.userData.password || this.userData.password.trim() === '') {
      this.errorMessage = 'La contraseña es obligatoria';
      return false;
    }

    // Validar longitud de contraseña
    if (this.userData.password.length < 6) {
      this.errorMessage = 'La contraseña debe tener al menos 6 caracteres';
      return false;
    }

    if (!this.confirmPassword || this.confirmPassword.trim() === '') {
      this.errorMessage = 'Debe confirmar la contraseña';
      return false;
    }

    // Validar que las contraseñas coincidan
    if (this.userData.password !== this.confirmPassword) {
      this.errorMessage = 'Las contraseñas no coinciden';
      return false;
    }

    // // Validar ubicación
    // if (!this.selectedCoordinates || this.userData.latitud === undefined || this.userData.longitud === undefined) {
    //   this.errorMessage = 'Debe seleccionar una ubicación en el mapa';
    //   return false;
    // }

    // Validar que la ubicación esté en Argentina
    if (!this.selectedLocation) {
      this.errorMessage = 'La ubicación seleccionada debe estar dentro de Argentina';
      return false;
    }

    return true;
  }

  onSubmit(): void {
    // Validar el formulario
    if (!this.validateForm()) {
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    this.authService.register(this.userData).subscribe({
      next: () => {
        this.router.navigate(['/']);
      },
      error: (error) => {
        this.isLoading = false;
        // Manejar diferentes formatos de respuesta de error
        if (typeof error.error === 'string') {
          this.errorMessage = error.error;
        } else if (error.error?.message) {
          this.errorMessage = error.error.message;
        } else {
          this.errorMessage = 'Error al registrarse. Intente nuevamente.';
        }
      },
      complete: () => {
        this.isLoading = false;
      }
    });
  }
}
