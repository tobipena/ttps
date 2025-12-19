import { Component, OnInit, PLATFORM_ID, inject } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { DesaparicionService } from '../../services/desaparicion.service';
import { UsuarioService } from '../../services/usuario.service';
import { PlatformService } from '../../services/platform.service';
import { LocationMap, LocationData } from '../location-map/location-map';

@Component({
  selector: 'app-desaparicion',
  imports: [CommonModule, ReactiveFormsModule, RouterModule, LocationMap],
  templateUrl: './desaparicion.html',
  styleUrl: './desaparicion.css',
})
export class Desaparicion implements OnInit{
  desaparicionForm: FormGroup;
  loading = false;
  fotoBase64: string = '';
  selectedFile: File | null = null;
  selectedFiles: File[] = [];
  fotosBase64: string[] = [];
  usuarioId: number | null = null;
  locationError = '';

  private platformId = inject(PLATFORM_ID);

  constructor(
    private fb: FormBuilder,
    private desaparicionService: DesaparicionService,
    private usuarioService: UsuarioService,
    private router: Router,
    private platformService: PlatformService
  ) {
    this.desaparicionForm = this.fb.group({
      nombreMascota: ['', Validators.required],
      animal: ['', Validators.required],
      tamano: ['', Validators.required],
      descripcion: ['', Validators.required],
      colorMascota: ['', Validators.required],
      fecha: ['', Validators.required],
      latitud: ['', Validators.required],
      longitud: ['', Validators.required],
      comentario: ['', Validators.required]
    });
  }

  ngOnInit() {
    // Solo ejecutar en el navegador, no en SSR
    if (isPlatformBrowser(this.platformId)) {
      // Obtener el ID del usuario autenticado
      this.usuarioService.obtenerPerfil().subscribe({
        next: (user) => {
          this.usuarioId = user.id;
        },
        error: (error) => {
          console.error('Error al obtener usuario:', error);
        }
      });
    }
  }

  onFileSelected(event: any) {
    const files: FileList = event.target.files;
    if (files && files.length > 0) {
      const newFiles = Array.from(files);

      // AGREGAR a los archivos existentes en lugar de reemplazar
      this.selectedFiles = [...this.selectedFiles, ...newFiles];

      // Convertir los nuevos archivos a Base64
      for (let i = 0; i < files.length; i++) {
        const file = files[i];
        const reader = new FileReader();

        reader.onload = (e: any) => {
          this.fotosBase64.push(e.target.result as string);

          // Para compatibilidad, guardar la primera como fotoBase64
          if (this.fotosBase64.length === 1) {
            this.fotoBase64 = e.target.result as string;
            this.selectedFile = file;
          }
        };

        reader.readAsDataURL(file);
      }
    }

    // Limpiar el input para permitir seleccionar los mismos archivos otra vez
    event.target.value = '';
  }

  removeFile(index: number, fileInput: any) {
    this.selectedFiles.splice(index, 1);
    this.fotosBase64.splice(index, 1);

    // Si eliminamos todos, limpiar el input
    if (this.selectedFiles.length === 0) {
      fileInput.value = '';
      this.fotoBase64 = '';
      this.selectedFile = null;
    }
  }

  clearFiles(fileInput: any) {
    this.selectedFiles = [];
    this.fotosBase64 = [];
    this.fotoBase64 = '';
    this.selectedFile = null;
    fileInput.value = '';
  }

  onSubmit() {
    if (this.desaparicionForm.valid) {
      if (!this.usuarioId) {
        this.router.navigate(['/login']);
        return;
      }

      // Validar que haya al menos una imagen
      if (this.selectedFiles.length === 0) {
        if (isPlatformBrowser(this.platformId)) {
          alert('Debes subir al menos una foto de tu mascota');
        }
        return;
      }

      // Validar que haya una ubicación seleccionada
      const latitud = this.desaparicionForm.get('latitud')?.value;
      const longitud = this.desaparicionForm.get('longitud')?.value;
      if (!latitud || !longitud) {
        this.locationError = 'Debe seleccionar una ubicación en el mapa';
        return;
      }

      this.loading = true;

      const fechaValue = this.desaparicionForm.get('fecha')?.value;
      const fechaISO = fechaValue ? new Date(fechaValue).toISOString() : new Date().toISOString();

      // Preparar todas las imágenes en el formato correcto
      const imagenes = [];
      for (let i = 0; i < this.fotosBase64.length; i++) {
        // Extraer solo la parte Base64 (sin el prefijo "data:image/jpeg;base64,")
        const base64Data = this.fotosBase64[i].split(',')[1];
        imagenes.push({
          datos: base64Data,
          tipo: this.selectedFiles[i].type || 'image/jpeg'
        });
      }

      const desaparicionData = {
        comentario: this.desaparicionForm.get('comentario')?.value,
        latitud: parseFloat(latitud),
        longitud: parseFloat(longitud),
        fecha: fechaISO,
        mascotaDTO: {
          nombre: this.desaparicionForm.get('nombreMascota')?.value,
          tamano: this.desaparicionForm.get('tamano')?.value,
          color: this.desaparicionForm.get('colorMascota')?.value || 'Sin especificar',
          descripcion: this.desaparicionForm.get('descripcion')?.value,
          estado: 'PERDIDO_PROPIO',
          animal: this.desaparicionForm.get('animal')?.value,
          imagenes: imagenes
        },
        usuarioId: this.usuarioId
      };

      this.desaparicionService.crear(desaparicionData).subscribe({
        next: () => {
          this.loading = false;
          this.router.navigate(['/profile']);
        },
        error: (error: any) => {
          this.loading = false;
          console.error('Error al reportar desaparición:', error);
        }
      });
    }
  }

  onLocationSelected(location: LocationData): void {
    this.locationError = '';
    // Guardar coordenadas en formato "lat, lng"
    const latitud = `${location.lat.toFixed(6)}`;
    const longitud = `${location.lng.toFixed(6)}`;
    this.desaparicionForm.patchValue({ latitud, longitud });
  }

  onLocationCleared(): void {
    this.locationError = 'La ubicación seleccionada debe estar dentro de Argentina';
    this.desaparicionForm.patchValue({ latitud: '', longitud: '' });
  }
}

