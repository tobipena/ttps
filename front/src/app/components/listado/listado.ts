import { Component, OnInit, PLATFORM_ID, inject, ChangeDetectorRef } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { isPlatformBrowser, CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';

interface Imagen {
  id: number;
  datos: string;
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
  foto: string;
  fecha: string;
  latitud: number;
  longitud: number;
  ciudad?: string;
  provincia?: string;
  mascota: Mascota;
}

interface Provincia {
  id: string;
  nombre: string;
}

interface Municipio {
  id: string;
  nombre: string;
  provincia: {
    id: string;
    nombre: string;
  };
}

interface GeorefProvinciaResponse {
  provincias: Provincia[];
}

interface GeorefMunicipioResponse {
  municipios: Municipio[];
}

interface PaginatedResponse {
  content: Desaparicion[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

@Component({
  selector: 'app-listado',
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './listado.html',
  styleUrl: './listado.css',
})
export class Listado implements OnInit {
  todasDesapariciones: Desaparicion[] = []; // Todas las desapariciones sin filtrar
  desapariciones: Desaparicion[] = []; // Desapariciones filtradas y paginadas
  loading = true;
  loadingCiudades = false;
  
  // Paginación
  currentPage = 0;
  pageSize = 12;
  totalPages = 0;
  totalElements = 0;
  
  // Filtros
  filtros = {
    provincia: '',
    ciudad: '',
    animal: '',
    tamano: ''
  };
  
  // Listas para los filtros
  provincias: Provincia[] = [];
  ciudades: Municipio[] = [];
  animales = ['Perro', 'Gato'];
  tamanos = ['Pequeño', 'Mediano', 'Grande'];

  Math = Math;

  private apiUrl = 'http://localhost:8080/ttps/desapariciones';
  private georefApiUrl = 'https://apis.datos.gob.ar/georef/api';
  private platformId = inject(PLATFORM_ID);
  private cdr = inject(ChangeDetectorRef);

  constructor(private readonly http: HttpClient) {}

  ngOnInit() {
    if (isPlatformBrowser(this.platformId)) {
      this.loadTodasDesapariciones();
      this.loadProvincias();
    }
  }

  loadTodasDesapariciones() {
    this.loading = true;
    this.cdr.detectChanges();

    this.http.get<Desaparicion[]>(this.apiUrl).subscribe({
      next: (data) => {
        this.todasDesapariciones = data;
        // Obtener ubicación para cada desaparición
        this.todasDesapariciones.forEach(desaparicion => {
          if (desaparicion.latitud && desaparicion.longitud) {
            this.obtenerUbicacion(desaparicion);
          }
        });
        this.aplicarFiltros();
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error('Error al cargar desapariciones:', error);
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  obtenerUbicacion(desaparicion: Desaparicion) {
    // Usar Georef API para geocoding inverso - mismo origen que los filtros
    const url = `${this.georefApiUrl}/ubicacion?lat=${desaparicion.latitud}&lon=${desaparicion.longitud}`;

    this.http.get<any>(url).subscribe({
      next: (response) => {
        if (response && response.ubicacion) {
          const ubicacion = response.ubicacion;
          desaparicion.ciudad = ubicacion.municipio?.nombre || '';
          desaparicion.provincia = ubicacion.provincia?.nombre || '';
          this.cdr.detectChanges();
        }
      },
      error: (error) => {
        console.error('Error al obtener ubicación:', error);
      }
    });
  }

  loadProvincias() {
    this.http.get<GeorefProvinciaResponse>(`${this.georefApiUrl}/provincias?campos=id,nombre&max=24`).subscribe({
      next: (response) => {
        this.provincias = response.provincias.sort((a, b) => a.nombre.localeCompare(b.nombre));
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error('Error al cargar provincias:', error);
      }
    });
  }

  onProvinciaChange() {
    this.filtros.ciudad = '';
    this.ciudades = [];
    
    if (this.filtros.provincia) {
      this.loadingCiudades = true;
      this.cdr.detectChanges();

      // Cargar municipios de la provincia seleccionada
      this.http.get<GeorefMunicipioResponse>(
        `${this.georefApiUrl}/municipios?provincia=${this.filtros.provincia}&campos=id,nombre&max=1000`
      ).subscribe({
        next: (response) => {
          this.ciudades = response.municipios.sort((a, b) => a.nombre.localeCompare(b.nombre));
          this.loadingCiudades = false;
          this.cdr.detectChanges();
        },
        error: (error) => {
          console.error('Error al cargar municipios:', error);
          this.loadingCiudades = false;
          this.cdr.detectChanges();
        }
      });
    }
    
    this.aplicarFiltros();
  }

  aplicarFiltros() {
    this.currentPage = 0;
    
    // Filtrar todas las desapariciones según los criterios
    let desaparicionesFiltradas = [...this.todasDesapariciones];

    // Filtro por provincia
    if (this.filtros.provincia) {
      const provinciaNombre = this.provincias.find(p => p.id === this.filtros.provincia)?.nombre;
      if (provinciaNombre) {
        desaparicionesFiltradas = desaparicionesFiltradas.filter(d => 
          d.provincia && d.provincia.toLowerCase() === provinciaNombre.toLowerCase()
        );
      }
    }

    // Filtro por ciudad
    if (this.filtros.ciudad) {
      desaparicionesFiltradas = desaparicionesFiltradas.filter(d => 
        d.ciudad && d.ciudad.toLowerCase() === this.filtros.ciudad.toLowerCase()
      );
    }

    // Filtro por animal
    if (this.filtros.animal) {
      desaparicionesFiltradas = desaparicionesFiltradas.filter(d => 
        d.mascota.animal && d.mascota.animal.toLowerCase() === this.filtros.animal.toLowerCase()
      );
    }

    // Filtro por tamaño
    if (this.filtros.tamano) {
      desaparicionesFiltradas = desaparicionesFiltradas.filter(d => 
        d.mascota.tamano && d.mascota.tamano.toLowerCase() === this.filtros.tamano.toLowerCase()
      );
    }

    // Calcular paginación
    this.totalElements = desaparicionesFiltradas.length;
    this.totalPages = Math.ceil(this.totalElements / this.pageSize);

    // Obtener página actual
    const startIndex = this.currentPage * this.pageSize;
    const endIndex = startIndex + this.pageSize;
    this.desapariciones = desaparicionesFiltradas.slice(startIndex, endIndex);

    this.cdr.detectChanges();
  }

  limpiarFiltros() {
    this.filtros = {
      provincia: '',
      ciudad: '',
      animal: '',
      tamano: ''
    };
    this.ciudades = [];
    this.aplicarFiltros();
  }

  changePage(page: number) {
    if (page >= 0 && page < this.totalPages) {
      this.currentPage = page;
      this.aplicarFiltros(); // Re-aplicar filtros para obtener la nueva página
      // Scroll to top
      if (isPlatformBrowser(this.platformId)) {
        window.scrollTo({ top: 0, behavior: 'smooth' });
      }
    }
  }

  getPages(): number[] {
    const pages: number[] = [];
    const maxPages = 5;
    
    let startPage = Math.max(0, this.currentPage - Math.floor(maxPages / 2));
    let endPage = Math.min(this.totalPages - 1, startPage + maxPages - 1);
    
    if (endPage - startPage < maxPages - 1) {
      startPage = Math.max(0, endPage - maxPages + 1);
    }
    
    for (let i = startPage; i <= endPage; i++) {
      pages.push(i);
    }
    
    return pages;
  }

  getPrimeraImagen(mascota: Mascota): string | null {
    if (mascota.imagenes && mascota.imagenes.length > 0) {
      return mascota.imagenes[0].datos;
    }
    return null;
  }
}
