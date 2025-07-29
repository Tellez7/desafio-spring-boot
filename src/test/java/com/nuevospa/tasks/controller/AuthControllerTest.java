package com.nuevospa.tasks.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nuevospa.tasks.config.JwtAuthenticationFilter;
import com.nuevospa.tasks.model.AuthRequestDto;
import com.nuevospa.tasks.model.AuthResponseDto;
import com.nuevospa.tasks.service.AuthService;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(
        controllers = AuthController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class))
class AuthControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @SuppressWarnings("removal")
    @MockBean
    private AuthService authService;

    @Test
    void login_ok() throws Exception {
        // given
        AuthRequestDto req = new AuthRequestDto("john", "secret");
        AuthResponseDto res = new AuthResponseDto("fake.jwt.token", new Date());

        BDDMockito.given(authService.authenticate("john", "secret"))
                .willReturn(res);

        // when - then
        mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").value("fake.jwt.token"));
    }
}
