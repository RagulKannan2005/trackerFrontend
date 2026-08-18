import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { TrackerSection } from "../models/trackersection.model";
import { TrackerSectionRequest } from "../models/section.model";

@Injectable({
    providedIn: 'root'
})
export class TrackerSectionService {
    private baseUrl = 'http://localhost:8081/api/v1/trackersection';
    
    constructor(private http: HttpClient) {}

    getSectionByTracker(trackerId: number) {
        return this.http.get<any[]>(
            `${this.baseUrl}/tracker/${trackerId}`
        );
    }

    getTrackerSectionById(id: number) {
        return this.http.get<any>(
            `${this.baseUrl}/${id}`
        );
    }

    assignSection(data: TrackerSectionRequest) {
        return this.http.post<any>(
            `${this.baseUrl}/assignSectiontoTracker`,
            data
        );
    }

    updateSectionOrder(id: number, data: TrackerSectionRequest) {
        return this.http.put<any>(
            `${this.baseUrl}/updatetrackersection/${id}`,
            data
        );
    }

    deleteSectionFromTracker(id: number) {
        return this.http.delete(
            `${this.baseUrl}/deletesectionfromtracker/${id}`
        );
    }

    deleteSectionByTrackerAndSection(trackerId: number, sectionId: number) {
        return this.http.delete(
            `${this.baseUrl}/tracker/${trackerId}/section/${sectionId}`
        );
    }
}