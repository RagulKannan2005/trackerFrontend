import { Component, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-register',
  imports: [RouterLink, FormsModule],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {
  private router = inject(Router);

  registerData = {
    username: '',
    email: '',
    password: '',
    role: ''
  };

  onRegister() {
    if(this.registerData.role == "Admin")
    {
      alert("Admin registered successfully");
      this.router.navigate(['admindashboard']);
    }
    else if(this.registerData.role == "Student")
    {
      alert("Student registered successfully");
      this.router.navigate(['studentdashboard']);
    }
    console.log('Register data submitted:', this.registerData);
  }
}
