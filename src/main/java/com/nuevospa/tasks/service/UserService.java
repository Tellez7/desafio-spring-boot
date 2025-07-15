package com.nuevospa.tasks.service;

import com.nuevospa.tasks.entity.UserEntity;
import com.nuevospa.tasks.model.UserDto;

import java.util.List;
import java.util.Map;

public interface UserService {

    //TODO: pagination
    List<UserDto> findAll(int page, int size);

    UserDto findById(Long id);

    UserDto findByUsername(String username);

    UserDto create(UserDto dto, String username);

    UserDto update(Long id, UserDto dto, String username);

    UserDto patch(Long id, Map<String, Object> changes, String username);

    void delete(Long id, String username);

    UserDto entityToDto(UserEntity entity);

    UserEntity dtoToEntity(UserDto dto);
}
