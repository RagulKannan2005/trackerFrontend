import { Routes } from '@angular/router';
import { Register } from './Authtication/register/register';
import { Login } from './Authtication/login/login';
import { Dashboard } from './Admin/dashboard/dashboard';
import { TeamMember } from './Admin/team-member/team-member';

export const routes: Routes = [
    { path: '', redirectTo: 'login', pathMatch: 'full' },
    { path: 'register', component: Register },
    { path: 'login', component: Login },
    { path: 'admindashboard', component: Dashboard },
    { path: 'team-member', component: TeamMember }
];
