import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { TrackerSectionRequest, TrackerSectionResponse } from "../models/section.model";

@Injectable({
    providedIn:'root'
})
export class TrackerSectionService{

    private apiurl='http://localhost:8081/api/v1/trackersection';

    constructor(private http:HttpClient){}


    getSectionByTracker(trackerId:number):Observable<TrackerSectionResponse[]>
    {
        return this.http.get<TrackerSectionResponse[]>(`${this.apiurl}/tracker/${trackerId}`);
    }
    assignSection(data:TrackerSectionRequest){
        return this.http.post<TrackerSectionResponse>(`${this.apiurl}/assignSectiontoTracker`,data);
    }
    updateTrackerSection(id:number,trackersection:TrackerSectionRequest){
        return this.http.put<TrackerSectionResponse>(`${this.apiurl}/updateTrackerSection/${id}`,trackersection);
    }
    deleteTrackerSection(id:number){
        return this.http.delete(`${this.apiurl}/deleteTrackerSection/${id}`);
    }
}