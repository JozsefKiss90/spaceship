package com.codecool.spaceship.model;

import java.util.*;

public class BaseStorage implements Upgradeable {
    private static final List<Level<Integer>> UPGRADE_LEVELS = new ArrayList<>() {{
        add(new Level<>(1, 20, null));
        add(new Level<>(2, 50, new HashMap<>() {{
            put(Resource.METAL, 5);
        }}));
        add(new Level<>(3, 100, new HashMap<>() {{
            put(Resource.METAL, 20);
            put(Resource.SILICONE, 10);
        }}));
        add(new Level<>(4, 500, new HashMap<>() {{
            put(Resource.METAL, 200);
            put(Resource.SILICONE, 100);
        }}));
        add(new Level<>(5, 1000, new HashMap<>() {{
            put(Resource.METAL, 400);
            put(Resource.SILICONE, 150);
            put(Resource.PLUTONIUM, 50);
        }}));
    }};
    private static final int MAX_LEVEL_INDEX = UPGRADE_LEVELS.size() - 1;

    private int currentLevelIndex;
    private final Map<Resource, Integer> storedItems;

    public BaseStorage() {
        currentLevelIndex = 0;
        storedItems = new HashMap<>();
    }

    public int getCurrentCapacity() {
        return UPGRADE_LEVELS.get(currentLevelIndex).effect();
    }

    private int getCurrentAvailableStorageSpace() {
        return getCurrentCapacity() - storedItems.values().stream().mapToInt(i -> i).sum();
    }

    public Map<Resource, Integer> getStoredItems() {
        return new HashMap<>(storedItems);
    }

    public void addResource(Resource resource, int quantity) throws StorageException {
        if (quantity < getCurrentAvailableStorageSpace()) {
            if (storedItems.containsKey(resource)) {
                storedItems.replace(resource, storedItems.get(resource) + quantity);
            } else {
                storedItems.put(resource, quantity);
            }
        } else {
            throw new StorageException();
        }
    }

    public boolean hasResource(Resource resource, int quantity) {
        return storedItems.containsKey(resource) && storedItems.get(resource) >= quantity;
    }

    public void removeResource(Resource resource, int quantity) throws StorageException {
        if (storedItems.containsKey(resource)) {
            int storedQuantity = storedItems.get(resource);
            if (quantity <= storedQuantity) {
                storedItems.replace(resource, storedQuantity - quantity);
            } else throw new StorageException("Not enough resource");
        } else throw new StorageException("No such resource stored");
    }

    @Override
    public Map<Resource, Integer> getUpgradeCost() throws UpgradeNotAvailableException {
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
