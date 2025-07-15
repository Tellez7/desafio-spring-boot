package com.nuevospa.tasks.controller;

import com.nuevospa.tasks.model.TaskStatusDto;
import com.nuevospa.tasks.model.UserDto;
import com.nuevospa.tasks.service.UserService;
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
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${api.path-users}")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @Operation(
            summary = "Listar usuarios",
            description = "Devuelve todas los usuarios"
    )
    @ApiResponse(responseCode = "200",
            description = "Lista de usuarios",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserDto.class))))
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserDto> findAll(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "20") int size) {
        return service.findAll(page, size);
    }

    @Operation(
            summary = "Obtener usuarios por ID",
            description = "Busca un usuarios por su identificador"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = @Content(schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "404",
                    description = "Usuario no encontrad",
                    content = @Content)
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserDto findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @Operation(
            summary = "Crear usuario",
            description = "Crea un nuevo usuario y devuelve la entidad creada"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201",
                    content = @Content(schema = @Schema(implementation = UserDto.class))),
            //TODO: check
            @ApiResponse(responseCode = "400",
                    description = "Datos inválidos",
                    content = @Content)
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> create(
            @Valid @RequestBody UserDto dto,
            @AuthenticationPrincipal UserDetails loggedUser) {
        UserDto created = service.create(dto, loggedUser.getUsername());
        return ResponseEntity
                .created(URI.create("/api/users/" + created.getId()))
                .body(created);
    }

    @Operation(
            summary = "Reemplazar usuario",
            description = "Reemplaza **completamente** el usuario con los datos enviados"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = @Content(schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "404",
                    description = "Usuario no encontrado",
                    content = @Content)
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserDto update(@PathVariable Long id,
                          @Valid @RequestBody UserDto dto,
                          @AuthenticationPrincipal UserDetails loggedUser) {
        return service.update(id, dto, loggedUser.getUsername());
    }

    @Operation(
            summary = "Actualizar parcialmente un usuario",
            description = "Modifica solo los campos indicados usando JSON Merge Patch"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = @Content(schema = @Schema(implementation = TaskStatusDto.class))),
            @ApiResponse(responseCode = "404",
                    description = "Usuario no encontrada",
                    content = @Content)
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping(value = "/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<UserDto> patch(@PathVariable Long id,
                                         @RequestBody Map<String, Object> changes,
                                         @AuthenticationPrincipal UserDetails loggedUser) {
        UserDto updated = service.patch(id, changes, loggedUser.getUsername());
        return ResponseEntity.ok(updated);
    }

    @Operation(
            summary = "Eliminar usuario",
            description = "Borra el usuario indicado"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Usuario no encontrado",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal UserDetails principal) {
        service.delete(id, principal.getUsername());
        return ResponseEntity.noContent().build();
    }
}
