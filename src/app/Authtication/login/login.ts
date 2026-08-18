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
        localStorage.setItem('token',response.token);
        localStorage.setItem(
          'user',
          JSON.stringify({
            id:response.id,
            username:response.username,
            role:response.role,
          })
        )
        if (response?.token) {
          this.router.navigate(['/admin/home']);
        }
        console.log('login successfull');
      },
      error:(error)=>{
        console.log('login failed');
        console.log(error);
        this.loginData.reset();
      }
    })
    
  }
}
