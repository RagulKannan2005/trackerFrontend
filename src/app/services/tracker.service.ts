import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Tracker } from "../models/tracker.model";

@Injectable({
    providedIn:'root'
})

export class TrackerService{
    private apiurl='http://localhost:8081/api/v1/trackers'

    constructor(private http:HttpClient){}

    getAllTracker(){
        return this.http.get<Tracker[]>(this.apiurl)
    }
    createTracker(tracker:Tracker){
        return this.http.post<Tracker>(this.apiurl+'/addtracker',tracker)

    }

    
}