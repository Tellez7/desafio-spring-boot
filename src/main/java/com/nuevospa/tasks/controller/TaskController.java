package com.nuevospa.tasks.controller;

import com.nuevospa.tasks.model.TaskDto;
import com.nuevospa.tasks.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
//TODO: colocar api
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

  private final TaskService service;

  //TODO: check principal
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
