package com.nuevospa.tasks.service.impl;

import com.nuevospa.tasks.entity.TaskStatusEntity;
import com.nuevospa.tasks.model.TaskStatusDto;
import com.nuevospa.tasks.repository.TaskStatusRepository;
import com.nuevospa.tasks.service.TaskStatusService;
import com.nuevospa.tasks.util.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
class TaskStatusServiceImplTest {

    @Mock
    TaskStatusRepository taskStatusRepository;

    TaskStatusService taskStatusService;

    @BeforeEach
    void setUp() {
        taskStatusService = new TaskStatusServiceImpl(taskStatusRepository);
    }

    @Test
    void findByName() {
        TaskStatusEntity statusEntity = new TaskStatusEntity();
        statusEntity.setName(TaskStatus.TODO);

        when(taskStatusRepository.findByName(any(TaskStatus.class))).thenReturn(Optional.of(statusEntity));

        TaskStatusDto dto = taskStatusService.findByName("TODO");

        assertNotNull(dto);
    }

    @Test
    void findById() {
        TaskStatusEntity statusEntity = new TaskStatusEntity();
        statusEntity.setId(1L);
        statusEntity.setName(TaskStatus.DONE);

        when(taskStatusRepository.findById(anyLong())).thenReturn(Optional.of(statusEntity));

        TaskStatusDto dto = taskStatusService.findById(1L);

        assertNotNull(dto);
    }

    @Test
    void create() {
        TaskStatusEntity entity = new TaskStatusEntity();
        entity.setName(TaskStatus.DONE);

        TaskStatusDto taskStatusDto = new TaskStatusDto();
        taskStatusDto.setName(TaskStatus.DONE);

        when(taskStatusRepository.save(any(TaskStatusEntity.class))).thenReturn(entity);

        TaskStatusDto dto = taskStatusService.create(taskStatusDto);

        assertNotNull(dto);
    }

    @Test
    void update() {
        TaskStatusEntity statusEntity = new TaskStatusEntity();
        statusEntity.setId(1L);
        statusEntity.setName(TaskStatus.DONE);

        TaskStatusEntity entity = new TaskStatusEntity();
        entity.setName(TaskStatus.DONE);

        TaskStatusDto taskStatusDto = new TaskStatusDto();
        taskStatusDto.setName(TaskStatus.DONE);

        when(taskStatusRepository.findById(anyLong())).thenReturn(Optional.of(statusEntity));
        when(taskStatusRepository.save(any(TaskStatusEntity.class))).thenReturn(entity);

        TaskStatusDto dto = taskStatusService.update(1L, taskStatusDto, "username");

        assertNotNull(dto);
    }

    @Test
    void patch() {
        TaskStatusEntity statusEntity = new TaskStatusEntity();
        statusEntity.setId(1L);
        statusEntity.setName(TaskStatus.DONE);

        TaskStatusEntity entity = new TaskStatusEntity();
        entity.setName(TaskStatus.DONE);

        TaskStatusDto taskStatusDto = new TaskStatusDto();
        taskStatusDto.setName(TaskStatus.DONE);

        when(taskStatusRepository.findById(anyLong())).thenReturn(Optional.of(statusEntity));
        when(taskStatusRepository.save(any(TaskStatusEntity.class))).thenReturn(entity);

        Map<String, Object> changes = new HashMap<>();

        TaskStatusDto dto = taskStatusService.patch(1L, changes, "username");

        assertNotNull(dto);
    }
}