import { Component, OnInit, PLATFORM_ID, inject, AfterViewInit, ChangeDetectorRef } from '@angular/core';
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
  latitud: number;
  longitud: number;
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

  // Carrusel de imágenes
  currentImageIndex = 0;

  // Dirección y mapa
  direccion: string = '';
  loadingMapa = true;
  mapaListo = false;

  private apiUrl = 'http://localhost:8080/ttps/desapariciones';
  private platformId = inject(PLATFORM_ID);
  private cdr = inject(ChangeDetectorRef);

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
    // El mapa se inicializa en loadDesaparicion cuando los datos están listos
  }

  loadDesaparicion(id: number) {
    this.loading = true;
    this.http.get<Desaparicion>(`${this.apiUrl}/${id}`).subscribe({
      next: (data) => {
        this.desaparicion = data;
        this.loading = false;

        // FORZAR detección de cambios para que Angular renderice inmediatamente
        this.cdr.detectChanges();

        // Estas llamadas se hacen en paralelo DESPUÉS de mostrar la página
        if (data.latitud && data.longitud) {
          this.obtenerDireccion(data.latitud, data.longitud);
        }

        // Inicializar mapa con reintento hasta que el elemento esté disponible
        if (isPlatformBrowser(this.platformId) && data.latitud && data.longitud) {
          setTimeout(() => {
            this.waitForMapElement({ lat: data.latitud, lng: data.longitud });
          }, 0);
        }
      },
      error: (error) => {
        console.error('Error al cargar desaparición:', error);
        this.error = true;
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  private waitForMapElement(coords: { lat: number, lng: number }, retries: number = 0) {
    const maxRetries = 20; // Aumentar intentos pero reducir delay
    const mapElement = document.getElementById('map');

    if (mapElement) {
      console.log('Elemento del mapa encontrado, inicializando...');
      this.initMap(coords);
    } else if (retries < maxRetries) {
      // Reducir delay a 100ms para que sea más rápido
      setTimeout(() => this.waitForMapElement(coords, retries + 1), 100);
    } else {
      console.error('No se pudo encontrar el elemento del mapa después de varios intentos');
      // Marcar como listo aunque falle para no dejar cargando indefinidamente
      this.mapaListo = true;
      this.checkMapaCompleto();
    }
  }

  obtenerDireccion(lat: number, lng: number) {
    // Usar Nominatim (OpenStreetMap) para geocoding inverso
    const url = `https://nominatim.openstreetmap.org/reverse?format=json&lat=${lat}&lon=${lng}&accept-language=es`;

    this.http.get<any>(url).subscribe({
      next: (response) => {
        if (response && response.display_name) {
          this.direccion = response.display_name;
        } else if (response && response.address) {
          // Construir dirección más legible
          const addr = response.address;
          const partes = [
            addr.road,
            addr.house_number,
            addr.suburb || addr.neighbourhood,
            addr.city || addr.town || addr.village,
            addr.state,
            addr.country
          ].filter(Boolean);
          this.direccion = partes.join(', ');
        } else {
          this.direccion = `${lat.toFixed(6)}, ${lng.toFixed(6)}`;
        }
        this.checkMapaCompleto();
      },
      error: (error) => {
        console.error('Error al obtener dirección:', error);
        this.direccion = `${lat.toFixed(6)}, ${lng.toFixed(6)}`;
        this.checkMapaCompleto();
      }
    });
  }

  private checkMapaCompleto() {
    // Solo mostrar mapa cuando dirección esté lista
    if (this.direccion && this.mapaListo) {
      this.loadingMapa = false;
      this.cdr.detectChanges();
    }
  }

  private initMap(coords: { lat: number, lng: number }) {
    if (this.mapInitialized || !isPlatformBrowser(this.platformId)) {
      console.log('Mapa ya inicializado o no es browser');
      return;
    }

    const mapElement = document.getElementById('map');
    if (!mapElement) {
      console.error('Elemento del mapa no encontrado en el DOM');
      return;
    }

    try {
      console.log('Inicializando mapa con coordenadas:', coords);

      // Zoom 13 para mostrar más contexto de la zona
      this.map = L.map('map').setView([coords.lat, coords.lng], 13);

      L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '© OpenStreetMap contributors',
        maxZoom: 19
      }).addTo(this.map);

      L.marker([coords.lat, coords.lng])
        .addTo(this.map)
        .bindPopup('Última ubicación vista')
        .openPopup();

      this.mapInitialized = true;
      this.mapaListo = true;
      this.checkMapaCompleto();
      console.log('Mapa inicializado exitosamente');
    } catch (e) {
      console.error('Error al inicializar mapa:', e);
      this.mapaListo = true;
      this.checkMapaCompleto();
    }
  }


  formatearFecha(fecha: string): string {
    const date = new Date(fecha);
    return date.toLocaleDateString('es-AR', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
    });
  }


  getImagenActual(): string | null {
    if (this.desaparicion?.mascota.imagenes && this.desaparicion.mascota.imagenes.length > 0) {
      return this.desaparicion.mascota.imagenes[this.currentImageIndex].datos;
    }
    return null;
  }

  getTotalImagenes(): number {
    return this.desaparicion?.mascota.imagenes?.length || 0;
  }

  nextImage() {
    if (this.desaparicion?.mascota.imagenes) {
      this.currentImageIndex = (this.currentImageIndex + 1) % this.desaparicion.mascota.imagenes.length;
    }
  }

  prevImage() {
    if (this.desaparicion?.mascota.imagenes) {
      this.currentImageIndex = this.currentImageIndex === 0
        ? this.desaparicion.mascota.imagenes.length - 1
        : this.currentImageIndex - 1;
    }
  }

  volver() {
    this.router.navigate(['/']);
  }
}

