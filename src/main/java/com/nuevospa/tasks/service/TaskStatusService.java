package com.nuevospa.tasks.service;

import com.nuevospa.tasks.entity.TaskStatusEntity;
import com.nuevospa.tasks.model.TaskStatusDto;

import java.util.List;
import java.util.Map;

public interface TaskStatusService {

    List<TaskStatusDto> findAll();

    TaskStatusDto findById(Long id);

    TaskStatusDto findByName(String name);

    TaskStatusDto create(TaskStatusDto dto);

    TaskStatusDto update(Long id, TaskStatusDto dto, String username);

    TaskStatusDto patch(Long id, Map<String, Object> changes, String username);

    void delete(Long id, String username);

    TaskStatusDto entityToDto(TaskStatusEntity entity);

    TaskStatusEntity dtoToEntity(TaskStatusDto dto);
}
