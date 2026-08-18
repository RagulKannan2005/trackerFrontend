import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { SectionSkillRequest, SectionSkillResponse } from '../models/section-skill.model';

@Injectable({
  providedIn: 'root',
})
export class SectionSkillService {
  private apiUrl = 'http://localhost:8081/api/v1/section-skills';

  constructor(private http: HttpClient) {}

  getAllSectionSkills(): Observable<SectionSkillResponse[]> {
    return this.http.get<SectionSkillResponse[]>(`${this.apiUrl}`);
  }

  getSkillsByTrackerSection(trackerSectionId: number): Observable<SectionSkillResponse[]> {
    return this.http.get<SectionSkillResponse[]>(`${this.apiUrl}/tracker-section/${trackerSectionId}`);
  }

  assignSkillToSection(request: SectionSkillRequest): Observable<SectionSkillResponse> {
    return this.http.post<SectionSkillResponse>(`${this.apiUrl}/create`, request);
  }

  updateSectionSkill(id: number, request: SectionSkillRequest): Observable<SectionSkillResponse> {
    return this.http.put<SectionSkillResponse>(`${this.apiUrl}/update/${id}`, request);
  }

  deleteSectionSkill(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/delete/${id}`);
  }
}
