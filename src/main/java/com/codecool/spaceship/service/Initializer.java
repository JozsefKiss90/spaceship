package com.codecool.spaceship.service;

import com.codecool.spaceship.model.*;
import com.codecool.spaceship.model.resource.ResourceType;
import com.codecool.spaceship.model.ship.MinerShip;
import com.codecool.spaceship.model.ship.MinerShipManager;
import com.codecool.spaceship.model.ship.shipparts.Color;
import com.codecool.spaceship.model.station.SpaceStation;
import com.codecool.spaceship.model.station.SpaceStationManager;
import com.codecool.spaceship.repository.LevelRepository;
import com.codecool.spaceship.repository.LocationRepository;
import com.codecool.spaceship.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class Initializer {

    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final LevelRepository levelRepository;
    private final PasswordEncoder passwordEncoder;
    private final LevelService levelService;

    @Autowired
    public Initializer(UserRepository userRepository, LocationRepository locationRepository, LevelRepository levelRepository, PasswordEncoder passwordEncoder, LevelService levelService) {
        this.userRepository = userRepository;
        this.locationRepository = locationRepository;
        this.levelRepository = levelRepository;
        this.passwordEncoder = passwordEncoder;
        this.levelService = levelService;
    }

    public void initialize() {
        if (userRepository.existsByRole(Role.ADMIN)) {
            return;
        }
        initLevels();
        initUsers();
        initLocations();
    }

    private void initUsers() {
        UserEntity admin = UserEntity.builder()
                .username("Mr. Admin")
                .email("admin@admail.min")
                .password(passwordEncoder.encode("password"))
                .role(Role.ADMIN)
                .build();
        MinerShip minerShip1 = MinerShipManager.createNewMinerShip(levelService,"Adminship", Color.DIAMOND);
        SpaceStation spaceStation1 = SpaceStationManager.createNewSpaceStation("Admin-i-station");

        minerShip1.setUser(admin);
        minerShip1.setStation(spaceStation1);

        spaceStation1.setHangar(Set.of(minerShip1));
        spaceStation1.setUser(admin);

        admin.setSpaceStation(spaceStation1);
        userRepository.save(admin);

        UserEntity user = UserEntity.builder()
                .username("TestGuy")
                .email("test@testmail.tst")
                .password(passwordEncoder.encode("password"))
                .role(Role.USER)
                .build();

        MinerShip minerShip2 = MinerShipManager.createNewMinerShip(levelService,"Built2Mine", Color.DIAMOND);
        minerShip2.setEngineLevel(1);
        minerShip2.setShieldEnergy(20);
        minerShip2.setDrillLevel(2);

        SpaceStation spaceStation2 = SpaceStationManager.createNewSpaceStation("Station ONE");
        spaceStation2.setStorageLevel(5);

        minerShip2.setUser(user);
        minerShip2.setStation(spaceStation2);

        spaceStation2.setHangar(Set.of(minerShip2));
        spaceStation2.setStoredResources(Map.of(
                ResourceType.METAL, 10000,
                ResourceType.CRYSTAL, 10000,
                ResourceType.PLUTONIUM, 10000,
                ResourceType.SILICONE, 10000
        ));
        spaceStation2.setUser(user);

        user.setSpaceStation(spaceStation2);

        userRepository.save(user);

    }

    private void initLocations() {
        Location morpheus = Location.builder()
                .name("Morpheus")
                .distanceFromStation(1)
                .resourceType(ResourceType.METAL)
                .build();
        Location koboh = Location.builder()
                .name("Koboh")
                .distanceFromStation(4)
                .resourceType(ResourceType.CRYSTAL)
                .build();
        Location palaven = Location.builder()
                .name("Palaven")
                .distanceFromStation(11)
                .resourceType(ResourceType.SILICONE)
                .build();
        Location crosie = Location.builder()
                .name("Crosie 3W")
                .distanceFromStation(13)
                .resourceType(ResourceType.PLUTONIUM)
                .build();
        locationRepository.saveAll(Set.of(morpheus, koboh, palaven, crosie));
    }

    private void initLevels() {
        Level engine1 = Level.builder()
                .type(UpgradeableType.ENGINE)
                .level(1)
                .effect(400)
                .max(false)
                .cost(Map.of())
                .build();
        Level engine2 = Level.builder()
                .type(UpgradeableType.ENGINE)
                .level(2)
                .effect(5)
                .max(false)
                .cost(Map.of(
                        ResourceType.SILICONE, 10,
                        ResourceType.CRYSTAL, 5
                ))
                .build();
        Level engine3 = Level.builder()
                .type(UpgradeableType.ENGINE)
                .level(3)
                .effect(10)
                .max(false)
                .cost(Map.of(
                        ResourceType.SILICONE, 25,
                        ResourceType.CRYSTAL, 5
                ))
                .build();
        Level engine4 = Level.builder()
                .type(UpgradeableType.ENGINE)
                .level(4)
                .effect(20)
                .max(false)
                .cost(Map.of(
                        ResourceType.SILICONE, 100,
                        ResourceType.CRYSTAL, 10
                ))
                .build();
        Level engine5 = Level.builder()
                .type(UpgradeableType.ENGINE)
                .level(5)
                .effect(50)
                .max(true)
                .cost(Map.of(
                        ResourceType.SILICONE, 150,
                        ResourceType.CRYSTAL, 20,
                        ResourceType.PLUTONIUM, 5
                ))
                .build();

        Level drill1 = Level.builder()
                .type(UpgradeableType.DRILL)
                .level(1)
                .effect(1200)
                .max(false)
                .cost(Map.of())
                .build();
        Level drill2 = Level.builder()
                .type(UpgradeableType.DRILL)
                .level(2)
                .effect(10)
                .max(false)
                .cost(Map.of(
                        ResourceType.METAL, 20,
                        ResourceType.CRYSTAL, 10
                ))
                .build();
        Level drill3 = Level.builder()
                .type(UpgradeableType.DRILL)
                .level(3)
                .effect(20)
                .max(false)
                .cost(Map.of(
                        ResourceType.METAL, 100,
                        ResourceType.CRYSTAL, 50
                ))
                .build();
        Level drill4 = Level.builder()
                .type(UpgradeableType.DRILL)
                .level(4)
                .effect(35)
                .max(false)
                .cost(Map.of(
                        ResourceType.METAL, 200,
                        ResourceType.CRYSTAL, 100
                ))
                .build();
        Level drill5 = Level.builder()
                .type(UpgradeableType.DRILL)
                .level(5)
                .effect(50)
                .max(true)
                .cost(Map.of(
                        ResourceType.METAL, 400,
                        ResourceType.CRYSTAL, 150,
                        ResourceType.PLUTONIUM, 10
                ))
                .build();

        Level shield1 = Level.builder()
                .type(UpgradeableType.SHIELD)
                .level(1)
                .effect(20)
                .max(false)
                .cost(Map.of())
                .build();
        Level shield2 = Level.builder()
                .type(UpgradeableType.SHIELD)
                .level(2)
                .effect(50)
                .max(false)
                .cost(Map.of(
                        ResourceType.CRYSTAL, 20
                ))
                .build();
        Level shield3 = Level.builder()
                .type(UpgradeableType.SHIELD)
                .level(3)
                .effect(100)
                .max(false)
                .cost(Map.of(
                        ResourceType.CRYSTAL, 40,
                        ResourceType.SILICONE, 10
                ))
                .build();
        Level shield4 = Level.builder()
                .type(UpgradeableType.SHIELD)
                .level(4)
                .effect(150)
                .max(false)
                .cost(Map.of(
                        ResourceType.CRYSTAL, 100,
                        ResourceType.SILICONE, 20
                ))
                .build();
        Level shield5 = Level.builder()
                .type(UpgradeableType.SHIELD)
                .level(5)
                .effect(200)
                .max(true)
                .cost(Map.of(
                        ResourceType.CRYSTAL, 150,
                        ResourceType.SILICONE, 40,
                        ResourceType.PLUTONIUM, 5
                ))
                .build();

        Level shipStorage1 = Level.builder()
                .type(UpgradeableType.SHIP_STORAGE)
                .level(1)
                .effect(10)
                .max(false)
                .cost(Map.of())
                .build();
        Level shipStorage2 = Level.builder()
                .type(UpgradeableType.SHIP_STORAGE)
                .level(2)
                .effect(25)
                .max(false)
                .cost(Map.of(
                        ResourceType.METAL, 20,
                        ResourceType.SILICONE, 10
                ))
                .build();
        Level shipStorage3 = Level.builder()
                .type(UpgradeableType.SHIP_STORAGE)
                .level(3)
                .effect(50)
                .max(false)
                .cost(Map.of(
                        ResourceType.METAL, 100,
                        ResourceType.SILICONE, 50
                ))
                .build();
        Level shipStorage4 = Level.builder()
                .type(UpgradeableType.SHIP_STORAGE)
                .level(4)
                .effect(75)
                .max(false)
                .cost(Map.of(
                        ResourceType.METAL, 200,
                        ResourceType.SILICONE, 100
                ))
                .build();
        Level shipStorage5 = Level.builder()
                .type(UpgradeableType.SHIP_STORAGE)
                .level(5)
                .effect(100)
                .max(true)
                .cost(Map.of(
                        ResourceType.METAL, 400,
                        ResourceType.SILICONE, 150,
                        ResourceType.PLUTONIUM, 10
                ))
                .build();

        Level stationStorage1 = Level.builder()
                .type(UpgradeableType.STATION_STORAGE)
                .level(1)
                .effect(20)
                .max(false)
                .cost(Map.of())
                .build();
        Level stationStorage2 = Level.builder()
                .type(UpgradeableType.STATION_STORAGE)
                .level(2)
                .effect(50)
                .max(false)
                .cost(Map.of(
                        ResourceType.METAL, 5
                ))
                .build();
        Level stationStorage3 = Level.builder()
                .type(UpgradeableType.STATION_STORAGE)
                .level(3)
                .effect(100)
                .max(false)
                .cost(Map.of(
                        ResourceType.METAL, 20,
                        ResourceType.SILICONE, 10
                ))
                .build();
        Level stationStorage4 = Level.builder()
                .type(UpgradeableType.STATION_STORAGE)
                .level(4)
                .effect(500)
                .max(false)
                .cost(Map.of(
                        ResourceType.METAL, 50,
                        ResourceType.SILICONE, 20
                ))
                .build();
        Level stationStorage5 = Level.builder()
                .type(UpgradeableType.STATION_STORAGE)
                .level(5)
                .effect(50000)
                .max(true)
                .cost(Map.of(
                        ResourceType.METAL, 300,
                        ResourceType.SILICONE, 150,
                        ResourceType.PLUTONIUM, 20
                ))
                .build();

        Level hangar1 = Level.builder()
                .type(UpgradeableType.HANGAR)
                .level(1)
                .effect(2)
                .max(false)
                .cost(Map.of())
                .build();
        Level hangar2 = Level.builder()
                .type(UpgradeableType.HANGAR)
                .level(2)
                .effect(4)
                .max(false)
                .cost(Map.of(
                        ResourceType.METAL, 5
                ))
                .build();
        Level hangar3 = Level.builder()
                .type(UpgradeableType.HANGAR)
                .level(3)
                .effect(6)
                .max(false)
                .cost(Map.of(
                        ResourceType.METAL, 20,
                        ResourceType.SILICONE, 20
                ))
                .build();
        Level hangar4 = Level.builder()
                .type(UpgradeableType.HANGAR)
                .level(4)
                .effect(8)
                .max(false)
                .cost(Map.of(
                        ResourceType.METAL, 100,
                        ResourceType.SILICONE, 100,
                        ResourceType.CRYSTAL, 50
                ))
                .build();
        Level hangar5 = Level.builder()
                .type(UpgradeableType.HANGAR)
                .level(5)
                .effect(10)
                .max(true)
                .cost(Map.of(
                        ResourceType.METAL, 500,
                        ResourceType.SILICONE, 150,
                        ResourceType.CRYSTAL, 100
                ))
                .build();

        levelRepository.saveAll(List.of(
                engine1, engine2, engine3, engine4, engine5,
                drill1, drill2, drill3, drill4, drill5,
                shield1, shield2, shield3, shield4, shield5,
                shipStorage1, shipStorage2, shipStorage3, shipStorage4, shipStorage5,
                stationStorage1, stationStorage2, stationStorage3, stationStorage4, stationStorage5,
                hangar1, hangar2, hangar3, hangar4, hangar5
        ));
    }
}
