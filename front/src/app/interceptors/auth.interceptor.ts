import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { PlatformService } from '../services/platform.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const platformService = inject(PlatformService);

  const authData = platformService.getLocalStorage('authData');
  
  if (authData) {
    const { token } = JSON.parse(authData);
    
    const authReq = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
    
    return next(authReq);
  }

  return next(req);
};
