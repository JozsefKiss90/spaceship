package com.codecool.spaceship.model.station;

import com.codecool.spaceship.model.*;
import com.codecool.spaceship.model.exception.InvalidLevelException;
import com.codecool.spaceship.model.exception.StorageException;
import com.codecool.spaceship.model.exception.UpgradeNotAvailableException;
import com.codecool.spaceship.model.resource.ResourceType;
import com.codecool.spaceship.model.resource.StationResource;

import java.util.*;

public class StationStorageManager implements Upgradeable {
    private static final List<Level<Integer>> UPGRADE_LEVELS = new ArrayList<>() {{
        add(new Level<>(1, 20, null));
        add(new Level<>(2, 50, new HashMap<>() {{
            put(ResourceType.METAL, 5);
        }}));
        add(new Level<>(3, 100, new HashMap<>() {{
            put(ResourceType.METAL, 20);
            put(ResourceType.SILICONE, 10);
        }}));
        add(new Level<>(4, 500, new HashMap<>() {{
            put(ResourceType.METAL, 50);
            put(ResourceType.SILICONE, 20);
        }}));
        add(new Level<>(5, 1000, new HashMap<>() {{
            put(ResourceType.METAL, 300);
            put(ResourceType.SILICONE, 150);
            put(ResourceType.PLUTONIUM, 20);
        }}));
    }};
    private static final int MAX_LEVEL_INDEX = UPGRADE_LEVELS.size() - 1;

    private int currentLevelIndex;
    private final Set<StationResource> storedItems;

    public StationStorageManager() {
        currentLevelIndex = 0;
        storedItems = new HashSet<>();
    }

    public StationStorageManager(int currentLevel, Set<StationResource> storedItems) {
        int currentLevelIndex = currentLevel - 1;
        if (currentLevelIndex < 0) {
            throw new InvalidLevelException("Level index can't be lower than 0");
        } else if (currentLevelIndex > MAX_LEVEL_INDEX) {
            throw new InvalidLevelException("Level index can't be higher than %d".formatted(MAX_LEVEL_INDEX));
        }
        this.currentLevelIndex = currentLevelIndex;
        this.storedItems = storedItems;
    }

    public int getCurrentCapacity() {
        return UPGRADE_LEVELS.get(currentLevelIndex).effect();
    }

    public int getCurrentAvailableStorageSpace() {
        return getCurrentCapacity() - storedItems.stream().mapToInt(StationResource::getQuantity).sum();
    }

    public Map<ResourceType, Integer> getStoredItems() {
        Map<ResourceType, Integer> resources = new HashMap<>();
        for (StationResource storedItem : storedItems) {
            resources.put(storedItem.getResourceType(), storedItem.getQuantity());
        }
        return resources;
    }

    public boolean addResource(ResourceType resourceType, int quantity) throws StorageException {
        if (quantity < getCurrentAvailableStorageSpace()) {
            StationResource resource = storedItems.stream()
                    .filter(sr -> sr.getResourceType() == resourceType)
                    .findFirst()
                    .orElse(null);
            if (resource != null) {
                resource.setQuantity(resource.getQuantity() + quantity);
            } else {
                resource = StationResource.builder()
                        .resourceType(resourceType)
                        .quantity(quantity)
                        .build();
                storedItems.add(resource);
            }
            return true;
        }
        throw new StorageException("Not enough storage space");
    }

    public boolean hasResource(ResourceType resourceType, int quantity) {
        return storedItems.stream()
                .anyMatch(stationResource -> stationResource.getResourceType() == resourceType
                        && stationResource.getQuantity() >= quantity);

    }

    public void removeResource(ResourceType resourceType, int quantity) throws StorageException {
        if (hasResource(resourceType, quantity)) {
            StationResource resource = storedItems.stream()
                    .filter(sr -> sr.getResourceType() == resourceType)
                    .findFirst()
                    .orElse(null);
            resource.setQuantity(resource.getQuantity() - quantity);
        } else throw new StorageException("Not enough resource");
    }

    @Override
    public Map<ResourceType, Integer> getUpgradeCost() throws UpgradeNotAvailableException {
        if (currentLevelIndex < MAX_LEVEL_INDEX) return new HashMap<>(UPGRADE_LEVELS.get(currentLevelIndex + 1).cost());
        throw new UpgradeNotAvailableException("Already on max level");
    }

    @Override
    public void upgrade() {
        if (currentLevelIndex < MAX_LEVEL_INDEX) currentLevelIndex++;
    }

    @Override
    public int getCurrentLevel() {
        return currentLevelIndex + 1;
    }
}
