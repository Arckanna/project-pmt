package com.pmt.entities;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @ManyToOne(optional = false)
    private Project project;

    public Task() {}

    public Task(String description, LocalDate createdDate, LocalDate dueDate, Project project) {
        this.description = description;
        this.createdDate = createdDate;
        this.dueDate = dueDate;
        this.project = project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Project getProject() {
        return project;
    }
}
