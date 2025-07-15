package com.nuevospa.tasks.service;

import com.nuevospa.tasks.model.TaskDto;

import java.util.List;
import java.util.Map;

public interface TaskService {

    List<TaskDto> findAllByUsername(int page, int size, String username);

    TaskDto findById(Long id, String username);

    List<TaskDto> findAllByUsernameAndStatus(int page, int size, String username, String status);

    TaskDto create(TaskDto dto, String username);

    TaskDto update(Long id, TaskDto dto, String username);

    TaskDto patch(Long id, Map<String, Object> changes, String username);

    void delete(Long id, String username);

}
