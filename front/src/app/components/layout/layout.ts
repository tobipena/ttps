import { Component, HostListener } from '@angular/core';
import { RouterOutlet, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-layout',
  imports: [RouterOutlet, CommonModule],
  templateUrl: './layout.html',
  styleUrl: './layout.css',
})
export class Layout {
  showDropdown = false;
  showLogoutModal = false;

  constructor(
    public authService: AuthService,
    private readonly router: Router
  ) {}

  @HostListener('document:click', ['$event'])
  onClickOutside(event: MouseEvent): void {
    const target = event.target as HTMLElement;
    if (!target.closest('.user-dropdown')) {
      this.showDropdown = false;
    }
  }

  toggleDropdown(): void {
    this.showDropdown = !this.showDropdown;
  }

  navigateToProfile(): void {
    this.router.navigate(['/profile']).then(() => {
      this.showDropdown = false;
    });
  }

  navigateToLogin(): void {
    this.router.navigate(['/login']);
  }

  navigateToRegister(): void {
    this.router.navigate(['/register']);
  }

  navigateToHome(): void {
    this.router.navigate(['/']);
  }

  showLogoutConfirmation(): void {
    this.showDropdown = false;
    this.showLogoutModal = true;
  }

  confirmLogout(): void {
    this.showLogoutModal = false;
    this.authService.logout();
    this.router.navigate(['/']);
  }

  cancelLogout(): void {
    this.showLogoutModal = false;
  }
  navigateToDesaparicion(): void {
    this.router.navigate(['/desaparicion']);
  }

  navigateToListado(): void {
    this.router.navigate(['/listado']);
  }
}
