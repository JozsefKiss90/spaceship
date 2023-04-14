package com.codecool.spaceship.model.dto;

import com.codecool.spaceship.model.Resource;
import com.codecool.spaceship.model.station.SpaceStationStorage;

import java.util.Map;

public record BaseStorageDTO(Map<Resource, Integer> resources, int level, int capacity, int freeSpace) {

    public BaseStorageDTO(SpaceStationStorage storage) {
        this(storage.getStoredItems(), storage.getCurrentLevel(), storage.getCurrentCapacity(), storage.getCurrentAvailableStorageSpace());
    }
}
