package com.codecool.spaceship.repository;

import com.codecool.spaceship.model.resource.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceRepository extends JpaRepository<Resource, Long> {
}
