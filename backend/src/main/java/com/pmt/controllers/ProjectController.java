package com.pmt.controllers;

import com.pmt.dto.ProjectWithRoleDTO;
import com.pmt.entities.Project;
import com.pmt.entities.ProjectUser;
import com.pmt.entities.User;
import com.pmt.repositories.ProjectRepository;
import com.pmt.repositories.ProjectUserRepository;
import com.pmt.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "*")
public class ProjectController {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectUserRepository projectUserRepository;

    public ProjectController(ProjectRepository projectRepository, UserRepository userRepository, ProjectUserRepository projectUserRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.projectUserRepository = projectUserRepository;
    }

    @GetMapping("/user/{email}")
    public ResponseEntity<List<ProjectWithRoleDTO>> getProjectsForUser(@PathVariable String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        List<ProjectUser> projectUsers = projectUserRepository.findByUser(user);
        List<ProjectWithRoleDTO> result = projectUsers.stream().map(pu -> {
            Project p = pu.getProject();
            return new ProjectWithRoleDTO(
                    p.getId(),
                    p.getName(),
                    p.getDescription(),
                    p.getStartDate(),
                    pu.getRole().name()
            );
        }).toList();

        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<?> createProject(@RequestBody Project project) {
        // 1. Créer le projet
        Project savedProject = projectRepository.save(project);

        // 2. Simuler l'utilisateur connecté (à adapter selon ton auth réelle)
        User creator = userRepository.findByEmail("valerie@example.com").orElse(null);
        if (creator == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Utilisateur non trouvé");
        }

        // 3. Créer le lien utilisateur-projet avec rôle ADMIN
        ProjectUser projectUser = new ProjectUser(creator, savedProject, ProjectUser.Role.ADMIN);
        projectUserRepository.save(projectUser);

        return ResponseEntity.ok(savedProject);
    }
}
