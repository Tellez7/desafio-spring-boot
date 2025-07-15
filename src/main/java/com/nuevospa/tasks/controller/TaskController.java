package com.nuevospa.tasks.controller;

import com.nuevospa.tasks.model.TaskDto;
import com.nuevospa.tasks.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${api.path-tasks}")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @Operation(
            summary = "Listar tareas",
            description = "Devuelve todas las tareas visibles para el usuario autenticado"
    )
    @ApiResponse(responseCode = "200",
            description = "Lista de tareas",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = TaskDto.class))))
    @GetMapping
    public List<TaskDto> findAll() {
        return taskService.findAll();
    }

    @Operation(
            summary = "Obtener tarea por ID",
            description = "Busca una tarea por su identificador"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = @Content(schema = @Schema(implementation = TaskDto.class))),
            @ApiResponse(responseCode = "404",
                    description = "Tarea no encontrada",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.findById(id));
    }

    @Operation(
            summary = "Crear tarea",
            description = "Crea una nueva tarea y devuelve la entidad creada"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201",
                    content = @Content(schema = @Schema(implementation = TaskDto.class))),
            //TODO: check
            @ApiResponse(responseCode = "400",
                    description = "Datos inválidos",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<TaskDto> create(
            @Valid @RequestBody TaskDto taskDto,
            @AuthenticationPrincipal UserDetails loggedUser) {
        TaskDto created = taskService.create(taskDto, loggedUser.getUsername());
        return ResponseEntity
                .created(URI.create("/api/tasks/" + created.getId()))
                .body(created);
    }

    @Operation(
            summary = "Reemplazar tarea",
            description = "Reemplaza **completamente** la tarea con los datos enviados"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = @Content(schema = @Schema(implementation = TaskDto.class))),
            @ApiResponse(responseCode = "404",
                    description = "Tarea no encontrada",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<TaskDto> update(
            @PathVariable Long id,
            @RequestBody TaskDto taskDto,
            @AuthenticationPrincipal UserDetails loggedUser) {
        TaskDto updated = taskService.update(id, taskDto, loggedUser.getUsername());
        return ResponseEntity.ok(updated);
    }

    @Operation(
            summary = "Actualizar parcialmente tarea",
            description = "Modifica solo los campos indicados usando JSON Merge Patch"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = @Content(schema = @Schema(implementation = TaskDto.class))),
            @ApiResponse(responseCode = "404",
                    description = "Tarea no encontrada",
                    content = @Content)
    })
    @PatchMapping(value = "/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<TaskDto> patch(@PathVariable Long id,
                                         @RequestBody Map<String, Object> changes,
                                         @AuthenticationPrincipal UserDetails loggedUser) {
        TaskDto updated = taskService.patch(id, changes, loggedUser.getUsername());
        return ResponseEntity.ok(updated);
    }

    @Operation(
            summary = "Eliminar tarea",
            description = "Borra la tarea indicada"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Tarea no encontrada",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal UserDetails loggedUser) {
        taskService.delete(id, loggedUser.getUsername());
        return ResponseEntity.noContent().build();
    }
}
