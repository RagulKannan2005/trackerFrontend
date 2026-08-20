import { Routes } from '@angular/router';
import { Register } from './Authtication/register/register';
import { Login } from './Authtication/login/login';
import { Dashboard } from './Admin/dashboard/dashboard';
import { Home } from './Admin/home/home';
import { TeamMember } from './Admin/team-member/team-member';
import { FinalYear } from './Admin/final-year/final-year';
import { Courseinfo } from './CommonPages/courseinfo/courseinfo';
import { TrackerList } from './features/Trackers/tracker-list/tracker-list';
import { TrackerDetails } from './features/Trackers/tracker-details/tracker-details';
import { SectionList } from './features/Section/section-list/section-list';
import { SectionDeatils } from './features/Section/section-deatils/section-deatils';
import { SkillList } from './features/Skills/skill-list/skill-list';
import { MeetingList } from './features/GoogleMeet/meeting-list/meeting-list';
import { MeetingForm } from './features/GoogleMeet/meeting-form/meeting-form';
import { MeetingDetails } from './features/GoogleMeet/meeting-details/meeting-details';
import { UserDashboard } from './User/user-dashboard/user-dashboard';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: Login },
  { path: 'register', component: Register },
  {path:'userDashboard',component:UserDashboard},

  // Shared Admin Layout
  {
    path: 'admin',
    component: Dashboard,
    children: [
      { path: 'home', component: Home },
      { path: 'team-member', component: TeamMember },
      { path: 'final-year', component: FinalYear },
      { path: 'courseinfo', component: Courseinfo },
      { path: 'trackers', component: TrackerList },
      { path: 'trackers/:id', component: TrackerDetails },
      { path: 'sections', component: SectionList },
      { path: 'sections/:id', component: SectionDeatils },
      { path: 'skills', component: SkillList },
      { path: '', redirectTo: 'home', pathMatch: 'full' },
      { path: 'meetings', children: [
        { path: '', component: MeetingList },
        {path:'new',component:MeetingForm},
        {path:':id',component:MeetingDetails}
      ] },
    ],
  },
];
