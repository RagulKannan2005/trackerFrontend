import { Component, EventEmitter, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-tracker-filter',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './tracker-filter.html',
  styleUrl: './tracker-filter.css',
})
export class TrackerFilter {
  searchTerm: string = '';
  statusFilter: string = 'ALL'; // 'ALL' | 'ACTIVE' | 'INACTIVE'

  @Output() filterChange = new EventEmitter<{ searchTerm: string; statusFilter: string }>();

  onSearchChange() {
    this.emitFilters();
  }

  onStatusChange(status: string) {
    this.statusFilter = status;
    this.emitFilters();
  }

  private emitFilters() {
    this.filterChange.emit({
      searchTerm: this.searchTerm,
      statusFilter: this.statusFilter,
    });
  }
}
