package com.nuevospa.tasks.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nuevospa.tasks.config.JwtAuthenticationFilter;
import com.nuevospa.tasks.model.TaskStatusDto;
import com.nuevospa.tasks.service.TaskStatusService;
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

import java.net.URI;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.beans.factory.annotation.Autowired;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(
        controllers = TaskStatusController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class))
class TaskStatusControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskStatusService taskStatusService;

    private TaskStatusDto firstStatus;

    private TaskStatusDto secondStatus;
    
    @Value("${api.path-task-statuses}")
    private String url;

    @BeforeEach
    void setUp() {
        firstStatus = new TaskStatusDto();
        firstStatus.setId(1L);
        firstStatus.setName(TaskStatus.IN_PROGRESS);

        secondStatus = new TaskStatusDto();
        secondStatus.setId(3L);
        secondStatus.setName(TaskStatus.DONE);
    }

    @Test
    void findAllByUsername() throws Exception {
        when(taskStatusService.findAll()).thenReturn(List.of(firstStatus, secondStatus));

        mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is(TaskStatus.IN_PROGRESS.name())))
                .andExpect(jsonPath("$[1].id", is(3)))
                .andExpect(jsonPath("$[1].name", is(TaskStatus.DONE.name())));

        verify(taskStatusService).findAll();
    }

    @Test
    void findById() throws Exception {
        when(taskStatusService.findById(firstStatus.getId())).thenReturn(firstStatus);

        mockMvc.perform(get(url + "/{id}", firstStatus.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(firstStatus.getId()))
                .andExpect(jsonPath("$.name").value(TaskStatus.IN_PROGRESS.name()));

        verify(taskStatusService).findById(firstStatus.getId());
    }

    @Test
    void create() throws Exception {
        when(taskStatusService.create(any(TaskStatusDto.class))).thenReturn(firstStatus);

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(secondStatus)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", URI.create(url + "/" + firstStatus.getId()).toString()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(firstStatus.getId()))
                .andExpect(jsonPath("$.name").value(TaskStatus.IN_PROGRESS.name()));

        verify(taskStatusService).create(any(TaskStatusDto.class));
    }

    @WithMockUser(username = "admin", roles = "ADMIN")
    @Test
    void update() throws Exception {
        when(taskStatusService.update(eq(3L), any(TaskStatusDto.class), eq("admin")))
                .thenReturn(secondStatus);

        mockMvc.perform(put(url + "/{id}", secondStatus.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(firstStatus)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(secondStatus.getId()))
                .andExpect(jsonPath("$.name").value(TaskStatus.DONE.name()));

        verify(taskStatusService).update(eq(secondStatus.getId()), any(TaskStatusDto.class), eq("admin"));
    }

    @WithMockUser(username = "admin", roles = "ADMIN")
    @Test
    void patchTest() throws Exception {
        Map<String, Object> changes = Map.of("name", "IN_PROGRESS");

        when(taskStatusService.patch(eq(firstStatus.getId()), anyMap(), eq("admin"))).thenReturn(secondStatus);

        mockMvc.perform(patch(url + "/{id}", firstStatus.getId())
                        .contentType("application/merge-patch+json")
                        .content(new ObjectMapper().writeValueAsString(changes)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(secondStatus.getId()))
                .andExpect(jsonPath("$.name").value(TaskStatus.DONE.name()));

        @SuppressWarnings("unchecked")
        ArgumentCaptor<Map<String, Object>> mapCaptor = ArgumentCaptor.forClass(Map.class);
        verify(taskStatusService).patch(eq(firstStatus.getId()), mapCaptor.capture(), eq("admin"));
        assertThat(mapCaptor.getValue()).containsEntry("name", "IN_PROGRESS");
    }

    @WithMockUser(username = "admin", roles = "ADMIN")
    @Test
    void deleteTest() throws Exception {
        mockMvc.perform(delete(url + "/{id}", firstStatus.getId()))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(taskStatusService).delete(eq(firstStatus.getId()), eq("admin"));
    }
}