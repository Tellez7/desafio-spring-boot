package com.nuevospa.tasks.controller;

import com.nuevospa.tasks.model.TaskStatusDto;
import com.nuevospa.tasks.service.TaskStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("${api.path-task-statuses}")
@RequiredArgsConstructor
public class TaskStatusController {

    private final TaskStatusService taskStatusService;

    @Operation(
            summary = "Listar estados de tareas",
            description = "Devuelve todas los estados de tareas visibles para el usuario autenticado"
    )
    @ApiResponse(responseCode = "200",
            description = "Lista de estados",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = TaskStatusDto.class))))
    @GetMapping
    public List<TaskStatusDto> findAll() {
        return taskStatusService.findAll();
    }

    @Operation(
            summary = "Obtener estado por ID",
            description = "Busca un estado por su identificador"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = @Content(schema = @Schema(implementation = TaskStatusDto.class))),
            @ApiResponse(responseCode = "404",
                    description = "Estado no encontrada",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<TaskStatusDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(taskStatusService.findById(id));
    }

    @Operation(
            summary = "Crear estado",
            description = "Crea un nuevo estado y devuelve la entidad creada"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201",
                    content = @Content(schema = @Schema(implementation = TaskStatusDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "Datos inválidos",
                    content = @Content)
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TaskStatusDto> create(@Valid @RequestBody TaskStatusDto taskStatusDto) {
        TaskStatusDto created = taskStatusService.create(taskStatusDto);
        return ResponseEntity
                .created(URI.create("/api/statuses/" + created.getId()))
                .body(created);
    }

    @Operation(
            summary = "Reemplazar estado",
            description = "Reemplaza **completamente** el estado con los datos enviados"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = @Content(schema = @Schema(implementation = TaskStatusDto.class))),
            @ApiResponse(responseCode = "404",
                    description = "Estado no encontrado",
                    content = @Content)
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TaskStatusDto> update(
            @PathVariable Long id,
            @RequestBody TaskStatusDto taskStatusDto,
            @AuthenticationPrincipal UserDetails loggedUser) {
        TaskStatusDto updated = taskStatusService.update(id, taskStatusDto, loggedUser.getUsername());
        return ResponseEntity.ok(updated);
    }

    @Operation(
            summary = "Actualizar parcialmente un estado",
            description = "Modifica solo los campos indicados usando JSON Merge Patch"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = @Content(schema = @Schema(implementation = TaskStatusDto.class))),
            @ApiResponse(responseCode = "404",
                    description = "Estado no encontrada",
                    content = @Content)
    })
    @PatchMapping(value = "/{id}", consumes = "application/merge-patch+json")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TaskStatusDto> patch(@PathVariable Long id,
                                               @RequestBody Map<String, Object> changes,
                                               @AuthenticationPrincipal UserDetails loggedUser) {
        TaskStatusDto updated = taskStatusService.patch(id, changes, loggedUser.getUsername());
        return ResponseEntity.ok(updated);
    }

    @Operation(
            summary = "Eliminar estado",
            description = "Borra el estado indicado"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Estado no encontrado",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal UserDetails loggedUser) {
        taskStatusService.delete(id, loggedUser.getUsername());
        return ResponseEntity.noContent().build();
    }
}
