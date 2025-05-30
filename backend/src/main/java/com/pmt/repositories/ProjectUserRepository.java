package com.pmt.repositories;

import com.pmt.entities.Project;
import com.pmt.entities.ProjectUser;
import com.pmt.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectUserRepository extends JpaRepository<ProjectUser, Long> {
    List<ProjectUser> findByUser(User user);

    List<ProjectUser> findByProject(Project project);
    Optional<ProjectUser> findByProjectAndUser(Project project, User user);
}
