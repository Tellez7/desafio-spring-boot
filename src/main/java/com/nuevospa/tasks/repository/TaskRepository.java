package com.nuevospa.tasks.repository;

import com.nuevospa.tasks.entity.TaskEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    Page<TaskEntity> findByUserUsername(String username, Pageable pageable);

    Page<TaskEntity> findByUserUsernameAndStatusName(String username, String status, Pageable pageable);

}
