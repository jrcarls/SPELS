import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { API_BASE_URL } from '../../../core/config/api-config';
import { AuthApi } from './auth-api';

describe('AuthApi', () => {
  let api: AuthApi;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        { provide: API_BASE_URL, useValue: 'https://api.example.test' },
      ],
    });

    api = TestBed.inject(AuthApi);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('should send login requests to the configured API', () => {
    const request = { email: 'ana@example.com', password: 'password123' };

    api.login(request).subscribe();

    const testRequest = http.expectOne('https://api.example.test/auth/login');
    expect(testRequest.request.method).toBe('POST');
    expect(testRequest.request.withCredentials).toBe(true);
    expect(testRequest.request.body).toEqual(request);
    testRequest.flush(null);
  });

  it('should send registration requests to the configured API', () => {
    const request = {
      name: 'Ana Souza',
      organizationName: 'Doces da Ana',
      cnpj: null,
      email: 'ana@example.com',
      password: 'password123',
    };

    api.register(request).subscribe();

    const testRequest = http.expectOne('https://api.example.test/auth/register');
    expect(testRequest.request.method).toBe('POST');
    expect(testRequest.request.withCredentials).toBe(true);
    expect(testRequest.request.body).toEqual(request);
    testRequest.flush(null);
  });
});
