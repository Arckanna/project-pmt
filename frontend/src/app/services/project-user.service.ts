import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {ProjectMember} from "../models/project-member.model";

@Injectable({
  providedIn: 'root'
})
export class ProjectUserService {
  private baseUrl = 'http://localhost:8080/api/projects';

  constructor(private http: HttpClient) {}

  addMember(projectId: number, data: { email: string; role: string }): Observable<any> {
    return this.http.post(`${this.baseUrl}/${projectId}/members`, data, { responseType: 'text' });
  }

  getMembers(projectId: number): Observable<ProjectMember[]> {
    console.log('on y est');
    return this.http.get<ProjectMember[]>(`http://localhost:8080/api/projects/${projectId}/members`);
  }
}
