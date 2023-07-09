package com.codecool.spaceship.repository;

import com.codecool.spaceship.model.location.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
