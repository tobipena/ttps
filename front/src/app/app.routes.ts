import { Routes } from '@angular/router';
import { Home } from './components/home/home';
import { Login } from './components/login/login';
import { Register } from './components/register/register';
import { Profile } from './components/profile/profile';
import { Desaparicion } from './components/desaparicion/desaparicion';
import { authGuard } from './guards/auth.guard';
import { DesaparicionDetalle } from './components/desaparicion-detalle/desaparicion-detalle';

export const routes: Routes = [
  {
    path: '',
    component: Home
  },
  {
    path: 'login',
    component: Login
  },
  {
    path: 'register',
    component: Register
  },
  {
    path: 'profile',
    component: Profile,
    canActivate: [authGuard]
  },
  {
    path: 'desaparicion',
    component: Desaparicion,
    canActivate: [authGuard]
  },
  {
    path: 'desaparicion/:id',
    component: DesaparicionDetalle
  }
];
