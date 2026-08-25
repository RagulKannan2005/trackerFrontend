import { inject } from '@angular/core';
import { type CanActivateFn , Router } from '@angular/router';
import { Auth } from '../services/auth';

export const adminGuard: CanActivateFn = (route,state) => {
  const authservice = inject(Auth);
  const router = inject(Router);

  const user=authservice.getuser();

  if (user && (user.role === 'ADMIN' || user.role === 'USER')) {
    return true;
  }

  router.navigate(['/login']);
  return false;
};
