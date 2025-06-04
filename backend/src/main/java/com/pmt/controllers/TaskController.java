package com.pmt.controllers;

import com.pmt.entities.Project;
import com.pmt.entities.Task;
import com.pmt.entities.User;
import com.pmt.repositories.ProjectRepository;
import com.pmt.repositories.ProjectUserRepository;
import com.pmt.repositories.TaskRepository;
import com.pmt.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin(origins = "*")
public class TaskController {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskController(ProjectRepository projectRepository, TaskRepository taskRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/api/projects/{projectId}/tasks")
    public ResponseEntity<List<Task>> getTasksForProject(@PathVariable Long projectId) {
        Project project = projectRepository.findById(projectId).orElse(null);
        if (project == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(taskRepository.findByProject(project));
    }

    @PostMapping("/api/projects/{projectId}/tasks")
    public ResponseEntity<?> createTask(@PathVariable Long projectId, @RequestBody Task task) {
        Project project = projectRepository.findById(projectId).orElse(null);
        if (project == null) {
            return ResponseEntity.badRequest().body("Projet introuvable"); // ou ResponseEntity.notFound().build()
        }

        task.setProject(project);

        if (task.getAssignedTo() != null && task.getAssignedTo().getEmail() != null) {
            String email = task.getAssignedTo().getEmail();
            User user = userRepository.findByEmail(email).orElse(null);
            if (user == null) {
                return ResponseEntity.badRequest().body("Utilisateur assigné introuvable");
            }
            task.setAssignedTo(user);
        }

        if (task.getDueDate() != null && task.getCreatedDate() != null &&
                task.getDueDate().isBefore(task.getCreatedDate())) {
            return ResponseEntity.badRequest().body("La date limite est antérieure à la date de création");
        }

        Task saved = taskRepository.save(task);
        return ResponseEntity.ok(saved);
    }
}

