import { Component, signal } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';

@Component({
  imports: [MatIconModule, RouterLink, RouterLinkActive, RouterOutlet],
  host: {
    '(document:keydown.escape)': 'closeNotifications()',
  },
  selector: 'app-shell',
  styleUrl: './app-shell.scss',
  templateUrl: './app-shell.html',
})
export class AppShell {
  protected readonly menuOpen = signal(false);
  protected readonly darkMode = signal(false);
  protected readonly collapsed = signal(false);
  protected readonly mobileOpen = signal(false);
  protected readonly notificationsOpen = signal(false);

  protected toggleSidebar(): void {
    this.collapsed.update((collapsed) => !collapsed);
  }

  protected toggleMobileMenu(): void {
    this.mobileOpen.update((open) => !open);
  }

  protected toggleMenu(): void {
    this.menuOpen.update((open) => !open);
  }

  protected toggleNotifications(): void {
    this.notificationsOpen.update((open) => !open);
  }

  protected closeNotifications(): void {
    this.notificationsOpen.set(false);
  }

  protected setTheme(dark: boolean): void {
    this.darkMode.set(dark);
  }
}
