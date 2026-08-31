import { Routes } from '@angular/router';

export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'login' },
  {
    path: 'login',
    title: 'Entrar | SPELS',
    loadComponent: () => import('./features/auth/pages/login/login').then((m) => m.Login),
  },
  {
    path: 'cadastro',
    title: 'Criar conta | SPELS',
    loadComponent: () => import('./features/auth/pages/register/register').then((m) => m.Register),
  },
  {
    path: 'inicio',
    title: 'Início | SPELS',
    loadComponent: () => import('./layouts/app-shell/app-shell').then((m) => m.AppShell),
    children: [
      { path: '', loadComponent: () => import('./features/home/pages/home/home').then((m) => m.Home) },
    ],
  },
  { path: '**', redirectTo: 'login' },
];
