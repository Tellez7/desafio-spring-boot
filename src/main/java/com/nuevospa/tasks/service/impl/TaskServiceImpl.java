package com.nuevospa.tasks.service.impl;

import com.nuevospa.tasks.entity.TaskEntity;
import com.nuevospa.tasks.exception.AccessDeniedException;
import com.nuevospa.tasks.exception.ResourceNotFoundException;
import com.nuevospa.tasks.model.TaskDto;
import com.nuevospa.tasks.model.TaskStatusDto;
import com.nuevospa.tasks.model.UserDto;
import com.nuevospa.tasks.repository.TaskRepository;
import com.nuevospa.tasks.service.TaskService;
import com.nuevospa.tasks.service.TaskStatusService;
import com.nuevospa.tasks.service.UserService;
import com.nuevospa.tasks.util.TaskStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.nuevospa.tasks.util.TaskStatus.TODO;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskStatusService taskStatusService;
    private final UserService userService;

    @Override
    public List<TaskDto> findAllByUsername(int page, int size, String username) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return taskRepository.findByUserUsername(username, pageable)
                .stream()
                .map(this::entityToDto)
                .toList();
    }

    @Override
    public TaskDto findById(Long id, String username) {
        TaskDto taskDto = taskRepository.findById(id)
                .map(this::entityToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Tarea " + id + " no encontrada"));

        validateTaskOwner(taskDto, username);
        return taskDto;
    }

    @Override
    public List<TaskDto> findAllByUsernameAndStatus(int page, int size, String username, String status) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return taskRepository.findByUserUsernameAndStatusName(username, TaskStatus.valueOf(status), pageable)
                .stream()
                .map(this::entityToDto)
                .toList();
    }

    @Override
    public TaskDto create(TaskDto dto, String username) {
        TaskEntity entity = new TaskEntity();
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());

        TaskStatusDto taskDto;
        if (dto.getStatus() != null) {
            taskDto = taskStatusService.findByName(dto.getStatus().getName().name());
        } else {
            taskDto = taskStatusService.findByName(TODO.name());
        }
        entity.setStatus(taskStatusService.dtoToEntity(taskDto));

        UserDto userDto = userService.findByUsername(username);
        entity.setUser(userService.dtoToEntity(userDto));

        return entityToDto(taskRepository.save(entity));
    }

    @Override
    public TaskDto update(Long id, TaskDto dto, String username) {
        TaskDto taskFound = findById(id, username);
        taskFound.setTitle(dto.getTitle());
        taskFound.setDescription(dto.getDescription());

        if (dto.getStatus() != null) {
            taskFound.setStatus(taskStatusService.findByName(dto.getStatus().getName().name()));
        }
        if (dto.getUser() != null) {
            taskFound.setUser(userService.findByUsername(dto.getUser().getUsername()));
        }
        return entityToDto(taskRepository.save(dtoToEntity(taskFound)));
    }

    @Override
    public TaskDto patch(Long id, Map<String, Object> changes, String username) {
        TaskDto taskFound = findById(id, username);

        if (changes.containsKey("title")) {
            taskFound.setTitle((String) changes.get("title"));
        }
        if (changes.containsKey("description")) {
            taskFound.setDescription((String) changes.get("description"));
        }
        if (changes.containsKey("status")) {
            Map<?, ?> statusMap = (Map<?, ?>) changes.get("status");
            String name = ((String) statusMap.get("name"));
            taskFound.setStatus(taskStatusService.findByName(name));
        }
        if (changes.containsKey("user")) {
            Map<?, ?> userMap = (Map<?, ?>) changes.get("user");
            String user = ((String) userMap.get("username"));
            taskFound.setUser(userService.findByUsername(user));
        }
        return entityToDto(taskRepository.save(dtoToEntity(taskFound)));
    }

    @Override
    public void delete(Long id, String username) {
        TaskDto taskFound = findById(id, username);
        taskRepository.delete(dtoToEntity(taskFound));
    }

    private void validateTaskOwner(TaskDto taskFound, String username) {
        if (!taskFound.getUser().getUsername().equals(username)) {
            throw new AccessDeniedException("No eres dueño de la tarea");
        }
    }

    private TaskDto entityToDto(TaskEntity entity) {
        return TaskDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .status(taskStatusService.findByName(entity.getStatus().getName().name()))
                .user(userService.findByUsername(entity.getUser().getUsername()))
                .build();
    }

    private TaskEntity dtoToEntity(TaskDto dto) {
        TaskStatusDto taskStatusDto = taskStatusService.findById(dto.getStatus().getId());
        UserDto userDto = userService.findById(dto.getUser().getId());

        TaskEntity entity = new TaskEntity();
        entity.setId(dto.getId());
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setStatus(taskStatusService.dtoToEntity(taskStatusDto));
        entity.setUser(userService.dtoToEntity(userDto));
        return entity;
    }
}
