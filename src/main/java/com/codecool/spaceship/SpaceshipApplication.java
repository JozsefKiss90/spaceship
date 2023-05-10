package com.codecool.spaceship;

import com.codecool.spaceship.model.Location;
import com.codecool.spaceship.model.resource.ResourceType;
import com.codecool.spaceship.model.ship.MinerShip;
import com.codecool.spaceship.model.ship.MinerShipManager;
import com.codecool.spaceship.model.ship.SpaceShip;
import com.codecool.spaceship.model.ship.shipparts.Color;
import com.codecool.spaceship.model.station.SpaceStation;
import com.codecool.spaceship.model.station.SpaceStationManager;
import com.codecool.spaceship.repository.LocationRepository;
import com.codecool.spaceship.repository.SpaceStationRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Set;

@SpringBootApplication
public class SpaceshipApplication {

    public static void main(String[] args) {

        ApplicationContext applicationContext = SpringApplication.run(SpaceshipApplication.class, args);

        SpaceStationRepository spaceStationRepository = applicationContext.getBean(SpaceStationRepository.class);
        LocationRepository locationRepository = applicationContext.getBean(LocationRepository.class);

        MinerShip minerShip = MinerShipManager.createNewMinerShip("Built2Mine", Color.DIAMOND);
        minerShip.setShieldLevel(3);
        minerShip.setShieldEnergy(100);
        minerShip.setDrillLevel(5);
        minerShip.setEngineLevel(5);

        Set<SpaceShip> hangar = Set.of(minerShip);

        SpaceStation spaceStation = SpaceStationManager.createNewSpaceStation("Station ONE");
        spaceStationRepository.save(spaceStation);
        spaceStation.setHangar(hangar);
        spaceStationRepository.save(spaceStation);

        Location morpheus = Location.builder()
                .name("Morpheus")
                .distanceFromStation(0)
                .resourceType(ResourceType.METAL)
                .build();
        locationRepository.save(morpheus);

    }


}
