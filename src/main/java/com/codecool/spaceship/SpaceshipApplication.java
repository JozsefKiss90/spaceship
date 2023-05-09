package com.codecool.spaceship;

import com.codecool.spaceship.model.UserEntity;
import com.codecool.spaceship.model.UserRole;
import com.codecool.spaceship.model.ship.MinerShip;
import com.codecool.spaceship.model.ship.MinerShipManager;
import com.codecool.spaceship.model.ship.shipparts.Color;
import com.codecool.spaceship.model.station.SpaceStation;
import com.codecool.spaceship.model.station.SpaceStationManager;
import com.codecool.spaceship.repository.SpaceStationRepository;
import com.codecool.spaceship.repository.UserRepository;
import com.codecool.spaceship.repository.UserRoleRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.Set;

@SpringBootApplication
public class SpaceshipApplication {

    public static void main(String[] args) {

        ApplicationContext applicationContext = SpringApplication.run(SpaceshipApplication.class, args);
        UserRoleRepository userRoleRepository = applicationContext.getBean(UserRoleRepository.class);
        UserRepository userRepository = applicationContext.getBean(UserRepository.class);
        SpaceStationRepository spaceStationRepository = applicationContext.getBean(SpaceStationRepository.class);

        UserRole roleAdmin = UserRole.builder().role("ROLE_ADMIN").build();
        UserRole roleUser = UserRole.builder().role("ROLE_USER").build();
        userRoleRepository.saveAll(List.of(roleAdmin, roleUser));

        UserEntity user = UserEntity.builder()
                .email("test@testmail.tst")
                .username("Player")
                .password("password")
                .roles(Set.of(roleUser))
                .build();
        user = userRepository.save(user);

        MinerShip minerShip = MinerShipManager.createNewMinerShip("Built2Mine", Color.DIAMOND);
        minerShip.setShieldLevel(3);
        minerShip.setShieldEnergy(100);
        minerShip.setDrillLevel(2);


        SpaceStation spaceStation = SpaceStationManager.createNewSpaceStation("Station ONE");
        spaceStation.setUser(user);
        spaceStationRepository.save(spaceStation);
        spaceStation.setHangar(Set.of(minerShip));
        spaceStationRepository.save(spaceStation);

    }


}
