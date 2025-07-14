package com.nuevospa.tasks.service;

import com.nuevospa.tasks.entity.UserEntity;
import com.nuevospa.tasks.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Primary                         // asegura que sea el elegido
@RequiredArgsConstructor
public class JpaUserDetailsService implements UserDetailsService {

    private final UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //TODO: check message y exception
        UserEntity userEntity = userRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return User.withUsername(userEntity.getUsername())
                .password(userEntity.getPassword())       // BCrypt hash
                .roles(userEntity.getRole().replace("ROLE_", ""))
                .build();
    }
}
