package com.codecool.spaceship.model.station;

import com.codecool.spaceship.model.*;
import com.codecool.spaceship.model.exception.StorageException;
import com.codecool.spaceship.model.exception.UpgradeNotAvailableException;
import com.codecool.spaceship.model.resource.ResourceType;

import java.util.*;

public class SpaceStationStorage implements Upgradeable {
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
            put(ResourceType.METAL, 200);
            put(ResourceType.SILICONE, 100);
        }}));
        add(new Level<>(5, 1000, new HashMap<>() {{
            put(ResourceType.METAL, 400);
            put(ResourceType.SILICONE, 150);
            put(ResourceType.PLUTONIUM, 50);
        }}));
    }};
    private static final int MAX_LEVEL_INDEX = UPGRADE_LEVELS.size() - 1;

    private int currentLevelIndex;
    private final Map<ResourceType, Integer> storedItems;

    public SpaceStationStorage() {
        currentLevelIndex = 0;
        storedItems = new HashMap<>();
    }

    public int getCurrentCapacity() {
        return UPGRADE_LEVELS.get(currentLevelIndex).effect();
    }

    public int getCurrentAvailableStorageSpace() {
        return getCurrentCapacity() - storedItems.values().stream().mapToInt(i -> i).sum();
    }

    public Map<ResourceType, Integer> getStoredItems() {
        return new HashMap<>(storedItems);
    }

    public boolean addResource(ResourceType resourceType, int quantity) throws StorageException {
        if (quantity < getCurrentAvailableStorageSpace()) {
            if (storedItems.containsKey(resourceType)) {
                storedItems.replace(resourceType, storedItems.get(resourceType) + quantity);
            } else {
                storedItems.put(resourceType, quantity);
            }
            return true;
        }
        throw new StorageException("Not enough storage space");
    }

    public boolean hasResource(ResourceType resourceType, int quantity) {
        return storedItems.containsKey(resourceType) && storedItems.get(resourceType) >= quantity;
    }

    public void removeResource(ResourceType resourceType, int quantity) throws StorageException {
        if (storedItems.containsKey(resourceType)) {
            int storedQuantity = storedItems.get(resourceType);
            if (quantity <= storedQuantity) {
                storedItems.replace(resourceType, storedQuantity - quantity);
            } else throw new StorageException("Not enough resource");
        } else throw new StorageException("No such resource stored");
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
