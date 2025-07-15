package com.nuevospa.tasks.service.impl;

import com.nuevospa.tasks.entity.UserEntity;
import com.nuevospa.tasks.model.UserDto;
import com.nuevospa.tasks.repository.UserRepository;
import com.nuevospa.tasks.util.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceImplTest {

    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder encoder;

    UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository, encoder);
    }

    @Test
    void findByName() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("username");
        userEntity.setPassword("password");
        userEntity.setRole(Role.ADMIN);

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(userEntity));

        UserDto dto = userService.findByUsername("username");

        assertNotNull(dto);
    }

    @Test
    void findById() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("username");
        userEntity.setPassword("password");
        userEntity.setRole(Role.ADMIN);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userEntity));

        UserDto dto = userService.findById(1L);

        assertNotNull(dto);
    }

    @Test
    void create() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("username");
        userEntity.setPassword("password");
        userEntity.setRole(Role.ADMIN);

        UserDto userDto = new UserDto();
        userDto.setUsername("other_username");
        userDto.setPassword("password");
        userDto.setRole(Role.ADMIN);

        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(encoder.encode(anyString())).thenReturn("encodePassword");
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        UserDto dto = userService.create(userDto, "username");

        assertNotNull(dto);
    }

    @Test
    void update() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("username");
        userEntity.setPassword("password");
        userEntity.setRole(Role.ADMIN);

        UserDto userDto = new UserDto();
        userDto.setUsername("username");
        userDto.setPassword("password");
        userDto.setRole(Role.ADMIN);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userEntity));
        when(encoder.encode(anyString())).thenReturn("encodePassword");
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        UserDto dto = userService.update(1L, userDto, "username");

        assertNotNull(dto);
    }

    @Test
    void patch() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("username");
        userEntity.setPassword("password");
        userEntity.setRole(Role.ADMIN);

        UserDto userDto = new UserDto();
        userDto.setUsername("username");
        userDto.setPassword("password");
        userDto.setRole(Role.ADMIN);

        Map<String, Object> changes = new HashMap<>();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userEntity));
        when(encoder.encode(anyString())).thenReturn("encodePassword");
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        UserDto dto = userService.patch(1L, changes, "username");

        assertNotNull(dto);
    }
}