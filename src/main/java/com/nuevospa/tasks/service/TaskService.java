package com.nuevospa.tasks.service;

import com.nuevospa.tasks.model.TaskDto;

import java.util.List;
import java.util.Map;

public interface TaskService {

    List<TaskDto> findAll();

    TaskDto findById(Long id);

    TaskDto createTask(TaskDto dto, String username);

    TaskDto updateTask(Long id, TaskDto dto, String username);

    TaskDto patchTask(Long id, Map<String, Object> changes, String username);

    void deleteTask(Long id, String username);

}
