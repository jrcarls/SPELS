import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../../core/config/api-config';

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  name: string;
  organizationName: string;
  cnpj: string | null;
  email: string;
  password: string;
}

@Injectable({ providedIn: 'root' })
export class AuthApi {
  private readonly http = inject(HttpClient);
  private readonly apiBaseUrl = inject(API_BASE_URL);

  login(request: LoginRequest): Observable<void> {
    return this.http.post<void>(`${this.apiBaseUrl}/auth/login`, request, { withCredentials: true });
  }

  register(request: RegisterRequest): Observable<void> {
    return this.http.post<void>(`${this.apiBaseUrl}/auth/register`, request, { withCredentials: true });
  }
}
