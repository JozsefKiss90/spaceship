package com.codecool.spaceship.repository;

import com.codecool.spaceship.model.UserEntity;
import com.codecool.spaceship.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
}
