package com.nuevospa.tasks.service.impl;

import com.nuevospa.tasks.entity.TaskEntity;
import com.nuevospa.tasks.entity.TaskStatusEntity;
import com.nuevospa.tasks.entity.UserEntity;
import com.nuevospa.tasks.model.TaskDto;
import com.nuevospa.tasks.model.TaskStatusDto;
import com.nuevospa.tasks.model.UserDto;
import com.nuevospa.tasks.repository.TaskRepository;
import com.nuevospa.tasks.service.TaskStatusService;
import com.nuevospa.tasks.service.UserService;
import com.nuevospa.tasks.util.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskStatusService taskStatusService;

    @Mock
    private UserService userService;

    private TaskServiceImpl taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskServiceImpl(taskRepository, taskStatusService, userService);
    }

    @Test
    void findAllByUsername() {
        List<TaskEntity> list = new ArrayList<>();
        TaskEntity entity = new TaskEntity();
        entity.setId(1L);
        entity.setTitle("title");
        entity.setDescription("description");

        TaskStatusEntity statusEntity = new TaskStatusEntity();
        statusEntity.setName(TaskStatus.TODO);
        entity.setStatus(statusEntity);

        UserEntity userEntity = new UserEntity();
        entity.setUser(userEntity);

        list.add(entity);

        Pageable pageReq = PageRequest.of(0, 20);

        Page<TaskEntity> page = new PageImpl<>(list, pageReq, list.size());

        when(taskRepository.findByUserUsername(anyString(), any(Pageable.class))).thenReturn(page);

        List<TaskDto> dtos = taskService.findAllByUsername(1, 1, "user");

        assertNotNull(dtos);
    }

    @Test
    void findById() {
        TaskEntity entity = new TaskEntity();
        entity.setId(1L);
        entity.setTitle("title");
        entity.setDescription("description");

        TaskStatusEntity statusEntity = new TaskStatusEntity();
        statusEntity.setName(TaskStatus.TODO);
        entity.setStatus(statusEntity);

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("username");
        entity.setUser(userEntity);

        UserDto userDto = new UserDto();
        userDto.setUsername("username");

        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(entity));
        when(userService.findByUsername(anyString())).thenReturn(userDto);

        TaskDto dto = taskService.findById(1L, "username");

        assertNotNull(dto);
    }

    @Test
    void findAllByUsernameAndStatus() {
        List<TaskEntity> list = new ArrayList<>();
        TaskEntity entity = new TaskEntity();
        entity.setId(1L);
        entity.setTitle("title");
        entity.setDescription("description");

        TaskStatusEntity statusEntity = new TaskStatusEntity();
        statusEntity.setName(TaskStatus.TODO);
        entity.setStatus(statusEntity);

        UserEntity userEntity = new UserEntity();
        entity.setUser(userEntity);

        list.add(entity);

        Pageable pageReq = PageRequest.of(0, 20);

        Page<TaskEntity> page = new PageImpl<>(list, pageReq, list.size());

        when(taskRepository.findByUserUsernameAndStatusName(anyString(), any(TaskStatus.class), any(Pageable.class))).thenReturn(page);

        List<TaskDto> dtos = taskService.findAllByUsernameAndStatus(0, 10, "username", "TODO");

        assertNotNull(dtos);
    }

    @Test
    void create() {
        TaskEntity entity = new TaskEntity();
        entity.setId(1L);
        entity.setTitle("title");
        entity.setDescription("description");

        TaskStatusEntity statusEntity = new TaskStatusEntity();
        statusEntity.setName(TaskStatus.TODO);
        entity.setStatus(statusEntity);

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("username");
        entity.setUser(userEntity);

        UserDto userDto = new UserDto();
        userDto.setUsername("username");

        TaskDto taskDto = new TaskDto();
        taskDto.setId(1L);
        taskDto.setTitle("title");
        taskDto.setDescription("description");
        taskDto.setUser(userDto);

        when(userService.findByUsername(anyString())).thenReturn(userDto);
        when(taskRepository.save(any(TaskEntity.class))).thenReturn(entity);

        TaskDto dto = taskService.create(taskDto, "username");

        assertNotNull(dto);
    }

    @Test
    void update() {
        TaskEntity entity = new TaskEntity();
        entity.setId(1L);
        entity.setTitle("title");
        entity.setDescription("description");

        TaskStatusEntity statusEntity = new TaskStatusEntity();
        statusEntity.setName(TaskStatus.TODO);
        entity.setStatus(statusEntity);

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("usernameEntity");
        entity.setUser(userEntity);

        UserDto userDto = new UserDto();
        userDto.setUsername("username");

        TaskDto taskDto = new TaskDto();
        taskDto.setId(1L);
        taskDto.setTitle("titleDto");
        taskDto.setDescription("descriptionDto");

        TaskStatusDto taskStatusDto = new TaskStatusDto();
        taskStatusDto.setName(TaskStatus.DONE);

        taskDto.setStatus(taskStatusDto);
        taskDto.setUser(userDto);

        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(entity));
        when(taskStatusService.findById(anyLong())).thenReturn(taskStatusDto);
        when(taskStatusService.findByName(anyString())).thenReturn(taskStatusDto);
        when(userService.findByUsername(anyString())).thenReturn(userDto);
        when(taskRepository.save(any(TaskEntity.class))).thenReturn(entity);
        when(taskStatusService.findById(anyLong())).thenReturn(taskStatusDto);

        TaskDto dto = taskService.update(1L, taskDto, "username");

        assertNotNull(dto);
    }

    @Test
    void patch() {
        TaskEntity entity = new TaskEntity();
        entity.setId(1L);
        entity.setTitle("title");
        entity.setDescription("description");

        TaskStatusEntity statusEntity = new TaskStatusEntity();
        statusEntity.setName(TaskStatus.TODO);
        entity.setStatus(statusEntity);

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("usernameEntity");
        entity.setUser(userEntity);

        UserDto userDto = new UserDto();
        userDto.setUsername("username");

        TaskDto taskDto = new TaskDto();
        taskDto.setId(1L);
        taskDto.setTitle("titleDto");
        taskDto.setDescription("descriptionDto");

        TaskStatusDto taskStatusDto = new TaskStatusDto();
        taskStatusDto.setName(TaskStatus.DONE);

        taskDto.setStatus(taskStatusDto);
        taskDto.setUser(userDto);

        Map<String, Object> changes = new HashMap<>();
        changes.put("title", "titleObject");

        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(entity));
        when(taskStatusService.findById(anyLong())).thenReturn(taskStatusDto);
        when(taskStatusService.findByName(anyString())).thenReturn(taskStatusDto);
        when(userService.findByUsername(anyString())).thenReturn(userDto);
        when(taskRepository.save(any(TaskEntity.class))).thenReturn(entity);
        when(taskStatusService.findById(anyLong())).thenReturn(taskStatusDto);

        TaskDto dto = taskService.patch(1L, changes, "username");

        assertNotNull(dto);
    }
}