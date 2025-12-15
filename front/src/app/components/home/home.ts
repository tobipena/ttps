import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { PlatformService } from '../../services/platform.service';

interface Mascota {
  id: number;
  nombre: string;
  tamano: string;
  color: string;
  foto: string;
  descripcion: string;
  animal: string;
  estado: string;
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

  constructor(
    private readonly http: HttpClient,
    private readonly platformService: PlatformService
  ) {}

  ngOnInit() {
    this.loadDesapariciones();
    if (this.platformService.isBrowser) {
      this.updateCardsPerView();
      window.addEventListener('resize', () => this.updateCardsPerView());
    }
  }

  loadDesapariciones() {
    this.http.get<Desaparicion[]>(this.apiUrl).subscribe({
      next: (data) => {
        this.desapariciones = data.slice(0, 10);
        this.loading = false;
      },
      error: (error) => {
        console.error('Error al cargar desapariciones:', error);
        this.loading = false;
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
    console.log('Ver detalle de desaparición:', id);
    // Aquí puedes implementar la navegación al detalle
  }
}
