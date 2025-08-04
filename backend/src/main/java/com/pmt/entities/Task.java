package com.pmt.entities;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * Représente une tâche appartenant à un projet.
 * Les tâches peuvent éventuellement être attribuées à un utilisateur et suivre leurs dates de création et d'échéance.
 */
@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @ManyToOne
    private User assignedTo;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @ManyToOne(optional = false)
    private Project project;

    public User getAssignedTo() { return assignedTo; }
    public void setAssignedTo(User assignedTo) { this.assignedTo = assignedTo; }

    public Task() {}

    public Task(String description, LocalDate createdDate, LocalDate dueDate, Project project, User assignedTo) {
        this.description = description;
        this.createdDate = createdDate;
        this.dueDate = dueDate;
        this.project = project;
        this.assignedTo = assignedTo;
    }

    public void setProject(Project project) {
        this.project = project;
    }
    public Project getProject() {
        return project;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }
    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
    public LocalDate getDueDate() {
        return dueDate;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", createdDate=" + createdDate +
                ", dueDate=" + dueDate +
                ", assignedTo=" + (assignedTo != null ? assignedTo.getEmail() : "null") +
                '}';
    }
}
