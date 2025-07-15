package com.nuevospa.tasks.service.impl;

import com.nuevospa.tasks.entity.TaskStatusEntity;
import com.nuevospa.tasks.exception.ResourceNotFoundException;
import com.nuevospa.tasks.model.TaskStatusDto;
import com.nuevospa.tasks.repository.TaskStatusRepository;
import com.nuevospa.tasks.service.TaskStatusService;
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

    //TODO: validar enum
    @Override
    public TaskStatusDto findByName(String name) {
        return taskStatusRepo.findByName(name)
                .map(this::entityToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Estado " + name + " no encontrado"));
    }

    @Override
    public TaskStatusDto createTaskStatus(TaskStatusDto dto) {
        TaskStatusEntity entity = new TaskStatusEntity();
        entity.setName(dto.getName());
        return entityToDto(taskStatusRepo.save(entity));
    }

    //TODO: validar campos nulos
    @Override
    public TaskStatusDto updateTaskStatus(Long id, TaskStatusDto dto, String username) {
        TaskStatusDto taskStatusFound = findById(id);
        validateTaskStatusRole(username);
        taskStatusFound.setName(dto.getName());
        return entityToDto(taskStatusRepo.save(dtoToEntity(taskStatusFound)));
    }

    @Override
    public TaskStatusDto patchTaskStatus(Long id, Map<String, Object> changes, String username) {
        TaskStatusDto taskFound = findById(id);

        validateTaskStatusRole(username);
        if (changes.containsKey("name")) {
            taskFound.setName((String) changes.get("name"));
        }
        //TODO: check campos a actualizar
        return entityToDto(taskStatusRepo.save(dtoToEntity(taskFound)));
    }

    //TODO: check cuando esta ligado a tarea
    @Override
    public void deleteTaskStatus(Long id, String username) {
        TaskStatusDto taskFound = findById(id);
        validateTaskStatusRole(username);
        taskStatusRepo.delete(dtoToEntity(taskFound));
    }

    //TODO: validar admin
    private void validateTaskStatusRole(String username) {
        /*if (!taskFound.getUser().getUsername().equals(username)) {
            throw new AccessDeniedException("No eres dueño de la tarea");
        }*/
    }

    //TODO: mapper
    public TaskStatusDto entityToDto(TaskStatusEntity entity) {
        return TaskStatusDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

    public TaskStatusEntity dtoToEntity(TaskStatusDto dto) {
        return TaskStatusEntity.builder()
                .id(dto.getId())
                .name(dto.getName())
                .build();
    }
}
