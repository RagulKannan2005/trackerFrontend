import { Routes } from '@angular/router';
import { Register } from './Authtication/register/register';
import { Login } from './Authtication/login/login';
import { Dashboard } from './Admin/dashboard/dashboard';
import { Home } from './Admin/home/home';
import { TeamMember } from './Admin/team-member/team-member';
import { FinalYear } from './Admin/final-year/final-year';
import { Courseinfo } from './CommonPages/courseinfo/courseinfo';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: Login },
  { path: 'register', component: Register },
  
  // Shared Admin Layout
  {
    path: 'admin',
    component: Dashboard,
    children: [
      { path: 'home', component: Home },
      { path: 'team-member', component: TeamMember },
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      {path:'final-year',component:FinalYear},
      {path:'courseinfo',component:Courseinfo}


    ]
  }
];
