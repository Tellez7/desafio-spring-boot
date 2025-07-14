package com.nuevospa.tasks.service;

import com.nuevospa.tasks.model.TaskDto;
import com.nuevospa.tasks.entity.TaskEntity;
import com.nuevospa.tasks.repository.TaskRepository;
import com.nuevospa.tasks.repository.TaskStatusRepository;
import com.nuevospa.tasks.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository repository;
    private final TaskStatusRepository statusRepo;
    private final UserRepository userRepo;

    public List<TaskDto> findAll(String username) {
        return repository.findByOwnerUsernameOrderByIdAsc(username)
                .stream()
                .map(this::toDto)
                .toList();
    }

    public TaskDto create(TaskDto dto, String username) {
        TaskEntity entity = new TaskEntity();
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        //entity.setStatus(statusRepo.getReferenceById(dto.getStatus().getId()));
        //entity.setOwner(userRepo.findByUsername(username).orElseThrow());
        return toDto(repository.save(entity));
    }

    private TaskDto toDto(TaskEntity entity) {
        return TaskDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .build();
    }
}
