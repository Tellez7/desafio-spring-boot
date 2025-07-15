package com.nuevospa.tasks.repository;

import com.nuevospa.tasks.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

//TODO: doc demas
/**
 * Acceso a datos para la entidad TaskEntity.
 * Hereda las operaciones CRUD básicas de JpaRepository.
 */
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

}
