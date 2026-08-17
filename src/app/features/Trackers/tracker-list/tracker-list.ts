import { Component, OnInit, inject } from '@angular/core';
import { Router } from '@angular/router';
import { TrackerService } from '../../../services/tracker.service';
import { Auth } from '../../../services/auth';
import { Tracker, TrackerRequest } from '../../../models/tracker.model';
import { TrackerFilter } from '../tracker-filter/tracker-filter';
import { TrackerForm } from '../tracker-form/tracker-form';

@Component({
  selector: 'app-tracker-list',
  standalone: true,
  imports: [TrackerFilter, TrackerForm],
  templateUrl: './tracker-list.html',
  styleUrl: './tracker-list.css',
})
export class TrackerList implements OnInit {
  private trackerService = inject(TrackerService);
  public authService = inject(Auth);
  private router = inject(Router);

  trackers: Tracker[] = [];
  filteredTrackers: Tracker[] = [];
  isLoading: boolean = false;
  errorMessage: string = '';

  // Modal State
  isModalOpen: boolean = false;
  isEditMode: boolean = false;
  selectedTracker: Tracker | null = null;

  ngOnInit() {
    this.loadTrackers();
  }

  isAdmin(): boolean {
    return this.authService.getrole() === 'ADMIN';
  }

  loadTrackers() {
    this.isLoading = true;
    this.trackerService.getAllTracker().subscribe({
      next: (data) => {
        this.trackers = data;
        this.filteredTrackers = data;
        this.isLoading = false;
      },
      error: (err) => {
        this.errorMessage = 'Failed to load trackers from server.';
        this.isLoading = false;
        console.error(err);
      },
    });
  }

  onFilterChange(filters: { searchTerm: string; statusFilter: string }) {
    let result = this.trackers;

    if (filters.searchTerm.trim()) {
      const term = filters.searchTerm.toLowerCase();
      result = result.filter(
        (t) =>
          t.trackerName.toLowerCase().includes(term) ||
          t.description.toLowerCase().includes(term)
      );
    }

    if (filters.statusFilter === 'ACTIVE') {
      result = result.filter((t) => t.active);
    } else if (filters.statusFilter === 'INACTIVE') {
      result = result.filter((t) => !t.active);
    }

    this.filteredTrackers = result;
  }

  openAddModal() {
    this.selectedTracker = null;
    this.isEditMode = false;
    this.isModalOpen = true;
  }

  openEditModal(tracker: Tracker) {
    this.selectedTracker = tracker;
    this.isEditMode = true;
    this.isModalOpen = true;
  }

  closeModal() {
    this.isModalOpen = false;
    this.selectedTracker = null;
  }

  handleFormSave(formData: TrackerRequest) {
    if (this.isEditMode && this.selectedTracker?.id) {
      this.trackerService.updateTracker(this.selectedTracker.id, formData).subscribe({
        next: () => {
          this.closeModal();
          this.loadTrackers();
        },
        error: (err) => alert('Failed to update tracker.'),
      });
    } else {
      this.trackerService.createTracker(formData).subscribe({
        next: () => {
          this.closeModal();
          this.loadTrackers();
        },
        error: (err) => alert('Failed to create tracker.'),
      });
    }
  }

  deleteTracker(id: number) {
    if (confirm('Are you sure you want to delete this tracker?')) {
      this.trackerService.deleteTracker(id).subscribe({
        next: () => this.loadTrackers(),
        error: (err) => alert('Failed to delete tracker.'),
      });
    }
  }

  viewDetails(id: number) {
    this.router.navigate(['/admin/trackers', id]);
  }

  get activeCount(): number {
    return this.trackers.filter((t) => t.active).length;
  }

  get inactiveCount(): number {
    return this.trackers.filter((t) => !t.active).length;
  }
}
