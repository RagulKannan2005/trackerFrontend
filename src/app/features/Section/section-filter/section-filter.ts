import { Component, EventEmitter, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-section-filter',
  imports: [FormsModule],
  templateUrl: './section-filter.html',
  styleUrl: './section-filter.css',
})
export class SectionFilter {
  searchTerm: string = '';
  statusFilter: string = 'ALL';

  @Output() filterChange = new EventEmitter<{
    searchTerm: string;
    statusFilter: string;
  }>();

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
