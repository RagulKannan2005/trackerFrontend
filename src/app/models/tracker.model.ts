export interface Tracker {
  id?: number;
  trackerName: string;
  description: string;
  active: boolean;
  createdDate?: string;
  updatedDate?: string;
}

export interface TrackerRequest {
  trackerName: string;
  description: string;
  active: boolean;
}
