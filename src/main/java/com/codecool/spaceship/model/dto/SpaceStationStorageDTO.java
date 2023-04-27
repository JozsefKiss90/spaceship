package com.codecool.spaceship.model.dto;

import com.codecool.spaceship.model.resource.ResourceType;
import com.codecool.spaceship.model.station.SpaceStationStorage;

import java.util.Map;

public record SpaceStationStorageDTO(Map<ResourceType, Integer> resources, int level, int capacity, int freeSpace) {

    public SpaceStationStorageDTO(SpaceStationStorage storage) {
        this(storage.getStoredItems(), storage.getCurrentLevel(), storage.getCurrentCapacity(), storage.getCurrentAvailableStorageSpace());
    }
}
