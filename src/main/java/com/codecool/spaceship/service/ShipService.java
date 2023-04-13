package com.codecool.spaceship.service;

import com.codecool.spaceship.model.dto.ShipDTO;
import com.codecool.spaceship.model.ship.MinerShip;
import com.codecool.spaceship.model.ship.SpaceShip;
import com.codecool.spaceship.model.ship.shipparts.Color;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ShipService {

    private final Set<SpaceShip> ships;

    public ShipService() {
        this.ships = new HashSet<>();
        ships.add(new MinerShip("Endurance", Color.DIAMOND));
    }


    public Set<ShipDTO> getAllShipsFromBase() {
        return ships.stream()
                .map(ShipDTO::new)
                .collect(Collectors.toSet());
    }
}
