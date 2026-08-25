import { HttpClient } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class Auth {
  private http = inject(HttpClient);

  private apiurl = 'http://localhost:8081/api/v1/auth';

  currentUser = signal<any>(null);

  constructor(private router: Router) {}
  register(user: any) {
    return this.http.post(this.apiurl + '/register', user);
  }

  login(user: any) {
    return this.http.post(this.apiurl + '/authenticate', user);
  }
  getuser() {
    const user = localStorage.getItem('user');
    return user ? JSON.parse(user) : null;
  }
  getusername() {
    const u = this.getuser();
    return u?.userName || u?.username || null;
  }
  getrole(): string | null {
    const role = this.getuser()?.role;
    return role ? role.toString().toUpperCase() : null;
  }
  isAdmin(): boolean {
    const role = this.getrole();
    return role === 'ADMIN' || role === 'ROLE_ADMIN';
  }
  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('user');

    this.currentUser.set(null);

    this.router.navigate(['/login'], {
      replaceUrl: true,
    });
  }
}
