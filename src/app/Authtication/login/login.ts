import { Component, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { FormBuilder, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Auth } from '../../services/auth';

@Component({
  selector: 'app-login',
  imports: [RouterLink, FormsModule,ReactiveFormsModule],
  templateUrl: './login.html',
  styleUrls: ['./login.css'],
})
export class Login {
 
  private fb=inject(FormBuilder);
  private authservice=inject(Auth);

  loginData =this.fb.group({
    email: [''],
    password: [''],
    remember: false
  });
 constructor(private router:Router){}
  onLogin() {
    const credentials = {
      email: this.loginData.value.email,
      password: this.loginData.value.password
    };
    this.authservice.login(credentials).subscribe({
      next:(response:any)=>{
        if (response?.token) {
          localStorage.setItem('token', response.token);
          localStorage.setItem(
            'user',
            JSON.stringify({
              id: response.id,
              username: response.username,
              role: response.role,
            })
          );
          const userRole = (response?.role || '').toString().toUpperCase();
          if (userRole === 'USER' ) {
            this.router.navigate(['/userDashboard']);
            console.log(response);
          } else {
            this.router.navigate(['/admin/home']);
          }
        }
        console.log('login successful');
      },
      error:(error)=>{
        console.log('login failed');
        console.log(error);
        this.loginData.reset();
      }
    })
    
  }
}
