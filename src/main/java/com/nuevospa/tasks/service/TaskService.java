package com.nuevospa.tasks.service;

import com.nuevospa.tasks.exception.ResourceNotFoundException;
import com.nuevospa.tasks.model.TaskDto;
import com.nuevospa.tasks.entity.TaskEntity;
import com.nuevospa.tasks.repository.TaskRepository;
import com.nuevospa.tasks.repository.TaskStatusRepository;
import com.nuevospa.tasks.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskStatusRepository statusRepo;
    private final UserRepository userRepo;

    public List<TaskDto> findAll() {
        return taskRepository.findAll()
                .stream()
                .map(this::entityToDto)
                .toList();
    }

    public TaskDto findById(Long id) {
        return taskRepository.findById(id)
                .map(this::entityToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Tarea " + id + " no encontrada"));
    }

    public TaskDto createTask(TaskDto dto, String username) {
        TaskEntity entity = new TaskEntity();
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        //entity.setStatus(statusRepo.getReferenceById(dto.getStatus().getId()));
        //entity.setOwner(userRepo.findByUsername(username).orElseThrow());
        return entityToDto(taskRepository.save(entity));
    }

    public TaskDto updateTask(Long id, TaskDto dto, String username) {
        TaskDto task = findById(id);

        /*  ----- reglas de negocio opcionales -----
         *  if (!task.getOwner().getUsername().equals(username)) {
         *      throw new AccessDeniedException("No eres dueño de la tarea");
         *  }
         */

        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());

        /*if (dto.getStatus() != null) {
            task.setStatus(statusRepo.getReferenceById(dto.getStatus().getId()));
        }*/

        return entityToDto(taskRepository.save(dtoToEntity(task)));
    }

    public TaskDto patchTask(Long id, Map<String, Object> changes) {
        TaskDto task = findById(id);

        //TODO: cambiar por generico
        if (changes.containsKey("title"))
            task.setTitle((String) changes.get("title"));

        if (changes.containsKey("description"))
            task.setDescription((String) changes.get("description"));

        /*if (changes.containsKey("status")) {
            Map<?, ?> statusMap = (Map<?, ?>) changes.get("status");
            Long statusId = ((Number) statusMap.get("id")).longValue();
            task.setStatus(statusRepo.getReferenceById(statusId));
        }*/

        //TODO: check campos a actualizar
        return entityToDto(taskRepository.save(dtoToEntity(task)));
    }


    public void deleteTask(Long id, String username) {
        TaskDto task = findById(id);

        /* validar dueño si aplica
        if (!entity.getOwner().getUsername().equals(username)) {
            throw new AccessDeniedException("No eres dueño de la tarea");
        }
        */

        taskRepository.delete(dtoToEntity(task));
    }

    //TODO: mapper
    private TaskDto entityToDto(TaskEntity entity) {
        return TaskDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .build();
    }

    private TaskEntity dtoToEntity(TaskDto dto) {
        return TaskEntity.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .build();
    }
}
