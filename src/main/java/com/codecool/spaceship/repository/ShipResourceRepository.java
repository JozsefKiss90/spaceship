package com.codecool.spaceship.repository;

import com.codecool.spaceship.model.resource.ShipResource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipResourceRepository extends JpaRepository<ShipResource, Long> {
}
