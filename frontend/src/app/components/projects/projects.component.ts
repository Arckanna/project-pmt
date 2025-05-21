import { Component, OnInit } from '@angular/core';
import { Project } from '../../models/project.model';
import { ProjectService } from '../../services/project.service';

@Component({
  selector: 'app-projects',
  templateUrl: './projects.component.html'
})
export class ProjectsComponent implements OnInit {
  projects: Project[] = [];

  constructor(private projectService: ProjectService) {}

  ngOnInit(): void {
    const email = 'valerie@example.com'; // temporairement en dur, à remplacer par localStorage plus tard

    this.projectService.getProjectsByUser(email).subscribe({
      next: (data) => this.projects = data,
      error: (err) => console.error('Erreur chargement projets', err)
    });
  }
}
