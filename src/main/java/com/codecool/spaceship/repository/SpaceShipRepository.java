package com.codecool.spaceship.repository;

import com.codecool.spaceship.model.ship.SpaceShip;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpaceShipRepository extends JpaRepository<SpaceShip, Long> {
}
