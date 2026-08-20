import { Component, OnInit, ChangeDetectorRef, inject } from '@angular/core';
import { Meeting } from '../../../models/meeting.model';
import { ActivatedRoute, Router } from '@angular/router';
import { GoogleMeetService } from '../../../services/google-meet.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-meeting-details',
  imports: [CommonModule],
  standalone: true,
  templateUrl: './meeting-details.html',
  styleUrl: './meeting-details.css',
})
export class MeetingDetails implements OnInit {
  meeting: Meeting | null = null;

  isloading = false;

  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private googleService = inject(GoogleMeetService);
  private cdr = inject(ChangeDetectorRef);

  ngOnInit(): void {
    this.route.paramMap.subscribe((params) => {
      const id = params.get('id');
      if (id) {
        this.loadMeeting(Number(id));
      }
    });
  }

  loadMeeting(id: number): void {
    this.isloading = true;
    this.googleService.getMeetingById(id).subscribe({
      next: (data) => {
        this.meeting = data;
        this.isloading = false;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error('Error loading meeting', err);
        this.isloading = false;
        this.cdr.markForCheck();
      },
    });
  }

  joinMeeting(): void {
    if (!this.meeting?.meetLink) {
      return;
    }
    window.open(this.meeting.meetLink, '_blank', 'noopener,noreferrer');
  }

  openCalender(): void {
    if (!this.meeting?.htmlLink) {
      return;
    }
    window.open(this.meeting.htmlLink, '_blank', 'noopener,noreferrer');
  }

  goBack(): void {
    this.router.navigate(['/admin/meetings']);
  }
}
