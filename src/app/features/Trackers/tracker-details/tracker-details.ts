import { Component, inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TrackerService } from '../../../services/tracker.service';
import { Tracker } from '../../../models/tracker.model';

@Component({
  selector: 'app-tracker-details',
  imports: [],
  templateUrl: './tracker-details.html',
  styleUrl: './tracker-details.css',
})
export class TrackerDetails {
  private route = inject(ActivatedRoute);
  private trackerService = inject(TrackerService);
  private router = inject(Router);

  tracker: Tracker | null = null;
  isLoading: boolean = true;

  ngOnInit() {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.loadTrackers(Number(idParam));
    }
  }
  loadTrackers(id: Number) {
    this.trackerService.getTrackerById(id).subscribe({
      next: (data) => {
        this.tracker = data;
        this.isLoading = false;
      },
      error: (err) => {
        console.error(err);
        this.isLoading = false;
      },
    });
  }

  goBack() {
    this.router.navigate(['/admin/trackers']);
  }
  
}
