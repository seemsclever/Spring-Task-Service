package com.seemsclever.ports.controllers;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.seemsclever.entities.TaskStatus;
import com.seemsclever.ports.controllers.dto.TaskRequest;
import com.seemsclever.ports.controllers.dto.TaskResponse;
import com.seemsclever.services.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    Instant now = Instant.now();

    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void testGetAllTasks() throws Exception {
                List<TaskResponse> mockTaskResponses = List.of(
                new TaskResponse(1L, "Задание 1", "Тапшырма 1", "Описание 1", now, now, now, now, now, TaskStatus.PENDING, 123L),
                new TaskResponse(2L, "Задание 2", "Тапшырма 2", "Описание 2", now, now, now, now, now, TaskStatus.IN_PROGRESS, 321L)
        );

        when(taskService.getAllTasks()).thenReturn(mockTaskResponses);

        mockMvc.perform(get("/api/v1/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));

        verify(taskService, times(1)).getAllTasks();
    }

    @Test
    void testGetTaskById() throws Exception {
        TaskResponse mockTaskResponse = new TaskResponse(2L, "Задание 2", "Тапшырма 2", "Описание 2", now, now, now, now, now, TaskStatus.IN_PROGRESS, 321L);

        when(taskService.getTaskById(2L)).thenReturn(mockTaskResponse);

        mockMvc.perform(get("/api/v1/tasks/{id}", 2L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.title").value("Задание 2"))
                .andExpect(jsonPath("$.titleOnTatar").value("Тапшырма 2"))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));

        verify(taskService, times(1)).getTaskById(2L);
    }

    @Test
    void testCreateTask() throws Exception {
        TaskRequest mockTaskRequest = new TaskRequest( "Задание 2", "Тапшырма 2", "Описание 2", now, now, now, TaskStatus.IN_PROGRESS, 321L);
        String mockTaskRequestJson = objectMapper.writeValueAsString(mockTaskRequest);

        TaskResponse mockTaskResponse = new TaskResponse(2L, "Задание 2", "Тапшырма 2", "Описание 2", now, now, now, now, now, TaskStatus.IN_PROGRESS, 321L);

        when(taskService.createTask(mockTaskRequest)).thenReturn(mockTaskResponse);

        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mockTaskRequestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.title").value("Задание 2"));;

        verify(taskService, times(1)).createTask(mockTaskRequest);
    }

    @Test
    void testCreateTask_WhenException_ReturnsBadRequest() throws Exception {
        TaskRequest mockTaskRequest = new TaskRequest("Задание 2", "Тапшырма 2", "Описание 2", now, now, now, TaskStatus.IN_PROGRESS, 321L);
        String mockTaskRequestJson = objectMapper.writeValueAsString(mockTaskRequest);

        when(taskService.createTask(mockTaskRequest)).thenThrow(new RuntimeException("Error"));

        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mockTaskRequestJson))
                .andExpect(status().isBadRequest());

        verify(taskService, times(1)).createTask(mockTaskRequest);
    }
}
