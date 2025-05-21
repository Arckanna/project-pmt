package com.pmt.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "project_users")
public class ProjectUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "project_id")
    private Project project;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    public enum Role {
        ADMIN,
        MEMBER,
        OBSERVER
    }

    public ProjectUser() {}

    public ProjectUser(User user, Project project, Role role) {
        this.user = user;
        this.project = project;
        this.role = role;
    }

    public Long getId() { return id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}
