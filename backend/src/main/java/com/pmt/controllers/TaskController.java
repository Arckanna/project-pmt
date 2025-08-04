package com.pmt.controllers;

import com.pmt.entities.Project;
import com.pmt.entities.Task;
import com.pmt.entities.TaskHistory;
import com.pmt.entities.User;
import com.pmt.repositories.ProjectRepository;
import com.pmt.repositories.TaskHistoryRepository;
import com.pmt.repositories.TaskRepository;
import com.pmt.repositories.UserRepository;
import com.pmt.services.EmailNotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur exposant les points d’accès liés à la gestion des tâches.
 */
@RestController
@CrossOrigin(origins = "*")
public class TaskController {


    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final EmailNotificationService emailNotificationService;
    private final TaskHistoryRepository taskHistoryRepository;

    public TaskController(ProjectRepository projectRepository, TaskRepository taskRepository, UserRepository userRepository, EmailNotificationService emailNotificationService, TaskHistoryRepository taskHistoryRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.emailNotificationService = emailNotificationService;
        this.taskHistoryRepository = taskHistoryRepository;
    }

    /**
     * Récupére toutes les tâches associées à un projet donné.
     *
     * @param projectId: identifiant du projet.
     * @return: 200 avec la liste des tâches ou 404 si le projet est introuvable.
     */
    @GetMapping("/api/projects/{projectId}/tasks")
    public ResponseEntity<List<Task>> getTasksForProject(@PathVariable Long projectId) {
        Project project = projectRepository.findById(projectId).orElse(null);
        if (project == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(taskRepository.findByProject(project));
    }

    /**
     * Récupére les entrées d'historique d'une tâche spécifique.
     *
     * @param taskId: identifiant de la tâche.
     * @return 200 avec l'historique ou 404 si la tâche n'existe pas.
     */
    @GetMapping("/api/tasks/{taskId}/history")
    public ResponseEntity<List<TaskHistory>> getTaskHistory(@PathVariable Long taskId) {
        Task task = taskRepository.findById(taskId).orElse(null);
        if (task == null) {
            return ResponseEntity.notFound().build();
        }
        List<TaskHistory> historyList = taskHistoryRepository.findByTask(task);
        return ResponseEntity.ok(historyList);
    }

    /**
     * Crée une tâche pour un projet et éventuellement l'attribuer à un utilisateur.
     *
     * @param projectId: identifiant du projet.
     * @param task: détails de la tâche, y compris l'utilisateur assigné (facultatif) et la date d'échéance.
     * @return: 200 avec la tâche créée, ou 400 si les validations échouent ou si le projet est introuvable.
     */
    @PostMapping("/api/projects/{projectId}/tasks")
    public ResponseEntity<?> createTask(@PathVariable Long projectId, @RequestBody Task task) {
        Project project = projectRepository.findById(projectId).orElse(null);
        if (project == null) {
            return ResponseEntity.badRequest().body("Projet introuvable");
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

        // Envoi d'email si l'utilisateur est assigné
        if (task.getAssignedTo() != null) {
            emailNotificationService.sendTaskAssignmentNotification(
                    task.getAssignedTo().getEmail(),
                    task.getDescription(),
                    "Projet ID: " + project.getId()
            );
        }

        // Enregistrement dans l'historique
        String performedBy = task.getAssignedTo() != null ? task.getAssignedTo().getEmail() : "système";
        TaskHistory history = new TaskHistory(saved, "Création de tâche", performedBy);
        taskHistoryRepository.save(history);
        return ResponseEntity.ok(saved);
    }
}
