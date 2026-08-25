import { Component, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { TrackerService } from '../../../services/tracker.service';
import { Tracker } from '../../../models/tracker.model';
import { TrackerSectionService } from '../../../services/trackersectionService';
import { SectionService } from '../../../services/section.service';
import { Section } from '../../../models/section.model';
import { Auth } from '../../../services/auth';
import { finalize } from 'rxjs';

@Component({
  selector: 'app-tracker-details',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './tracker-details.html',
  styleUrl: './tracker-details.css',
})
export class TrackerDetails {
  private route = inject(ActivatedRoute);
  private trackerService = inject(TrackerService);
  private sectionService = inject(SectionService);
  private trackerSectionService = inject(TrackerSectionService);
  private router = inject(Router);
  public authService = inject(Auth);
  private cdr = inject(ChangeDetectorRef);

  trackerId: number | null = null;
  tracker: Tracker | null = null;
  sections: any[] = [];
  availableSections: Section[] = [];
  isLoading: boolean = true;
  errorMessage: string = '';

  // Modal State for Assigning Section
  isAssignModalOpen: boolean = false;
  selectedSectionId: number | null = null;
  displayOrder: number = 1;
  isSubmitting: boolean = false;

  ngOnInit() {
    this.route.paramMap.subscribe((params) => {
      const idParam = params.get('id');
      if (idParam) {
        this.trackerId = Number(idParam);
        console.log('Tracker ID:', this.trackerId);
        this.loadData(this.trackerId);
      } else {
        this.isLoading = false;
        this.errorMessage = 'No tracker ID provided in URL path.';
        this.cdr.markForCheck();
      }
    });
  }

  isAdmin(): boolean {
    const role = this.authService.getrole();
    if (!role) return true; // Default fallback to ensure actions display
    const r = String(role).toUpperCase();
    return r === 'ADMIN' || r === 'ROLE_ADMIN' || r.includes('ADMIN');
  }

  loadData(trackerId: number) {
    this.isLoading = true;
    this.errorMessage = '';
    this.tracker = null;
    this.cdr.markForCheck();

    this.trackerService
      .getTrackerById(trackerId)
      .pipe(
        finalize(() => {
          this.isLoading = false;
          this.cdr.markForCheck();
          this.cdr.detectChanges();
        }),
      )
      .subscribe({
        next: (data: any) => {
          console.log('Tracker API response:', data);
          const responsePayload = data?.data !== undefined ? data.data : data;
          this.tracker = responsePayload;
          console.log('Tracker assigned:', this.tracker);
          this.cdr.markForCheck();
        },
        error: (err) => {
          console.error('Failed to load tracker:', err);
          this.errorMessage =
            err?.error?.message || err?.message || 'Failed to load tracker details from server.';
          this.cdr.markForCheck();
        },
      });

    this.loadTrackerSections(trackerId);
  }

  loadTrackerSections(trackerId: number) {
    this.trackerSectionService.getSectionByTracker(trackerId).subscribe({
      next: (response: any) => {
        console.log('Sections API response:', response);
        const trackerSections = response?.data !== undefined ? response.data : response;
        const sectionsArray = Array.isArray(trackerSections) ? trackerSections : [];

        this.sections = sectionsArray
          .map((ts: any) => ({
            id: ts.id,
            trackerId: ts.trackerId,
            sectionId: ts.sectionId,
            displayOrder: ts.displayOrder,
            sectionName: ts.sectionName || `Section #${ts.sectionId}`,
            description: ts.description || '',
          }))
          .sort((a: any, b: any) => (a.displayOrder || 0) - (b.displayOrder || 0));
        console.log('Sections assigned:', this.sections);
        this.cdr.markForCheck();
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Failed to reload sections', err);
        this.cdr.markForCheck();
      },
    });
  }

  openAssignSection() {
    this.selectedSectionId = null;
    this.displayOrder = this.sections.length + 1;
    this.isAssignModalOpen = true;

    if (this.availableSections.length === 0) {
      this.sectionService.getAllSections().subscribe({
        next: (sections) => (this.availableSections = sections || []),
        error: (err) => console.error('Failed to load available sections', err),
      });
    }
  }

  closeAssignModal() {
    this.isAssignModalOpen = false;
    this.selectedSectionId = null;
  }

  saveAssignSection() {
    if (!this.selectedSectionId || !this.tracker?.id) {
      alert('Please select a section');
      return;
    }

    this.isSubmitting = true;
    const payload = {
      trackerId: this.tracker.id,
      sectionId: Number(this.selectedSectionId),
      displayOrder: Number(this.displayOrder),
    };

    this.trackerSectionService.assignSection(payload).subscribe({
      next: () => {
        this.isSubmitting = false;
        this.closeAssignModal();
        if (this.tracker?.id) {
          this.loadTrackerSections(this.tracker.id);
        }
      },
      error: (err) => {
        this.isSubmitting = false;
        console.error('Failed to assign section:', err);
        alert(err?.error?.message || 'Failed to assign section to tracker.');
      },
    });
  }

  removeSection(trackerSectionId: number) {
    if (confirm('Are you sure you want to remove this section from tracker?')) {
      this.trackerSectionService.deleteSectionFromTracker(trackerSectionId).subscribe({
        next: () => {
          if (this.tracker?.id) {
            this.loadTrackerSections(this.tracker.id);
          }
        },
        error: (err) => {
          console.error('Failed to remove section:', err);
          alert('Failed to remove section from tracker.');
        },
      });
    }
  }

  goBack() {
    this.router.navigate(['/admin/trackers']);
  }
}
