import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ɵInternalFormsSharedModule } from '@angular/forms';
import { GoogleMeetService } from '../../../services/google-meet.service';
import { Router } from '@angular/router';
import { MeetingRequest } from '../../../models/meeting.model';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-meeting-form',
  imports: [FormsModule, ReactiveFormsModule,CommonModule],
  templateUrl: './meeting-form.html',
  styleUrl: './meeting-form.css',
})
export class MeetingForm implements OnInit {
  meetingForm!: FormGroup;

  isLoading = false;
  errormessage = '';

  constructor(
    private fb: FormBuilder,
    private googlemeetService: GoogleMeetService,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.meetingForm = this.fb.group({
      summary: ['', [Validators.required]],
      description: ['', [Validators.required]],
      startDateTime: ['', [Validators.required]],
      endDateTime: ['', [Validators.required]],
      attendeeEmails: ['', [Validators.required]],
    });
  }

  createMeeting(): void {
    if (this.meetingForm.invalid) {
      this.meetingForm.markAllAsTouched();
      return;
    }
    const form = this.meetingForm.value;

    const formatIsoString = (val: string) => {
      if (!val) return '';
      return val.length === 16 ? `${val}:00` : val;
    };

    const emails = form.attendeeEmails
      ? form.attendeeEmails
          .split(',')
          .map((email: string) => email.trim())
          .filter((email: string) => email.length > 0)
      : [];

    const request: MeetingRequest = {
      summary: form.summary,
      description: form.description,
      startDateTime: formatIsoString(form.startDateTime),
      endDateTime: formatIsoString(form.endDateTime),
      attendeeEmails: emails,
    };
    this.isLoading = true;
    this.errormessage = '';

    this.googlemeetService.createMeeting(request).subscribe({
      next: (response) => {
        this.isLoading = false;
        this.router.navigate(['/admin/meetings'], {
          queryParams: { message: 'Meeting created successfully' },
        });
      },
      error: (error) => {
        this.isLoading = false;
        this.errormessage = error.error || 'Failed to create meeting';
        this.router.navigate(['/admin/meetings'], { queryParams: { error: this.errormessage } });
      },
    });
  }

  cancel(): void {
    this.router.navigate(['/admin/meetings']);
  }
}
