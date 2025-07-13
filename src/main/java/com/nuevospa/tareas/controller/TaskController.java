package com.nuevospa.tareas.controller;

import com.nuevospa.tareas.model.TaskDto;
import com.nuevospa.tareas.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
//TODO: colocar api
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

  private final TaskService service;

  @GetMapping
  public List<TaskDto> list(Principal principal) {
    return service.findAll(principal.getName());
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public TaskDto create(@RequestBody @Valid TaskDto dto, Principal p) {
    return service.create(dto, p.getName());
  }

  // GET /tasks/{id}, PUT, DELETE similares
}
