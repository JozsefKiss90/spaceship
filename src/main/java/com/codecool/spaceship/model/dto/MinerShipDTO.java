package com.codecool.spaceship.model.dto;


import com.codecool.spaceship.model.exception.NoSuchPartException;
import com.codecool.spaceship.model.resource.ShipResource;
import com.codecool.spaceship.model.ship.MinerShip;
import com.codecool.spaceship.model.ship.SpaceShip;
import com.codecool.spaceship.model.ship.SpaceShipService;
import com.codecool.spaceship.model.ship.shipparts.Color;

import java.util.Set;
import java.util.stream.Collectors;

public record MinerShipDTO(Long id, String name, Color color, int engineLevel, int shieldLevel, int shieldEnergy, Set<ShipResource> resources, int storageLevel) {
    public MinerShipDTO(MinerShip minerShip) {
        this(minerShip.getId(), minerShip.getName(), minerShip.getColor(), minerShip.getEngineLevel(), minerShip.getShieldLevel(), minerShip.getEngineLevel(), minerShip.getResources(), minerShip.getStorageLevel());
    }

}
