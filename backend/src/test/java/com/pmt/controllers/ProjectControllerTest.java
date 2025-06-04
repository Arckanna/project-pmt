package com.pmt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pmt.entities.Project;
import com.pmt.entities.ProjectUser;
import com.pmt.entities.User;
import com.pmt.repositories.ProjectRepository;
import com.pmt.repositories.ProjectUserRepository;
import com.pmt.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProjectController.class)
public class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectRepository projectRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ProjectUserRepository projectUserRepository;

    private ObjectMapper objectMapper;
    private Project sampleProject;
    private User sampleUser;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        sampleProject = new Project();
        sampleProject.setId(1L);
        sampleProject.setName("Test Project");
        sampleProject.setDescription("Test Desc");
        sampleProject.setStartDate(LocalDate.now());

        sampleUser = new User();
        sampleUser.setEmail("valerie@example.com");
    }

    @Test
    void getProjectById_shouldReturnProject_ifExists() throws Exception {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(sampleProject));

        mockMvc.perform(get("/api/projects/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Project"));
    }

    @Test
    void getProjectById_shouldReturn404_ifNotFound() throws Exception {
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/projects/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createProject_shouldCreateAndReturnProject() throws Exception {
        when(userRepository.findByEmail("valerie@example.com")).thenReturn(Optional.of(sampleUser));
        when(projectRepository.save(any(Project.class))).thenReturn(sampleProject);

        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleProject)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Project"));
    }

    @Test
    void createProject_shouldReturnBadRequest_ifUserNotFound() throws Exception {
        when(userRepository.findByEmail("valerie@example.com")).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleProject)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getProjectMembers_shouldReturnListOfMembers() throws Exception {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(sampleProject));
        ProjectUser pu = new ProjectUser(sampleUser, sampleProject, ProjectUser.Role.MEMBER);
        when(projectUserRepository.findByProject(sampleProject)).thenReturn(List.of(pu));

        mockMvc.perform(get("/api/projects/1/members"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].user.email").value("valerie@example.com"));
    }

    @Test
    void getProjectMembers_shouldReturn404_ifProjectNotFound() throws Exception {
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/projects/1/members"))
                .andExpect(status().isNotFound());
    }

    @Test
    void addMember_shouldSucceed_whenUserAndProjectExist() throws Exception {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(sampleProject));
        when(userRepository.findByEmail("valerie@example.com")).thenReturn(Optional.of(sampleUser));

        String json = "{\"email\":\"valerie@example.com\",\"role\":\"MEMBER\"}";

        mockMvc.perform(post("/api/projects/1/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string("Utilisateur ajouté avec succès."));
    }

    @Test
    void addMember_shouldFail_ifUserOrProjectNotFound() throws Exception {
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        String json = "{\"email\":\"valerie@example.com\",\"role\":\"MEMBER\"}";

        mockMvc.perform(post("/api/projects/1/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }
}
