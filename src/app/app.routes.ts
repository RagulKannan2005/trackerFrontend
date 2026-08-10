import { Routes } from '@angular/router';
import { Register } from './Authtication/register/register';
import { Login } from './Authtication/login/login';
import { Dashboard } from './Admin/dashboard/dashboard';
import { Home } from './Admin/home/home';
import { TeamMember } from './Admin/team-member/team-member';
import { FinalYear } from './Admin/final-year/final-year';
import { Java } from './Trackers/java/java';
import { DotNet } from './Trackers/dot-net/dot-net';
import { CyberSecurity } from './Trackers/cyber-security/cyber-security';
import { DevOps } from './Trackers/dev-ops/dev-ops';
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
      {path:'java',component:Java},
      {path:'dotnet',component:DotNet},
      {path:'cybersecurity',component:CyberSecurity},
      {path:'devops',component:DevOps},
      {path:'courseinfo',component:Courseinfo}


    ]
  }
];
