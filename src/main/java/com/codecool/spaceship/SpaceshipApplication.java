package com.codecool.spaceship;

import com.codecool.spaceship.model.ship.MinerShip;
import com.codecool.spaceship.model.ship.SpaceShip;
import com.codecool.spaceship.model.ship.shipparts.Color;
import com.codecool.spaceship.model.station.SpaceStation;
import com.codecool.spaceship.repository.SpaceStationRepository;
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

        MinerShip minerShip = new MinerShip();
        minerShip.setName("minership");
        minerShip.setColor(Color.DIAMOND);
        minerShip.setEngineLevel(2);
        minerShip.setShieldLevel(3);
        minerShip.setShieldEnergy(100);
        minerShip.setDrillLevel(2);
        minerShip.setResources(new HashSet<>());
        minerShip.setStorageLevel(1);


        Set<SpaceShip> hangar = new HashSet<>() {{
            add(minerShip);
        }};

        SpaceStation spaceStation = new SpaceStation();
        spaceStation.setName("spaceStation");
        spaceStationRepository.save(spaceStation);
        spaceStation.setHangar(hangar);
        spaceStationRepository.save(spaceStation);

    }


}
