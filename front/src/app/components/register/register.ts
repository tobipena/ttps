import { Component, OnInit, PLATFORM_ID, Inject, AfterViewInit } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService, RegisterRequest } from '../../services/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './register.html',
  styleUrl: './register.css'
})
export class Register implements OnInit, AfterViewInit {
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
  selectedCoordinates: { lat: number, lng: number } | null = null;
  
  private map: any;
  private marker: any;

  constructor(
    private readonly authService: AuthService,
    private readonly router: Router,
    @Inject(PLATFORM_ID) private readonly platformId: Object
  ) {}

  ngOnInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      this.loadLeaflet();
    }
  }

  ngAfterViewInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      setTimeout(() => this.initMap(), 500);
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
      leafletScript.onload = () => {
        setTimeout(() => this.initMap(), 100);
      };
      document.head.appendChild(leafletScript);
    }
  }

  private initMap(): void {
    if (typeof (window as any).L !== 'undefined' && !this.map) {
      const L = (window as any).L;
      // Centro de Argentina aproximado
      this.map = L.map('map').setView([-38.4161, -63.6167], 4);

      L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '© OpenStreetMap contributors'
      }).addTo(this.map);

      // Manejar clicks en el mapa
      this.map.on('click', (e: any) => {
        this.onMapClick(e);
      });
    }
  }

  private onMapClick(e: any): void {
    const lat = e.latlng.lat;
    const lng = e.latlng.lng;

    // Verificar que esté dentro de Argentina (aproximado)
    if (lat < -55 || lat > -21 || lng < -73 || lng > -53) {
      this.errorMessage = 'Por favor, seleccione una ubicación dentro de Argentina';
      return;
    }

    this.selectedCoordinates = { lat, lng };
    this.userData.latitud = lat;
    this.userData.longitud = lng;
    this.errorMessage = '';

    // Remover marcador anterior si existe
    if (this.marker) {
      this.map.removeLayer(this.marker);
    }

    // Agregar nuevo marcador
    const L = (window as any).L;
    this.marker = L.marker([lat, lng]).addTo(this.map);
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

    // Validar ubicación
    if (!this.selectedCoordinates || this.userData.latitud === undefined || this.userData.longitud === undefined) {
      this.errorMessage = 'Debe seleccionar una ubicación en el mapa';
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
