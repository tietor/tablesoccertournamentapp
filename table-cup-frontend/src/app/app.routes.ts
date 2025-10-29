import {Routes} from '@angular/router';
import {authGuard} from './guard/auth.guard';

export const routes: Routes = [
  {
    path: 'home',
    loadComponent: () => import('./page/home/home.page').then(m => m.HomePage)
  },
  {
    path: 'login',
    loadComponent: () => import('./page/login/login.page').then(m => m.LoginPage)
  },
  {
    path: 'register',
    loadComponent: () => import('./page/register/register.page').then(m => m.RegisterPage)
  },
  {
    path: 'tournaments',
    loadComponent: () => import('./page/tournament/tournament.page').then(m => m.TournamentPage),
    canActivate: [authGuard]
  },
  {
    path: 'games/:uuid',
    loadComponent: () => import('./page/game/game.page').then(m => m.GamePage), canActivate: [authGuard]
  },
  {
    path: '',
    redirectTo: '/tournaments',
    pathMatch: 'full',
  },
];
