package com.codecool.spaceship.model.dto;


import com.codecool.spaceship.model.resource.ResourceType;
import com.codecool.spaceship.model.ship.shipparts.Color;

import java.util.Map;

public record MinerShipDTO(
        Long id,
        String name,
        Color color,
        long missionId,
        int engineLevel,
        double maxSpeed,
        int shieldLevel,
        int shieldEnergy,
        int maxShieldEnergy,
        int drillLevel,
        int drillEfficiency,
        int storageLevel,
        int maxStorageCapacity,
        Map<ResourceType, Integer> resources
) {

}
