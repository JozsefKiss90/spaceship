package com.codecool.spaceship.model.ship.shipparts;

import com.codecool.spaceship.model.Level;
import com.codecool.spaceship.model.exception.InvalidLevelException;
import com.codecool.spaceship.model.resource.ResourceType;
import com.codecool.spaceship.model.Upgradeable;
import com.codecool.spaceship.model.exception.StorageException;
import com.codecool.spaceship.model.exception.UpgradeNotAvailableException;
import com.codecool.spaceship.model.resource.ShipResource;

import java.util.*;

public class ShipStorageManager implements Upgradeable {

    private static final List<Level<Integer>> LEVELS = List.of(
            new Level<>(1, 10, Map.of()),
            new Level<>(2, 25,
                    Map.of(
                            ResourceType.METAL, 20,
                            ResourceType.SILICONE, 10
                    )),
            new Level<>(3, 50,
                    Map.of(
                            ResourceType.METAL, 100,
                            ResourceType.SILICONE, 50
                    )),
            new Level<>(4, 80,
                    Map.of(
                            ResourceType.METAL, 200,
                            ResourceType.SILICONE, 100
                    )),
            new Level<>(5, 100,
                    Map.of(
                            ResourceType.METAL, 400,
                            ResourceType.SILICONE, 150,
                            ResourceType.PLUTONIUM, 10
                    ))
    );
    private static final int MAX_LEVEL_INDEX = LEVELS.size() - 1;
    private int currentLevelIndex;
    private final Set<ShipResource> storedResources;

    public ShipStorageManager() {
        currentLevelIndex = 0;
        storedResources = new HashSet<>();
    }

    public ShipStorageManager(int currentLevel, Set<ShipResource> storedResources) {
        int currentLevelIndex = currentLevel - 1;
        if (currentLevelIndex < 0) {
            throw new InvalidLevelException("Level index can't be lower than 0");
        } else if (currentLevelIndex > MAX_LEVEL_INDEX) {
            throw new InvalidLevelException("Level index can't be higher than %d".formatted(MAX_LEVEL_INDEX));
        }
        this.currentLevelIndex = currentLevelIndex;
        if (storedResources.stream().mapToInt(ShipResource::getQuantity).sum() > LEVELS.get(currentLevelIndex).effect()) {
            throw new IllegalArgumentException("Stored resources can't exceed %d at this level".formatted(LEVELS.get(currentLevelIndex).effect()));
        }
        this.storedResources = storedResources;
    }

    @Override
    public Map<ResourceType, Integer> getUpgradeCost() throws UpgradeNotAvailableException {
        if (getCurrentLevel() == LEVELS.size()) {
            throw new UpgradeNotAvailableException("Already at max level");
        } else {
            return LEVELS.get(currentLevelIndex + 1).cost();
        }
    }

    @Override
    public void upgrade() {
        if (getCurrentLevel() < LEVELS.size()) {
            currentLevelIndex++;
        }
    }

    @Override
    public int getCurrentLevel() {
        return LEVELS.get(currentLevelIndex).level();
    }

    public Map<ResourceType, Integer> getStoredResources() {
        Map<ResourceType, Integer> resources = new HashMap<>();
        storedResources.forEach(shipResource -> resources.put(shipResource.getResourceType(), shipResource.getQuantity()));
        return resources;
    }

    public int getMaxCapacity() {
        return LEVELS.get(currentLevelIndex).effect();
    }

    public int getEmptySpace() {
        int filledSpace = storedResources.stream()
                .mapToInt(ShipResource::getQuantity)
                .sum();
        return getMaxCapacity() - filledSpace;
    }

    public boolean hasResource(ResourceType resourceType, int amount) {
        ShipResource resource = storedResources.stream()
                .filter(sr -> sr.getResourceType() == resourceType)
                .findFirst()
                .orElse(null);
        if (resource == null) {
            return false;
        }
        return resource.getQuantity() >= amount;
    }

    public boolean addResource(ResourceType resourceType, int amount) throws StorageException {
        int emptySpace = getEmptySpace();
        if (emptySpace < amount) {
            throw new StorageException("Not enough free space");
        }
        ShipResource resource = storedResources.stream()
                .filter(sr -> sr.getResourceType() == resourceType)
                .findFirst()
                .orElse(null);
        if (resource != null) {
            resource.setQuantity(resource.getQuantity() + amount);
        } else {
            resource = ShipResource.builder()
                    .resourceType(resourceType)
                    .quantity(amount)
                    .build();
            storedResources.add(resource);
        }
        return true;
    }

    public boolean removeResource(ResourceType resourceType, int amount) throws StorageException {
        ShipResource resource = storedResources.stream()
                .filter(sr -> sr.getResourceType() == resourceType
                        && sr.getQuantity() >= amount)
                .findFirst()
                .orElse(null);
        if (resource == null) {
            throw new StorageException("Not enough resource");
        } else if (resource.getQuantity() == amount) {
            storedResources.remove(resource);
        } else {
            resource.setQuantity(resource.getQuantity() - amount);
        }
        return true;
    }

}
