import {CanActivateFn, Router} from '@angular/router';
import {inject} from '@angular/core';
import {AuthService} from '../service/auth/auth.service';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  if (authService.isUserLoggedIn()) {
    return true;
  }
  const router = inject(Router);
  router.navigate(['/home']).then();
  return true;
};
