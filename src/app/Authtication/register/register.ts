import { Component, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { FormBuilder, FormsModule } from '@angular/forms';
import { Auth } from '../../services/auth';

@Component({
  selector: 'app-register',
  imports: [RouterLink, FormsModule],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {
  private router = inject(Router);
  private fb = inject(FormBuilder);
  private authService = inject(Auth);

  registerData = {
    username: '',
    email: '',
    password: '',
    role: '',
  };

  errorMessage = '';
  successMessage = '';
  isLoading = false;

  onRegister() {
    this.errorMessage = '';
    this.successMessage = '';
    const credentials = {
      username: this.registerData.username,
      email: this.registerData.email,
      password: this.registerData.password,
      role: this.registerData.role,
    };
    this.isLoading = true;
    this.authService.register(credentials).subscribe({
      next: (data: any) => {
        this.isLoading = false;
        this.successMessage = 'Registration is successfull Redirecting to login';

        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 2000);
      },
      error: (err) => {
        this.isLoading = false;
        if (err.error.includes('User is already registered')) {
          this.errorMessage = 'User is already registered';
        } else {
          this.errorMessage = 'Registration failed';
        }
        this.registerData = { username: '', email: '', password: '', role: '' };
      },
    });
  }
}
