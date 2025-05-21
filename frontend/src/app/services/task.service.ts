import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Task } from '../models/task.model';
import { Observable } from 'rxjs';
import {Project} from "../models/project.model";

@Injectable({
  providedIn: 'root'
})
export class TaskService {
  private baseUrl = 'http://localhost:8080/api/projects';

  constructor(private http: HttpClient) {}

  getTasksByProject(projectId: number): Observable<Task[]> {
    return this.http.get<Task[]>(`${this.baseUrl}/${projectId}/tasks`);
  }

  createTask(projectId: number,task: Task) {
    return this.http.post(`http://localhost:8080/api/projects/${projectId}/tasks`, task);
  }
}
