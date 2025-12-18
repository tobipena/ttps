import { Component, OnInit, PLATFORM_ID, inject, ChangeDetectorRef } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { isPlatformBrowser, CommonModule } from '@angular/common';
import { PlatformService } from '../../services/platform.service';

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
  foto?: string; // Deprecated pero mantener compatibilidad
  descripcion: string;
  animal: string;
  estado: string;
  imagenes?: Imagen[]; // Nueva propiedad
}

interface Desaparicion {
  id: number;
  comentario: string;
  coordenada: string;
  foto: string;
  fecha: string;
  mascota: Mascota;
}

@Component({
  selector: 'app-home',
  imports: [CommonModule],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home implements OnInit {
  desapariciones: Desaparicion[] = [];
  loading = true;
  currentIndex = 0;
  cardsPerView = 3;
  Math = Math;

  private apiUrl = 'http://localhost:8080/ttps/desapariciones';
  private platformId = inject(PLATFORM_ID);
  private cdr = inject(ChangeDetectorRef);

  constructor(
    private readonly http: HttpClient,
    private readonly platformService: PlatformService
  ) {}

  ngOnInit() {
    if (isPlatformBrowser(this.platformId)) {
      this.loadDesapariciones();
      this.updateCardsPerView();
      window.addEventListener('resize', () => this.updateCardsPerView());
    }
  }

  loadDesapariciones() {
    this.loading = true;
    this.cdr.detectChanges();

    this.http.get<Desaparicion[]>(this.apiUrl).subscribe({
      next: (data) => {
        this.desapariciones = data.slice(0, 10);
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

  updateCardsPerView() {
    if (this.platformService.isBrowser) {
      const width = window.innerWidth;
      if (width < 768) {
        this.cardsPerView = 1;
      } else if (width < 1200) {
        this.cardsPerView = 2;
      } else {
        this.cardsPerView = 3;
      }
    }
  }

  getVisibleCards(): Desaparicion[] {
    return this.desapariciones.slice(this.currentIndex, this.currentIndex + this.cardsPerView);
  }

  nextSlide() {
    if (this.currentIndex < this.desapariciones.length - this.cardsPerView) {
      this.currentIndex++;
    }
  }

  prevSlide() {
    if (this.currentIndex > 0) {
      this.currentIndex--;
    }
  }

  goToSlide(index: number) {
    this.currentIndex = index * this.cardsPerView;
  }

  getDots(): number[] {
    const totalDots = Math.ceil(this.desapariciones.length / this.cardsPerView);
    return new Array(totalDots).fill(0);
  }

  verDetalle(id: number) {
    // Aquí puedes implementar la navegación al detalle
  }

  getPrimeraImagen(mascota: Mascota): string | null {
    // Primero intentar con las nuevas imágenes
    if (mascota.imagenes && mascota.imagenes.length > 0) {
      return mascota.imagenes[0].datos;
    }
    // Fallback al campo foto deprecated
    if (mascota.foto) {
      return mascota.foto;
    }
    return null;
  }
}
