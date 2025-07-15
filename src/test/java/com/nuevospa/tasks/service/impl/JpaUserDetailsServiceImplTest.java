package com.nuevospa.tasks.service.impl;

import com.nuevospa.tasks.entity.UserEntity;
import com.nuevospa.tasks.repository.UserRepository;
import com.nuevospa.tasks.util.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class JpaUserDetailsServiceImplTest {

    JpaUserDetailsServiceImpl jpaUserDetailsService;
    @Mock
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        jpaUserDetailsService = new JpaUserDetailsServiceImpl(userRepository);
    }

    @Test
    void loadUserByUsername() {

        UserEntity entity = new UserEntity();
        entity.setUsername("user");
        entity.setPassword("pass");
        entity.setRole(Role.USER);

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(entity));

        UserDetails details = jpaUserDetailsService.loadUserByUsername(anyString());

        assertNotNull(details);
    }
}