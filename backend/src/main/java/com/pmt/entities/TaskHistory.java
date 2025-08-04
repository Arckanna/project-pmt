package com.pmt.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
/**
 * Enregistre une action effectuée sur une tâche {@link} à des fins d'audit.
 */
@Entity
public class TaskHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Task task;

    private String action; // Exemple : "création", "modification", "assignation"

    private LocalDateTime timestamp;

    private String performedBy; // Email ou nom d'utilisateur

    public TaskHistory() {}

    public TaskHistory(Task task, String action, String performedBy) {
        this.task = task;
        this.action = action;
        this.timestamp = LocalDateTime.now();
        this.performedBy = performedBy;
    }

    // Getters & setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Task getTask() { return task; }
    public void setTask(Task task) { this.task = task; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getPerformedBy() { return performedBy; }
    public void setPerformedBy(String performedBy) { this.performedBy = performedBy; }
}
