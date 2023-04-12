package com.codecool.spaceship.model;

import java.util.*;

public class BaseStorage implements Upgradeable {
    private static final List<Map<Resource, Integer>> UPGRADE_NEEDS = new ArrayList<>() {
        {
            add(null);
            add(new HashMap<>() {{
                put(Resource.METAL, 5);
            }});
            add(new HashMap<>() {{
                put(Resource.METAL, 20);
                put(Resource.SILICONE, 10);
            }});
            add(new HashMap<>() {{
                put(Resource.METAL, 200);
                put(Resource.SILICONE, 100);
            }});
            add(new HashMap<>() {{
                put(Resource.METAL, 400);
                put(Resource.SILICONE, 150);
                put(Resource.PLUTONIUM, 50);
            }});
        }
    };
    private static final List<Integer> UPGRADE_CAPACITIES = new ArrayList<>() {{
        add(20);
        add(50);
        add(100);
        add(500);
        add(1000);
    }};
    private static final int MAX_LEVEL_INDEX = 4;

    private int currentLevelIndex;
    private final Map<Resource, Integer> storedItems;

    public BaseStorage() {
        currentLevelIndex = 0;
        storedItems = new HashMap<>();
    }

    public int getCurrentCapacity() {
        return UPGRADE_CAPACITIES.get(currentLevelIndex);
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
            } else throw new StorageException();
        } else throw new StorageException();
    }

    @Override
    public Map<Resource, Integer> getUpgradeCost() throws UpgradeNotAvailable {
        if (currentLevelIndex < MAX_LEVEL_INDEX) return UPGRADE_NEEDS.get(currentLevelIndex + 1);
        throw new UpgradeNotAvailable();
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
