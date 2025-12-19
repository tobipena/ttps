import { Component, OnInit, AfterViewInit, PLATFORM_ID, Inject, Input, Output, EventEmitter, ChangeDetectorRef } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { HttpClient } from '@angular/common/http';

export interface LocationData {
  lat: number;
  lng: number;
  ciudad: string;
  provincia: string;
}

@Component({
  selector: 'app-location-map',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './location-map.html',
  styleUrl: './location-map.css'
})
export class LocationMap implements OnInit, AfterViewInit {
  @Input() initialLat?: number;
  @Input() initialLng?: number;
  @Output() locationSelected = new EventEmitter<LocationData>();
  @Output() locationCleared = new EventEmitter<void>();
  
  isLoadingLocation = false;
  selectedCoordinates: { lat: number, lng: number } | null = null;
  selectedLocation: { ciudad: string, provincia: string } | null = null;
  
  private map: any;
  private marker: any;

  constructor(
    private readonly http: HttpClient,
    private readonly cdr: ChangeDetectorRef,
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
      
      // Si hay coordenadas iniciales, centrarse ahí, sino centro de Argentina
      const centerLat = this.initialLat || -38.4161;
      const centerLng = this.initialLng || -63.6167;
      const zoom = (this.initialLat && this.initialLng) ? 13 : 4;
      
      this.map = L.map('location-map').setView([centerLat, centerLng], zoom);

      L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '© OpenStreetMap contributors'
      }).addTo(this.map);

      // Si hay coordenadas iniciales, agregar marcador y obtener ubicación
      if (this.initialLat && this.initialLng) {
        this.marker = L.marker([this.initialLat, this.initialLng]).addTo(this.map);
        this.selectedCoordinates = { lat: this.initialLat, lng: this.initialLng };
        this.getLocationFromApi(this.initialLat, this.initialLng);
      }

      // Manejar clicks en el mapa
      this.map.on('click', (e: any) => {
        this.onMapClick(e);
      });
    }
  }

  private onMapClick(e: any): void {
    const lat = e.latlng.lat;
    const lng = e.latlng.lng;

    this.selectedCoordinates = { lat, lng };
    this.selectedLocation = null;

    // Remover marcador anterior si existe
    if (this.marker) {
      this.map.removeLayer(this.marker);
    }

    // Agregar nuevo marcador
    const L = (window as any).L;
    this.marker = L.marker([lat, lng]).addTo(this.map);
    
    this.getLocationFromApi(lat, lng);
  }

  private getLocationFromApi(lat: number, lng: number): void {
    this.isLoadingLocation = true;
    const url = `https://apis.datos.gob.ar/georef/api/ubicacion?lat=${lat}&lon=${lng}`;
    
    this.http.get<any>(url).subscribe({
      next: (response) => {
        this.isLoadingLocation = false;
        
        if (response?.ubicacion) {
          const ciudad = response.ubicacion.municipio?.nombre || 
                        response.ubicacion.departamento?.nombre || 
                        null;
          const provincia = response.ubicacion.provincia?.nombre || null;
          
          if (ciudad && provincia) {
            this.selectedLocation = { ciudad, provincia };
            this.locationSelected.emit({
              lat,
              lng,
              ciudad,
              provincia
            });
          } else {
            // La API respondió pero sin datos válidos (fuera de Argentina)
            this.selectedLocation = null;
            this.locationCleared.emit();
          }
        } else {
          // Respuesta sin datos
          this.selectedLocation = null;
          this.locationCleared.emit();
        }
        this.cdr.detectChanges();
      },
      error: (error) => {
        this.isLoadingLocation = false;
        this.selectedLocation = null;
        this.locationCleared.emit();
        console.error('Error al llamar a la API de georef:', error);
        this.cdr.detectChanges();
      }
    });
  }
}
