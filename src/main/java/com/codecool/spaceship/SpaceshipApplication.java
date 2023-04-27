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
        ShipResourceRepository shipResourceRepository = applicationContext.getBean(ShipResourceRepository.class);
        StationResourceRepository stationResourceRepository = applicationContext.getBean(StationResourceRepository.class);
        MinerShipRepository minerShipRepository = applicationContext.getBean(MinerShipRepository.class);
        SpaceStationRepository spaceStationRepository = applicationContext.getBean(SpaceStationRepository.class);
        Set<ShipResource> shipResources = new HashSet<>() {{
            add(ShipResource.builder()
                    .resourceType(ResourceType.METAL)
                    .quantity(10)
                    .build());
            add(ShipResource.builder()
                    .resourceType(ResourceType.CRYSTAL)
                    .quantity(5)
                    .build());
        }};
        Set<StationResource> stationResources = new HashSet<>() {{
            add(StationResource.builder()
                    .resourceType(ResourceType.METAL)
                    .quantity(10)
                    .build());
            add(StationResource.builder()
                    .resourceType(ResourceType.CRYSTAL)
                    .quantity(5)
                    .build());
        }};

        shipResourceRepository.saveAll(shipResources);
        stationResourceRepository.saveAll(stationResources);

        MinerShip minerShip = new MinerShip();
        minerShip.setName("minership");
        minerShip.setColor(Color.DIAMOND);
        minerShip.setEngineLevel(2);
        minerShip.setShieldLevel(3);
        minerShip.setShieldEnergy(100);
        minerShip.setDrillLevel(2);
        minerShip.setResources(shipResources);
        minerShip.setStorageLevel(1);
        minerShipRepository.save(minerShip);

        Set<SpaceShip> hangar = new HashSet<>() {{
            add(minerShip);
        }};

        SpaceStation spaceStation = new SpaceStation();
        spaceStation.setName("spaceStation");
        spaceStation.setStorageLevelIndex(3);
        spaceStation.setHangarLevelIndex(1);
        spaceStation.setHangar(hangar);
        spaceStation.setResources(stationResources);
        spaceStationRepository.save(spaceStation);

    }


}
