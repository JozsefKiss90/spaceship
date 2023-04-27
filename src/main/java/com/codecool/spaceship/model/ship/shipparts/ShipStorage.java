package com.codecool.spaceship.model.ship.shipparts;

import com.codecool.spaceship.model.Level;
import com.codecool.spaceship.model.resource.ResourceType;
import com.codecool.spaceship.model.Upgradeable;
import com.codecool.spaceship.model.exception.StorageException;
import com.codecool.spaceship.model.exception.UpgradeNotAvailableException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShipStorage implements Upgradeable {

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
    private int currentLevelIndex;
    private final Map<ResourceType, Integer> storedResources;

    public ShipStorage() {
        storedResources = new HashMap<>();
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
        return new HashMap<>(storedResources);
    }

    public int getMaxCapacity() {
        return LEVELS.get(currentLevelIndex).effect();
    }

    public int getEmptySpace() {
        int filledSpace = storedResources.values().stream()
                .mapToInt(i -> i)
                .sum();
        return getMaxCapacity() - filledSpace;
    }

    public boolean addResource(ResourceType resourceType, int amount) throws StorageException {
        int emptySpace = getEmptySpace();
        if (emptySpace < amount) {
            throw new StorageException("Not enough free space");
        }
        storedResources.merge(resourceType, amount, (Integer::sum));
        return true;
    }

    public boolean removeResource(ResourceType resourceType, int amount) throws StorageException {
        if (!storedResources.containsKey(resourceType)) {
            throw new StorageException("No such resource stored");
        }
        int storedAmount = storedResources.get(resourceType);
        if (amount > storedAmount) {
            throw new StorageException("Not enough resource");
        }
        storedResources.replace(resourceType, storedAmount - amount);
        storedResources.remove(resourceType, 0);
        return true;
    }

}
