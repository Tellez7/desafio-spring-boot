package com.nuevospa.tasks.repository;

import com.nuevospa.tasks.entity.TaskStatusEntity;
import com.nuevospa.tasks.util.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskStatusRepository extends JpaRepository<TaskStatusEntity, Long> {

    Optional<TaskStatusEntity> findByName(TaskStatus name);
}
