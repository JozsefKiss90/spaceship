package com.codecool.spaceship.service;

import com.codecool.spaceship.model.Location;
import com.codecool.spaceship.model.Role;
import com.codecool.spaceship.model.UserEntity;
import com.codecool.spaceship.model.resource.ResourceType;
import com.codecool.spaceship.model.resource.StationResource;
import com.codecool.spaceship.model.ship.MinerShip;
import com.codecool.spaceship.model.ship.MinerShipManager;
import com.codecool.spaceship.model.ship.shipparts.Color;
import com.codecool.spaceship.model.station.SpaceStation;
import com.codecool.spaceship.model.station.SpaceStationManager;
import com.codecool.spaceship.repository.LocationRepository;
import com.codecool.spaceship.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class Initializer {

    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public Initializer(UserRepository userRepository, LocationRepository locationRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.locationRepository = locationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void initialize() {
        if (userRepository.existsByRole(Role.ADMIN)) {
            return;
        }

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
        minerShip.setEngineLevel(2);
        minerShip.setShieldEnergy(20);
        minerShip.setDrillLevel(2);

        SpaceStation spaceStation = SpaceStationManager.createNewSpaceStation("Station ONE");
        spaceStation.setStorageLevel(5);

        StationResource metal = StationResource.builder()
                .resourceType(ResourceType.METAL)
                .quantity(10000)
                .build();

        StationResource crystal = StationResource.builder()
                .resourceType(ResourceType.CRYSTAL)
                .quantity(10000)
                .build();

        StationResource plutonium = StationResource.builder()
                .resourceType(ResourceType.PLUTONIUM)
                .quantity(10000)
                .build();

        StationResource silicone = StationResource.builder()
                .resourceType(ResourceType.SILICONE)
                .quantity(10000)
                .build();

        minerShip.setUser(user);
        minerShip.setStation(spaceStation);

        spaceStation.setHangar(Set.of(minerShip));
        spaceStation.setResources(Set.of(metal, crystal, silicone, plutonium));
        spaceStation.setUser(user);

        user.setSpaceStation(spaceStation);



        userRepository.save(user);



        Location morpheus = Location.builder()
                .name("Morpheus")
                .distanceFromStation(1)
                .resourceType(ResourceType.METAL)
                .build();
        locationRepository.save(morpheus);
    }
}
