import { Component, OnInit, PLATFORM_ID, inject, AfterViewInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { isPlatformBrowser, CommonModule } from '@angular/common';

// Declarar Leaflet globalmente
declare var L: any;

interface Imagen {
  id: number;
  datos: string; // Base64
  tipo: string;
}

interface Mascota {
  id: number;
  nombre: string;
  tamano: string;
  color: string;
  descripcion: string;
  animal: string;
  estado: string;
  imagenes?: Imagen[];
}

interface Desaparicion {
  id: number;
  comentario: string;
  coordenada: string;
  fecha: string;
  mascota: Mascota;
}

@Component({
  selector: 'app-desaparicion-detalle',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './desaparicion-detalle.html',
  styleUrl: './desaparicion-detalle.css'
})
export class DesaparicionDetalle implements OnInit, AfterViewInit {
  desaparicion: Desaparicion | null = null;
  loading = true;
  error = false;
  private map: any = null;
  private mapInitialized = false;

  private apiUrl = 'http://localhost:8080/ttps/desapariciones';
  private platformId = inject(PLATFORM_ID);

  constructor(
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly http: HttpClient
  ) {}

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.loadDesaparicion(+id);
    } else {
      this.error = true;
      this.loading = false;
    }
  }

  ngAfterViewInit() {
    if (this.desaparicion && !this.mapInitialized && isPlatformBrowser(this.platformId)) {
      const coords = this.getCoordenadas(this.desaparicion.coordenada);
      if (coords) {
        setTimeout(() => this.initMap(coords!), 300);
      }
    }
  }

  loadDesaparicion(id: number) {
    this.loading = true;
    this.http.get<Desaparicion>(`${this.apiUrl}/${id}`).subscribe({
      next: (data) => {
        this.desaparicion = data;
        this.loading = false;

        // Inicializar mapa si ya está en el DOM
        if (isPlatformBrowser(this.platformId)) {
          const coords = this.getCoordenadas(data.coordenada);
          if (coords) {
            setTimeout(() => this.initMap(coords!), 300);
          }
        }
      },
      error: (error) => {
        console.error('Error al cargar desaparición:', error);
        this.error = true;
        this.loading = false;
      }
    });
  }

  private initMap(coords: { lat: number, lng: number }) {
    if (this.mapInitialized || !isPlatformBrowser(this.platformId)) return;

    const mapElement = document.getElementById('map');
    if (!mapElement) return;

    try {
      this.map = L.map('map').setView([coords.lat, coords.lng], 15);

      L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '© OpenStreetMap contributors',
        maxZoom: 19
      }).addTo(this.map);

      L.marker([coords.lat, coords.lng])
        .addTo(this.map)
        .bindPopup('Última ubicación vista')
        .openPopup();

      this.mapInitialized = true;
    } catch (e) {
      console.error('Error al inicializar mapa:', e);
    }
  }

  getCoordenadas(coordenada: string): { lat: number, lng: number } | null {
    try {
      const coords = coordenada.split(',').map(c => parseFloat(c.trim()));
      if (coords.length === 2 && !isNaN(coords[0]) && !isNaN(coords[1])) {
        return { lat: coords[0], lng: coords[1] };
      }
    } catch (e) {
      console.error('Error al parsear coordenadas:', e);
    }
    return null;
  }

  formatearFecha(fecha: string): string {
    const date = new Date(fecha);
    return date.toLocaleDateString('es-AR', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  }

  getPrimeraImagen(mascota: Mascota): string | null {
    if (mascota.imagenes && mascota.imagenes.length > 0) {
      return mascota.imagenes[0].datos;
    }
    return null;
  }

  volver() {
    this.router.navigate(['/']);
  }
}

