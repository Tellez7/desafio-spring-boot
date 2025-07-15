package com.nuevospa.tasks.model;

//TODO: check
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskStatusDto {

    private Long id;
    private String name;
}