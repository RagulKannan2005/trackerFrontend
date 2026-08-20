import { Component, OnInit, ChangeDetectorRef, inject } from '@angular/core';
import { GoogleMeetService } from '../../../services/google-meet.service';
import { Meeting } from '../../../models/meeting.model';
import { Router } from '@angular/router';
import { Auth } from '../../../services/auth';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-meeting-list',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './meeting-list.html',
  styleUrl: './meeting-list.css',
})
export class MeetingList implements OnInit {
  private googlemeetService = inject(GoogleMeetService);
  private router = inject(Router);
  public authservice = inject(Auth);
  private cdr = inject(ChangeDetectorRef);

  meetings: Meeting[] = [];

  searchText = '';
  isLoading = false;

  ngOnInit(): void {
    this.loadMeetings();
  }

  isAdmin(): boolean {
    const role = this.authservice.getrole();
    if (!role) return true;
    const r = String(role).toUpperCase();
    return r === 'ADMIN' || r === 'ROLE_ADMIN' || r.includes('ADMIN');
  }

  loadMeetings(): void {
    this.isLoading = true;

    this.googlemeetService.getAllMeetings().subscribe({
      next: (data) => {
        this.meetings = Array.isArray(data) ? data : (data as any)?.content || [];
        this.isLoading = false;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error('Error loading meetings', err);
        this.meetings = [];
        this.isLoading = false;
        this.cdr.markForCheck();
      },
    });
  }

  get filteredMeetings(): Meeting[] {
    const meetingsList = Array.isArray(this.meetings) ? this.meetings : [];
    if (!this.searchText || !this.searchText.trim()) {
      return meetingsList;
    }
    const search = this.searchText.toLowerCase().trim();

    return meetingsList.filter((meeting) => {
      return (
        (meeting.summary ? String(meeting.summary).toLowerCase() : '').includes(search) ||
        (meeting.description ? String(meeting.description).toLowerCase() : '').includes(search)
      );
    });
  }

  createMeetings(): void {
    this.router.navigate(['/admin/meetings/new']);
  }

  viewMeetings(id: number): void {
    this.router.navigate(['/admin/meetings', id]);
  }

  joinMeeting(link: string): void {
    if (!link) {
      return;
    }

    window.open(link, '_blank', 'noopener,noreferrer');
  }
}
