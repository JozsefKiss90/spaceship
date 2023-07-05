package com.codecool.spaceship.model.dto;


import com.codecool.spaceship.model.mission.Mission;
import com.codecool.spaceship.model.resource.ResourceType;
import com.codecool.spaceship.model.ship.ShipType;
import com.codecool.spaceship.model.ship.shipparts.Color;
import lombok.Getter;

import java.util.Map;

@Getter
public class MinerShipDTO extends ShipDetailDTO {
    private final int drillLevel;
    private final int drillEfficiency;
    private final int storageLevel;
    private final int maxStorageCapacity;
    private final Map<ResourceType, Integer> resources;

    public MinerShipDTO(long id, String name, Color color, ShipType type, Mission mission, int engineLevel, int maxSpeed,
                        int shieldLevel, int shieldEnergy, int maxShieldEnergy, int drillLevel, int drillEfficiency,
                        int storageLevel, int maxStorageCapacity, Map<ResourceType, Integer> resources) {
        super(id, name, color, type, mission, engineLevel, maxSpeed, shieldLevel, shieldEnergy, maxShieldEnergy);
        this.drillLevel = drillLevel;
        this.drillEfficiency = drillEfficiency;
        this.storageLevel = storageLevel;
        this.maxStorageCapacity = maxStorageCapacity;
        this.resources = resources;
    }
}
