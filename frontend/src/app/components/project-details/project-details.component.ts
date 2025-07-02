import {Component, OnInit} from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import {Project} from "../../models/project.model";
import { ProjectService } from '../../services/project.service';
import { TaskService } from '../../services/task.service';
import { Task } from '../../models/task.model';
import {ProjectUserService} from "../../services/project-user.service";
import { ProjectMember } from '../../models/project-member.model';
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-project-details',
  templateUrl: './project-details.component.html',
  styleUrl: './project-details.component.scss'
})
export class ProjectDetailsComponent implements OnInit {
  projectId!: number;

  newTask: Task = {
    description: '',
    createdDate: '',
    dueDate: '',
    assignedTo: { email: '' }
  };

  showTaskForm = false;

  memberToAdd = {
    email: '',
    role: 'MEMBER'  // valeur par défaut
  };

  showMemberForm = false;

  historyMap: { [taskId: number]: any[] } = {};

  constructor(
    private route: ActivatedRoute,
    private projectService: ProjectService,
    private taskService: TaskService,
    private projectUserService: ProjectUserService,
    private http: HttpClient
  ) {}

  project: Project | null = null;
  tasks: Task[] = [];
  members: ProjectMember[] = [];

  loadHistory(taskId: number) {
    if (this.historyMap[taskId]) return; // ne recharge pas si déjà chargé
    this.http.get<any[]>(`/api/tasks/${taskId}/history`).subscribe({
      next: (data) => this.historyMap[taskId] = data,
      error: (err) => console.error('Erreur chargement historique', err)
    });
  }

  ngOnInit(): void {
    this.projectId = Number(this.route.snapshot.paramMap.get('id'));

    this.projectService.getProjectWithRole(this.projectId, 'valerie@example.com').subscribe({
      next: data => {
        this.project = data;
        console.log("Projet avec rôle :", this.project);
      }
    });

    this.taskService.getTasksByProject(this.projectId).subscribe({
      next: data => this.tasks = data,
      error: err => console.error('Erreur chargement tâches', err)
    });

    this.projectUserService.getMembers(this.projectId).subscribe({
      next: (data) => this.members = data,
      error: (err) => console.error('Erreur chargement membres', err)
    });

  }

  createTask() {
    console.log("👉 Formulaire soumis", this.newTask); // 🔍 TEST
    const taskToCreate = { ...this.newTask, createdDate: new Date().toISOString().split('T')[0] };

    this.taskService.createTask(this.projectId, taskToCreate).subscribe({
      next: () => {
        this.tasks.push(taskToCreate);
        this.newTask = { description: '', createdDate: '', dueDate: '' ,assignedTo:{email:''}};
        this.showTaskForm = false;
      },
      error: (err) => console.error('Erreur création tâche', err)
    });
  }

  addMember() {
    this.projectUserService.addMember(this.projectId, this.memberToAdd).subscribe({
      next: () => {
        alert('✅ Membre ajouté avec succès');
        this.memberToAdd = { email: '', role: 'MEMBER' };
        this.showMemberForm = false;
      },
      error: () => {
        alert('❌ Erreur lors de l’ajout du membre');
      }
    });
  }
}
