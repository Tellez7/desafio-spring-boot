package com.nuevospa.tasks.service.impl;

import com.nuevospa.tasks.entity.TaskStatusEntity;
import com.nuevospa.tasks.exception.ResourceNotFoundException;
import com.nuevospa.tasks.model.TaskStatusDto;
import com.nuevospa.tasks.repository.TaskStatusRepository;
import com.nuevospa.tasks.service.TaskStatusService;
import com.nuevospa.tasks.util.TaskStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TaskStatusServiceImpl implements TaskStatusService {

    private final TaskStatusRepository taskStatusRepo;

    @Override
    public List<TaskStatusDto> findAll() {
        return taskStatusRepo.findAll()
                .stream()
                .map(this::entityToDto)
                .toList();
    }

    @Override
    public TaskStatusDto findById(Long id) {
        return taskStatusRepo.findById(id)
                .map(this::entityToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Estado " + id + " no encontrado"));
    }

    @Override
    public TaskStatusDto findByName(String name) {
        return taskStatusRepo.findByName(TaskStatus.valueOf(name))
                .map(this::entityToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Estado " + name + " no encontrado"));
    }

    @Override
    public TaskStatusDto create(TaskStatusDto dto) {
        TaskStatusEntity entity = new TaskStatusEntity();
        entity.setName(dto.getName());
        return entityToDto(taskStatusRepo.save(entity));
    }

    @Override
    public TaskStatusDto update(Long id, TaskStatusDto dto, String username) {
        TaskStatusDto taskStatusFound = findById(id);
        taskStatusFound.setName(dto.getName());
        return entityToDto(taskStatusRepo.save(dtoToEntity(taskStatusFound)));
    }

    @Override
    public TaskStatusDto patch(Long id, Map<String, Object> changes, String username) {
        TaskStatusDto taskFound = findById(id);

        if (changes.containsKey("name")) {
            taskFound.setName(TaskStatus.valueOf((String) changes.get("name")));
        }
        return entityToDto(taskStatusRepo.save(dtoToEntity(taskFound)));
    }

    @Override
    public void delete(Long id, String username) {
        TaskStatusDto taskFound = findById(id);
        taskStatusRepo.delete(dtoToEntity(taskFound));
    }

    public TaskStatusDto entityToDto(TaskStatusEntity entity) {
        return TaskStatusDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

    public TaskStatusEntity dtoToEntity(TaskStatusDto dto) {
        TaskStatusEntity entity = new TaskStatusEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        return entity;
    }
}
