package com.codecool.spaceship.repository;

import com.codecool.spaceship.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
