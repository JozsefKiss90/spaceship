package com.codecool.spaceship.repository;

import com.codecool.spaceship.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
}
