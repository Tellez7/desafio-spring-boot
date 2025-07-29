package com.nuevospa.tasks.service.impl;

import com.nuevospa.tasks.model.AuthResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.when;

@SpringBootTest
class AuthServiceImplTest {

    private AuthServiceImpl authService;

    @Mock
    private AuthenticationManager authManager;

    @Mock
    private JwtServiceImpl jwtService;

    @BeforeEach
    void setUp() {
        authService = new AuthServiceImpl(authManager, jwtService);
    }

    @Test
    void authenticate() {
        UserDetails principal = User.withUsername("user")
                .password("pass")
                .roles("ADMIN")
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());

        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtService.generateToken(any(UserDetails.class), anyMap())).thenReturn("fake.jwt.token");

        AuthResponseDto details = authService.authenticate("user", "pass");

        assertNotNull(details);
    }
}