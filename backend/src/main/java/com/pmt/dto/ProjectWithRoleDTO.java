package com.pmt.dto;

import java.time.LocalDate;

public class ProjectWithRoleDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDate startDate;
    private String role;

    public ProjectWithRoleDTO(Long id, String name, String description, LocalDate startDate, String role) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.role = role;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public LocalDate getStartDate() { return startDate; }
    public String getRole() { return role; }
}
