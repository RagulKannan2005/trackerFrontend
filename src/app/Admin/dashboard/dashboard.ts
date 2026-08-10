import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  imports: [RouterLink, RouterLinkActive, RouterOutlet],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
})
export class Dashboard {

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



