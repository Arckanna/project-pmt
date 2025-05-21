package com.pmt.repositories;

import com.pmt.entities.Task;
import com.pmt.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByProject(Project project);
}
