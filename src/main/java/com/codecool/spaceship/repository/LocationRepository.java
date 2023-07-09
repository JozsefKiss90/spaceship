package com.codecool.spaceship.repository;

import com.codecool.spaceship.model.UserEntity;
import com.codecool.spaceship.model.location.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {

    List<Location> findAllByUser(UserEntity user);
}
