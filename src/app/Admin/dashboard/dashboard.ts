import { Component, inject } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';

import { Auth } from '../../services/auth';

@Component({
  selector: 'app-dashboard',
  imports: [RouterLink, RouterLinkActive, RouterOutlet],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
})
export class Dashboard {
  Authservice = inject(Auth);

  isTrackerOpen: boolean = false;
  selectedTracker: string = '';

  toggleTrackerMenu(event?: Event) {
    if (event) {
      event.preventDefault();
    }
    this.isTrackerOpen = !this.isTrackerOpen;
  }

  selectTracker(tracker: string) {
    this.selectedTracker = tracker;
  }
}
