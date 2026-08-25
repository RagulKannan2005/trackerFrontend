export interface TrackerSection {
  id: number;
  trackerId: number;
  sectionId: number;
  displayOrder: number;
  createdAt: Date;
  updatedAt: Date;
}

export interface TrackerRequest {
  trackerId: number;
  sectionId: number;
  displayOrder: number;
}
