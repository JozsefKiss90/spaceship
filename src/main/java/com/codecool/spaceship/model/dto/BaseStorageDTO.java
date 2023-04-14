package com.codecool.spaceship.model.dto;

import com.codecool.spaceship.model.Resource;
import com.codecool.spaceship.model.base.BaseStorage;

import java.util.Map;

public record BaseStorageDTO(Map<Resource, Integer> resources, int level, int capacity, int freeSpace) {

    public BaseStorageDTO(BaseStorage storage) {
        this(storage.getStoredItems(), storage.getCurrentLevel(), storage.getCurrentCapacity(), storage.getCurrentAvailableStorageSpace());
    }
}
