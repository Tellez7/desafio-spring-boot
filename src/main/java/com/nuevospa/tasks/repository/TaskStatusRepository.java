package com.nuevospa.tasks.repository;

import com.nuevospa.tasks.entity.TaskStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Acceso a datos para la entidad TaskStatusEntity (“estados_tarea”).
 */
public interface TaskStatusRepository extends JpaRepository<TaskStatusEntity, Long> {

    /**
     * Busca un estado por su nombre (“PENDING”, “DONE”, etc.).
     *
     * @param name nombre del estado
     * @return Optional con el estado si existe
     */
    Optional<TaskStatusEntity> findByName(String name);
}
