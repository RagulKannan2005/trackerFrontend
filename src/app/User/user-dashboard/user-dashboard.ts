import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Auth } from '../../services/auth';
import { GoogleMeetService } from '../../services/google-meet.service';
import { Meeting } from '../../models/meeting.model';

@Component({
  selector: 'app-user-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './user-dashboard.html',
  styleUrl: './user-dashboard.css',
})
export class UserDashboard implements OnInit {
  Authservice = inject(Auth);

  activeTab: string = 'dashboard';

  // Candidate Profile Details (derived from backend Candidate & Users entities)
  candidate = {
    name: 'Alex Johnson',
    email: 'alex.johnson@example.com',
    degree: 'B.Tech',
    stream: 'Computer Science & Engineering',
    graduationYear: '2025',
    phone: '+91 98765 43210',
    trackerName: 'Full Stack Java & Angular Coaching',
    haveLaptop: true,
    haveInternet: true,
    haveMobile: true,
    englishSpeaking: 'EXCELLENT',
    englishWriting: 'GOOD',
    englishReading: 'EXCELLENT',
  };

  // KPI Statistics
  stats = {
    overallProgress: 75,
    completedSkills: 18,
    totalSkills: 24,
    attendanceRate: 92,
    upcomingMeetingsCount: 2,
  };

  // Tracker Sections & Skill Progress
  sections = [
    {
      id: 1,
      title: 'Core Java & OOP Concepts',
      description: 'Classes, Objects, Inheritance, Polymorphism, Collections & Multithreading',
      progress: 100,
      skills: [
        { name: 'Java Basics & Syntax', status: 'COMPLETED' },
        { name: 'OOP Concepts', status: 'COMPLETED' },
        { name: 'Collections Framework', status: 'COMPLETED' },
        { name: 'Exception Handling', status: 'COMPLETED' },
      ],
    },
    {
      id: 2,
      title: 'Spring Boot REST APIs',
      description: 'Dependency Injection, Spring Data JPA, Hibernate & Security',
      progress: 75,
      skills: [
        { name: 'Spring Boot Setup & Controllers', status: 'COMPLETED' },
        { name: 'Spring Data JPA & Entities', status: 'COMPLETED' },
        { name: 'Spring Security & JWT Auth', status: 'IN_PROGRESS' },
        { name: 'Unit Testing with JUnit & Mockito', status: 'NOT_STARTED' },
      ],
    },
    {
      id: 3,
      title: 'Angular Frontend Development',
      description: 'Components, Services, RxJS, Reactive Forms & Routing',
      progress: 60,
      skills: [
        { name: 'TypeScript & Angular CLI', status: 'COMPLETED' },
        { name: 'Components & Data Binding', status: 'COMPLETED' },
        { name: 'Services & HttpClient', status: 'IN_PROGRESS' },
        { name: 'State Management & Guards', status: 'NOT_STARTED' },
      ],
    },
    {
      id: 4,
      title: 'Mock Interviews & Soft Skills',
      description: 'Resume building, HR rounds, technical mock interviews & presentation',
      progress: 40,
      skills: [
        { name: 'Resume Optimization', status: 'COMPLETED' },
        { name: 'Self Introduction & Behavioral Qs', status: 'IN_PROGRESS' },
        { name: 'Technical Mock Interview #1', status: 'NOT_STARTED' },
        { name: 'Final Assessment & Placement Prep', status: 'NOT_STARTED' },
      ],
    },
  ];

  // Upcoming Google Meet Coaching Sessions

  private googlemeetservice = inject(GoogleMeetService);
  private cdr = inject(ChangeDetectorRef);
  upcomingMeetings: Meeting[] = [];

  ngOnInit(): void {
    this.loadUpcomingMeetings();
  }

  loadUpcomingMeetings(): void {
    this.googlemeetservice.getAllMeetings().subscribe({
      next: (data: any) => {
        const meetings: Meeting[] = Array.isArray(data) ? data : data?.content || [];
        const now = new Date();

        const filtered = meetings.filter((meeting: Meeting) => {
          if (!meeting.startDateTime) return true;
          const dateStr = meeting.startDateTime.includes(' ') && !meeting.startDateTime.includes('T')
            ? meeting.startDateTime.replace(' ', 'T')
            : meeting.startDateTime;
          const start = new Date(dateStr);
          return isNaN(start.getTime()) || start >= now;
        });

        // Fall back to showing all meetings if no future meetings exist
        this.upcomingMeetings = (filtered.length > 0 ? filtered : meetings).slice(0, 3);
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error('Failed to load Meetings:', err);
        this.upcomingMeetings = [];
        this.cdr.markForCheck();
      },
    });
  }

  joinMeeting(url?: string): void {
    if (url) {
      window.open(url, '_blank', 'noopener,noreferrer');
    }
  }
}
