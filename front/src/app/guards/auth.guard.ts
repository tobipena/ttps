import { inject, PLATFORM_ID } from '@angular/core';
import { Router, CanActivateFn } from '@angular/router';
import { isPlatformBrowser } from '@angular/common';
import { AuthService } from '../services/auth.service';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  const platformId = inject(PLATFORM_ID);

  // En SSR, permitir acceso (se verificará en el cliente)
  if (!isPlatformBrowser(platformId)) {
    return true;
  }

  // En el navegador, verificar autenticación
  if (authService.isAuthenticated() && !authService.isTokenExpired()) {
    return true;
  }

  // Si no está autenticado o el token expiró, redirigir al login
  authService.logout();
  router.navigate(['/login']);
  return false;
};
