package com.nuevospa.tasks.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nuevospa.tasks.config.JwtAuthenticationFilter;
import com.nuevospa.tasks.model.UserDto;
import com.nuevospa.tasks.service.UserService;
import com.nuevospa.tasks.util.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
        controllers = UserController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class))
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private UserDto firstUser;

    private UserDto secondUser;

    @Value("${api.path-users}")
    private String url;

    @BeforeEach
    void setUp() {
        firstUser = new UserDto();
        firstUser.setId(1L);
        firstUser.setUsername("user");
        firstUser.setPassword("secret123");
        firstUser.setRole(Role.ADMIN);

        secondUser = new UserDto();
        secondUser.setId(3L);
        secondUser.setUsername("otherUser");
        secondUser.setPassword("otherPass");
        secondUser.setRole(Role.USER);
    }

    @Test
    void findAll() throws Exception {
        when(userService.findAll(0, 20)).thenReturn(List.of(firstUser, secondUser));

        mockMvc.perform(get(url)
                        .param("page", "0")
                        .param("size", "20")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(firstUser.getId()))
                .andExpect(jsonPath("$[0].username").value(firstUser.getUsername()))
                .andExpect(jsonPath("$[0].role").value(firstUser.getRole().name()))
                .andExpect(jsonPath("$[1].id").value(secondUser.getId()))
                .andExpect(jsonPath("$[1].username").value(secondUser.getUsername()))
                .andExpect(jsonPath("$[1].role").value(secondUser.getRole().name()));

        verify(userService).findAll(eq(0), eq(20));
    }

    @WithMockUser(username = "admin", roles = "ADMIN")
    @Test
    void findById() throws Exception {
        when(userService.findById(firstUser.getId())).thenReturn(firstUser);

        mockMvc.perform(get(url + "/{id}", firstUser.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(firstUser.getId()))
                .andExpect(jsonPath("$.username").value(firstUser.getUsername()))
                .andExpect(jsonPath("$.role").value(firstUser.getRole().name()));

        verify(userService).findById(eq(firstUser.getId()));
    }

    @WithMockUser(username = "admin", roles = "ADMIN")
    @Test
    void findAllByRole() throws Exception {
        when(userService.findAllByRole(0, 20, Role.USER)).thenReturn(List.of(firstUser, secondUser));

        mockMvc.perform(get(url + "/role/{role}", Role.USER.name())
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(firstUser.getId()))
                .andExpect(jsonPath("$[0].username").value(firstUser.getUsername()))
                .andExpect(jsonPath("$[0].role").value(firstUser.getRole().name()))
                .andExpect(jsonPath("$[1].id").value(secondUser.getId()))
                .andExpect(jsonPath("$[1].username").value(secondUser.getUsername()))
                .andExpect(jsonPath("$[1].role").value(secondUser.getRole().name()));

        verify(userService).findAllByRole(eq(0), eq(20), eq(Role.USER));
    }

    @WithMockUser(username = "admin", roles = "ADMIN")
    @Test
    void create() throws Exception {
        var request = """
                {
                  "username": "alice",
                  "password": "secret123",
                  "role": "USER"
                }
                """;

        when(userService.create(any(UserDto.class), eq("admin"))).thenReturn(firstUser);

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", url + "/" + firstUser.getId()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(firstUser.getId()))
                .andExpect(jsonPath("$.username").value(firstUser.getUsername()))
                .andExpect(jsonPath("$.role").value(firstUser.getRole().name()));

        verify(userService).create(any(UserDto.class), eq("admin"));
    }

    @WithMockUser(username = "admin", roles = "ADMIN")
    @Test
    void update() throws Exception {
        var requestJson = """
                {
                  "username": "alice.updated",
                  "password": "newpass",
                  "role": "ADMIN"
                }
                """;

        when(userService.update(eq(firstUser.getId()), any(UserDto.class), eq("admin"))).thenReturn(firstUser);

        mockMvc.perform(put(url + "/{id}", firstUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(firstUser.getId()))
                .andExpect(jsonPath("$.username").value(firstUser.getUsername()))
                .andExpect(jsonPath("$.role").value(firstUser.getRole().name()));

        verify(userService).update(eq(firstUser.getId()), any(UserDto.class), eq("admin"));
    }

    @WithMockUser(username = "admin", roles = "ADMIN")
    @Test
    void patchTest() throws Exception {
        Map<String, Object> changes = Map.of(
                "username", "newname",
                "role", "USER"
        );

        when(userService.patch(eq(secondUser.getId()), eq(changes), eq("admin"))).thenReturn(secondUser);

        mockMvc.perform(patch(url + "/" + secondUser.getId())
                        .contentType("application/merge-patch+json")
                        .content(new ObjectMapper().writeValueAsString(changes))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(secondUser.getId()))
                .andExpect(jsonPath("$.username").value(secondUser.getUsername()))
                .andExpect(jsonPath("$.role").value(secondUser.getRole().name()));

        verify(userService).patch(secondUser.getId(), changes, "admin");
    }

    @WithMockUser(username = "admin", roles = "ADMIN")
    @Test
    void deleteTest() throws Exception {
        mockMvc.perform(delete(url + "/" + firstUser.getId())).andExpect(status().isNoContent());

        verify(userService).delete(firstUser.getId(), "admin");
    }
}