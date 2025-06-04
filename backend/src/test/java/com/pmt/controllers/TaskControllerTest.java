package com.pmt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pmt.entities.Project;
import com.pmt.entities.Task;
import com.pmt.entities.User;
import com.pmt.repositories.ProjectRepository;
import com.pmt.repositories.TaskRepository;
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

@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskRepository taskRepository;

    @MockBean
    private ProjectRepository projectRepository;

    @MockBean
    private UserRepository userRepository;

    private ObjectMapper objectMapper;
    private Project project;
    private User user;
    private Task task;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        project = new Project();
        project.setId(1L);
        project.setName("Test Project");

        user = new User();
        user.setEmail("valerie@example.com");

        task = new Task();
        task.setId(1L);
        task.setDescription("Task 1");
        task.setCreatedDate(LocalDate.now());
        task.setDueDate(LocalDate.now().plusDays(5));
        task.setProject(project);
        task.setAssignedTo(user);
    }

    @Test
    void getTasks_shouldReturnTasksForProject() throws Exception {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(taskRepository.findByProject(project)).thenReturn(List.of(task));

        mockMvc.perform(get("/api/projects/1/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value("Task 1"));
    }

    @Test
    void getTasks_shouldReturn404_ifProjectNotFound() throws Exception {
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/projects/1/tasks"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createTask_shouldSucceedWithAssignedUser() throws Exception {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(userRepository.findByEmail("valerie@example.com")).thenReturn(Optional.of(user));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        String json = objectMapper.writeValueAsString(task);

        mockMvc.perform(post("/api/projects/1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Task 1"));
    }

    @Test
    void createTask_shouldSucceedWithoutAssignedUser() throws Exception {
        Task unassignedTask = new Task();
        unassignedTask.setDescription("Task no user");
        unassignedTask.setCreatedDate(LocalDate.now());
        unassignedTask.setDueDate(LocalDate.now().plusDays(3));
        unassignedTask.setProject(project);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(taskRepository.save(any(Task.class))).thenReturn(unassignedTask);

        String json = objectMapper.writeValueAsString(unassignedTask);

        mockMvc.perform(post("/api/projects/1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Task no user"));
    }

    @Test
    void createTask_shouldReturnBadRequest_ifProjectNotFound() throws Exception {
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        String json = objectMapper.writeValueAsString(task);

        mockMvc.perform(post("/api/projects/1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createTask_shouldReturnBadRequest_ifAssignedUserNotFound() throws Exception {
        task.setAssignedTo(new User());
        task.getAssignedTo().setEmail("unknown@example.com");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        String json = objectMapper.writeValueAsString(task);

        mockMvc.perform(post("/api/projects/1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createTask_shouldReturnBadRequest_ifDueDateBeforeCreatedDate() throws Exception {
        Task invalidTask = new Task();
        invalidTask.setDescription("Invalid Task");
        invalidTask.setCreatedDate(LocalDate.now());
        invalidTask.setDueDate(LocalDate.now().minusDays(1));
        invalidTask.setProject(project);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        String json = objectMapper.writeValueAsString(invalidTask);

        mockMvc.perform(post("/api/projects/1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }
}
