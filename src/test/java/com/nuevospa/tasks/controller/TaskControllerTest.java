package com.nuevospa.tasks.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nuevospa.tasks.config.JwtAuthenticationFilter;
import com.nuevospa.tasks.model.TaskDto;
import com.nuevospa.tasks.model.TaskStatusDto;
import com.nuevospa.tasks.model.UserDto;
import com.nuevospa.tasks.service.TaskService;
import com.nuevospa.tasks.util.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.beans.factory.annotation.Autowired;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(
        controllers = TaskController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class))
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    private TaskDto firstTask;

    private TaskDto secondTask;

    private TaskStatusDto status;

    private UserDto user;
    
    @Value("${api.path-tasks}")
    private String url;

    @BeforeEach
    void setUp() {
        firstTask = new TaskDto();
        firstTask.setId(1L);
        firstTask.setTitle("Tarea 1");
        firstTask.setDescription("Desc 1");
        firstTask.setStatus(TaskStatusDto.builder().id(10L).name(TaskStatus.TODO).build());
        firstTask.setUser(UserDto.builder().id(100L).username("user").build());

        secondTask = new TaskDto();
        secondTask.setId(2L);
        secondTask.setTitle("Tarea 2");
        secondTask.setDescription("Desc 2");
        secondTask.setStatus(TaskStatusDto.builder().id(11L).name(TaskStatus.IN_PROGRESS).build());
        secondTask.setUser(UserDto.builder().id(100L).username("otherUser").build());

        status = new TaskStatusDto();
        status.setName(TaskStatus.IN_PROGRESS);

        user = new UserDto();
        user.setUsername("user");
    }

    @WithMockUser(username = "user", roles = "USER")
    @Test
    void findAllByUsername() throws Exception {
        when(taskService.findAllByUsername(1, 5, firstTask.getUser().getUsername()))
                .thenReturn(List.of(firstTask, secondTask));

        mockMvc.perform(get(url)
                        .param("page", "1")
                        .param("size", "5")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value(firstTask.getTitle()))
                .andExpect(jsonPath("$[0].user.username").value(firstTask.getUser().getUsername()))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].status.name").value(TaskStatus.IN_PROGRESS.name()));

        verify(taskService).findAllByUsername(1, 5, firstTask.getUser().getUsername());
    }

    @WithMockUser(username = "user", roles = "USER")
    @Test
    void findByIdAndUserUsername() throws Exception {
        when(taskService.findById(1L, firstTask.getUser().getUsername())).thenReturn(firstTask);

        mockMvc.perform(get(url + "/{id}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value(firstTask.getTitle()))
                .andExpect(jsonPath("$.user.username").value(firstTask.getUser().getUsername()))
                .andExpect(jsonPath("$.status.name").value(TaskStatus.TODO.name()));

        verify(taskService).findById(1L, firstTask.getUser().getUsername());
    }

    @WithMockUser(username = "user", roles = "USER")
    @Test
    void findAllByUsernameAndStatus() throws Exception {
        when(taskService.findAllByUsernameAndStatus(0, 20, firstTask.getUser().getUsername(), TaskStatus.TODO.name()))
                .thenReturn(List.of(firstTask, secondTask));

        mockMvc.perform(get(url + "/status/{status}", TaskStatus.TODO.name())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status.name").value(TaskStatus.TODO.name()))
                .andExpect(jsonPath("$[0].user.username").value(firstTask.getUser().getUsername()))
                .andExpect(jsonPath("$[1].id").value(2));

        verify(taskService).findAllByUsernameAndStatus(0, 20, firstTask.getUser().getUsername(), TaskStatus.TODO.name());
    }

    @WithMockUser(username = "user", roles = "USER")
    @Test
    void create() throws Exception {
        when(taskService.create(any(TaskDto.class), eq(firstTask.getUser().getUsername()))).thenReturn(firstTask);

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(firstTask)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", url + "/" + firstTask.getId()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(firstTask.getId()))
                .andExpect(jsonPath("$.title").value(firstTask.getTitle()))
                .andExpect(jsonPath("$.user.username").value(firstTask.getUser().getUsername()));

        ArgumentCaptor<TaskDto> captor = ArgumentCaptor.forClass(TaskDto.class);
        verify(taskService).create(captor.capture(), eq(firstTask.getUser().getUsername()));
    }

    @WithMockUser(username = "user", roles = "USER")
    @Test
    void update() throws Exception {
        TaskDto updated = new TaskDto(10L, "Actualizada", "Desc actualizada", status, user);
        when(taskService.update(eq(10L), any(TaskDto.class), eq(firstTask.getUser().getUsername()))).thenReturn(updated);

        mockMvc.perform(put(url + "/{id}", 10L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(firstTask)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(10)))
                .andExpect(jsonPath("$.title", is("Actualizada")))
                .andExpect(jsonPath("$.description", is("Desc actualizada")))
                .andExpect(jsonPath("$.status.name", is(TaskStatus.IN_PROGRESS.name())));

        ArgumentCaptor<TaskDto> captor = ArgumentCaptor.forClass(TaskDto.class);
        verify(taskService).update(eq(10L), captor.capture(), eq(firstTask.getUser().getUsername()));
    }

    @WithMockUser(username = "user", roles = "USER")
    @Test
    void patchTest() throws Exception {
        String patchBody = """
                {"title":"Parcial","status":{"name":"DONE"}}
                """;

        TaskDto patched = new TaskDto(5L, "Parcial", "Desc 5", status, user);
        when(taskService.patch(eq(5L), anyMap(), eq("user"))).thenReturn(patched);

        mockMvc.perform(patch(url + "/{id}", 5L)
                        .contentType("application/merge-patch+json")
                        .content(patchBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(5)))
                .andExpect(jsonPath("$.title", is("Parcial")))
                .andExpect(jsonPath("$.status.name", is(status.getName().name())));

        verify(taskService).patch(eq(5L), any(Map.class), eq("user"));
    }

    @WithMockUser(username = "user", roles = "USER")
    @Test
    void deleteTest() throws Exception {
        doNothing().when(taskService).delete(15L, user.getUsername());

        mockMvc.perform(delete(url + "/{id}", 15L)).andExpect(status().isNoContent());

        verify(taskService).delete(15L, user.getUsername());
    }
}