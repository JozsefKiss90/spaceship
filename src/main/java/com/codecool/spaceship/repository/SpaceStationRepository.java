package com.codecool.spaceship.repository;

import com.codecool.spaceship.model.station.SpaceStation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpaceStationRepository extends JpaRepository<SpaceStation, Long> {

}
