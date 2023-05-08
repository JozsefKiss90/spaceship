package com.codecool.spaceship.model.dto;


import com.codecool.spaceship.model.resource.ShipResource;
import com.codecool.spaceship.model.ship.MinerShip;
import com.codecool.spaceship.model.ship.shipparts.Color;

import java.util.Set;

public record MinerShipDTO(Long id, String name, Color color, int engineLevel, int shieldLevel, int shieldEnergy, int drillLevel, int storageLevel, Set<ShipResource> resources) {
    public MinerShipDTO(MinerShip minerShip) {
        this(minerShip.getId(),
                minerShip.getName(),
                minerShip.getColor(),
                minerShip.getEngineLevel(),
                minerShip.getShieldLevel(),
                minerShip.getShieldEnergy(),
                minerShip.getDrillLevel(),
                minerShip.getStorageLevel(),
                minerShip.getResources()
        );
    }

}
