import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { Tracker, TrackerRequest } from "../models/tracker.model";
import { Observable } from "rxjs";

@Injectable({
    providedIn: 'root'
})

export class TrackerService {

    private http=inject(HttpClient);
    private apiurl = 'http://localhost:8081/api/v1/trackers';

    getAllTracker():Observable<Tracker[]> {
        return this.http.get<Tracker[]>(`${this.apiurl}/gettrackers`);
    }

    createTracker(tracker: TrackerRequest): Observable<Tracker> {
        return this.http.post<Tracker>(`${this.apiurl}/addtracker`, tracker);
    }

    getTrackerById(id: Number): Observable<Tracker> {
        return this.http.get<Tracker>(`${this.apiurl}/gettracker/${id}`);
    }

    updateTracker(id: number, tracker: TrackerRequest):Observable<Tracker> {
        return this.http.put<Tracker>(`${this.apiurl}/updatetracker/${id}`, tracker);
    }

    deleteTracker(id: number): Observable<Tracker> {
        return this.http.delete<Tracker>(`${this.apiurl}/deletetracker/${id}`);
    }
}
