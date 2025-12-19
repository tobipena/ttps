import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { PlatformService } from '../services/platform.service';
import { AuthService } from '../services/auth.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const platformService = inject(PlatformService);
  const authService = inject(AuthService);
  const router = inject(Router);

  const authData = platformService.getLocalStorage('authData');
  
  if (authData) {
    const { token, expiresAt } = JSON.parse(authData);
    
    // Verificar si el token expiró
    if (Date.now() >= expiresAt) {
      // Token expirado, hacer logout y redirigir
      authService.logout();
      router.navigate(['/login']);
      return next(req);
    }
    
    const authReq = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
    
    // Interceptar errores de respuesta
    return next(authReq).pipe(
      catchError((error: HttpErrorResponse) => {
        // Si el backend responde con 401, el token es inválido o expiró
        if (error.status === 401) {
          authService.logout();
          router.navigate(['/login']);
        }
        return throwError(() => error);
      })
    );
  }

  return next(req);
};
