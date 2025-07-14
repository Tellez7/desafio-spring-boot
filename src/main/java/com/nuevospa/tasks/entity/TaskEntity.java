package com.nuevospa.tasks.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tareas")
@Getter
@Setter
//TODO: pasa algo con builder?
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private TaskStatusEntity status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity owner;
}
