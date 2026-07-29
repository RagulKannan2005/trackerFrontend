import { Component, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login',
  imports: [RouterLink, FormsModule],
  templateUrl: './login.html',
  styleUrls: ['./login.css'],
})
export class Login {
  private router = inject(Router);

  loginData = {
    email: 'admin1@gmail.com',
    password: 'admin123',
    remember: false
  };

  onLogin() {
    if (this.loginData.email == "admin1@gmail.com" && this.loginData.password == "admin123") {
      this.router.navigate(['admindashboard']);
    }
    console.log('Login data submitted:', this.loginData);
  }
}
