package com.codecool.spaceship;

import com.codecool.spaceship.model.Location;
import com.codecool.spaceship.model.Role;
import com.codecool.spaceship.model.UserEntity;
import com.codecool.spaceship.model.resource.ResourceType;
import com.codecool.spaceship.model.ship.MinerShip;
import com.codecool.spaceship.model.ship.MinerShipManager;
import com.codecool.spaceship.model.ship.shipparts.Color;
import com.codecool.spaceship.model.station.SpaceStation;
import com.codecool.spaceship.model.station.SpaceStationManager;
import com.codecool.spaceship.repository.LocationRepository;
import com.codecool.spaceship.repository.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@SpringBootApplication
public class SpaceshipApplication {

    public static void main(String[] args) {

        ApplicationContext applicationContext = SpringApplication.run(SpaceshipApplication.class, args);
        UserRepository userRepository = applicationContext.getBean(UserRepository.class);
        PasswordEncoder passwordEncoder = applicationContext.getBean(PasswordEncoder.class);
        LocationRepository locationRepository = applicationContext.getBean(LocationRepository.class);

        UserEntity admin = UserEntity.builder()
                .username("Mr. Admin")
                .email("admin@admail.min")
                .password(passwordEncoder.encode("password"))
                .role(Role.ADMIN)
                .build();
        userRepository.save(admin);

        UserEntity user = UserEntity.builder()
                .username("TestGuy")
                .email("test@testmail.tst")
                .password(passwordEncoder.encode("password"))
                .role(Role.USER)
                .build();

        MinerShip minerShip = MinerShipManager.createNewMinerShip("Built2Mine", Color.DIAMOND);
        minerShip.setShieldLevel(3);
        minerShip.setShieldEnergy(100);
        minerShip.setDrillLevel(2);

        SpaceStation spaceStation = SpaceStationManager.createNewSpaceStation("Station ONE");

        minerShip.setUser(user);
        minerShip.setStation(spaceStation);

        spaceStation.setHangar(Set.of(minerShip));
        spaceStation.setUser(user);

        user.setSpaceStation(spaceStation);

        userRepository.save(user);

        Location morpheus = Location.builder()
                .name("Morpheus")
                .distanceFromStation(0)
                .resourceType(ResourceType.METAL)
                .build();
        locationRepository.save(morpheus);
    }


}
