import { Injectable, signal } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class AuthSession {
  private readonly storageKey = 'spels-authenticated';
  private readonly authenticated = signal(this.hasStoredSession());

  readonly isAuthenticated = this.authenticated.asReadonly();

  markAuthenticated(): void {
    sessionStorage.setItem(this.storageKey, 'true');
    this.authenticated.set(true);
  }

  clear(): void {
    sessionStorage.removeItem(this.storageKey);
    this.authenticated.set(false);
  }

  private hasStoredSession(): boolean {
    return typeof sessionStorage !== 'undefined' && sessionStorage.getItem(this.storageKey) === 'true';
  }
}
