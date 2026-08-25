import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Section, SectionRequest } from '../models/section.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class SectionService {
  private apiurl = 'http://localhost:8081/api/v1/section';

  constructor(private http: HttpClient) {}

  getAllSections() {
    return this.http.get<Section[]>(`${this.apiurl}/allsection`);
  }

  addSection(section: SectionRequest): Observable<Section> {
    return this.http.post<Section>(`${this.apiurl}/addSection`, section);
  }

  updateSection(id: number, section: SectionRequest): Observable<Section> {
    return this.http.put<Section>(`${this.apiurl}/updateSection/${id}`, section);
  }

  deleteSection(id: number) {
    return this.http.delete<void>(`${this.apiurl}/deleteSection/${id}`);
  }

  getSectionById(id:number){
    return this.http.get<Section>(`${this.apiurl}/getSection/${id}`)
  }
}
