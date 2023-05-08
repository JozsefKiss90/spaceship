package com.codecool.spaceship.repository;

import com.codecool.spaceship.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
