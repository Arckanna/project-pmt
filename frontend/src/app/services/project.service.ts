import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Project } from '../models/project.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProjectService {

  private apiUrl = 'http://localhost:8080/api/projects';

  constructor(private http: HttpClient) {}

  getProjects(): Observable<Project[]> {
    return this.http.get<Project[]>(this.apiUrl);
  }

  createProject(project: Project) {
    return this.http.post('http://localhost:8080/api/projects', project);
  }

  getProjectsByUser(email: string): Observable<Project[]> {
    return this.http.get<Project[]>(`http://localhost:8080/api/projects/user/${email}`);
  }

  getProjectById(id: number): Observable<Project> {
    return this.http.get<Project>(`http://localhost:8080/api/projects/${id}`);
  }
}
