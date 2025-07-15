package com.nuevospa.tasks.repository;

import com.nuevospa.tasks.entity.TaskStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskStatusRepository extends JpaRepository<TaskStatusEntity, Long> {

    Optional<TaskStatusEntity> findByName(String name);
}
