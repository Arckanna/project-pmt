import { Component } from '@angular/core';
import { ProjectService } from '../../services/project.service';
import { Project } from '../../models/project.model';

@Component({
  selector: 'app-project-form',
  templateUrl: './project-form.component.html'
})
export class ProjectFormComponent {
  project: Project = { name: '', description: '',startDate: '',
    role: '' };
  success = false;

  constructor(private projectService: ProjectService) {}

  onSubmit() {
    this.projectService.createProject(this.project).subscribe({
      next: () => {
        this.success = true;
        this.project = { name: '', description: '' ,startDate: '',
          role: ''};
      },
      error: (err) => console.error('Erreur création projet', err)
    });
  }
  newProject() {
    this.success = false;
    this.project = { name: '', description: '',startDate: '',
      role: '' };
  }
}
