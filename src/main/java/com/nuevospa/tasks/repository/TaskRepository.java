package com.nuevospa.tasks.repository;

import com.nuevospa.tasks.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

}
