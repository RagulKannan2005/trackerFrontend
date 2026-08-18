import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Skill, SkillsRequest } from "../models/skill.model";
import { Observable } from "rxjs";

@Injectable({
    providedIn:'root'
})
export class skillsService{

    private apiurl='http://localhost:8081/api/v1/skills'

    constructor(private http:HttpClient){}

    getAllSkills(){
        return this.http.get<Skill[]>(
            `${this.apiurl}/allSkills`
        )
    }

    addSkill(skill:SkillsRequest):Observable<Skill>{
        return this.http.post<Skill>(`${this.apiurl}/addSkill`,skill)
    }
    updateSkill(id:number,skill:SkillsRequest):Observable<Skill>{
        return this.http.put<Skill>(`${this.apiurl}/updateSkill/${id}`,skill)
    }

    deleteSkill(id:number):Observable<void>{
        return this.http.delete<void>(`${this.apiurl}/deleteSkill/${id}`)
    }
    getSkillById(id:number):Observable<Skill>{
        return this.http.get<Skill>(`${this.apiurl}/getSkill/${id}`)
    }


}