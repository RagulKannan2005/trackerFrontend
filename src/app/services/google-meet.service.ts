import { HttpBackend, HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Meeting,MeetingRequest } from "../models/meeting.model";



@Injectable({
    providedIn:'root'
})
export class GoogleMeetService{

    private apiurl='http://localhost:8081/api/v1/google-meet'

    constructor(private http:HttpClient){}

    createMeeting (request:MeetingRequest){
        return this.http.post<Meeting>(`${this.apiurl}/create`,request);
    }

    getAllMeetings(){
        return this.http.get<Meeting[]>(`${this.apiurl}/all`);
    }

    getMeetingById(id:number){
        return this.http.get<Meeting>(`${this.apiurl}/${id}`);
    }

    updateMeeting(id:number,request:MeetingRequest){
        return this.http.put<Meeting>(`${this.apiurl}/updateMeet/${id}`,request);
    }

    deleteMeeting(id:number){
        return this.http.delete(`${this.apiurl}/deleteMeet/${id}`);
    }
}