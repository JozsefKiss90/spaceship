package com.codecool.spaceship.model.dto;


import com.codecool.spaceship.model.resource.ShipResource;
import com.codecool.spaceship.model.ship.MinerShip;
import com.codecool.spaceship.model.ship.shipparts.Color;
import com.codecool.spaceship.model.ship.shipparts.EngineManager;
import com.codecool.spaceship.model.ship.shipparts.ShieldManager;
import org.apache.catalina.Engine;

import java.util.Set;

public record MinerShipDTO(
        Long id,
        String name,
        Color color,
        String status,
        int engineLevel,
        double maxSpeed,
        int shieldLevel,
        int shieldEnergy,
        int maxShieldEnergy,
        int drillLevel,
        int drillEfficiency,
        int storageLevel,
        int maxStorageCapacity,
        Set<ShipResource> resources) {

}
