package com.codecool.spaceship;

import com.codecool.spaceship.model.resource.ResourceType;
import com.codecool.spaceship.model.resource.ShipResource;
import com.codecool.spaceship.model.resource.StationResource;
import com.codecool.spaceship.model.ship.MinerShip;
import com.codecool.spaceship.model.ship.SpaceShip;
import com.codecool.spaceship.model.ship.shipparts.Color;
import com.codecool.spaceship.model.station.SpaceStation;
import com.codecool.spaceship.repository.MinerShipRepository;
import com.codecool.spaceship.repository.ShipResourceRepository;
import com.codecool.spaceship.repository.SpaceStationRepository;
import com.codecool.spaceship.repository.StationResourceRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class SpaceshipApplication {

    public static void main(String[] args) {

        ApplicationContext applicationContext = SpringApplication.run(SpaceshipApplication.class, args);

        SpaceStationRepository spaceStationRepository = applicationContext.getBean(SpaceStationRepository.class);




        Set<SpaceShip> hangar = new HashSet<>();

        SpaceStation spaceStation = new SpaceStation();
        spaceStation.setName("spaceStation");
        spaceStationRepository.save(spaceStation);

    }


}
