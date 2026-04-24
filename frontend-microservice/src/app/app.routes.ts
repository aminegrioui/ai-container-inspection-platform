import { Routes } from '@angular/router';

export const routes: Routes = [
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  {
    path: 'home',
    loadComponent: () => import('./pages/home/home.component').then(m => m.HomeComponent)
  },
  // --- add new pages here as your app grows ---
  // { path: 'history',    loadComponent: () => import('./pages/history/history.component').then(m => m.HistoryComponent) },
  // { path: 'statistics', loadComponent: () => import('./pages/statistics/statistics.component').then(m => m.StatisticsComponent) },
  { path: '**', redirectTo: 'home' }
];

