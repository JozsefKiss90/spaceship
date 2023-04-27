package com.codecool.spaceship.model.station;

import com.codecool.spaceship.model.*;
import com.codecool.spaceship.model.exception.StorageException;
import com.codecool.spaceship.model.exception.UpgradeNotAvailableException;
import com.codecool.spaceship.model.resource.ResourceType;
import com.codecool.spaceship.model.ship.SpaceShipManager;

import java.util.*;

public class HangarService implements Upgradeable {

    private static final List<Level<Integer>> UPGRADE_LEVELS = new ArrayList<>() {{
        add(new Level<>(1, 2, null));
        add(new Level<>(2, 4, new HashMap<>() {{
            put(ResourceType.METAL, 5);
        }}));
        add(new Level<>(3, 6, new HashMap<>() {{
            put(ResourceType.METAL, 20);
            put(ResourceType.SILICONE, 20);
        }}));
        add(new Level<>(4, 8, new HashMap<>() {{
            put(ResourceType.METAL, 100);
            put(ResourceType.SILICONE, 100);
            put(ResourceType.CRYSTAL, 50);
        }}));
        add(new Level<>(5, 10, new HashMap<>() {{
            put(ResourceType.METAL, 500);
            put(ResourceType.SILICONE, 150);
            put(ResourceType.CRYSTAL, 100);
        }}));
    }};

    private static final int MAX_LEVEL_INDEX = UPGRADE_LEVELS.size() - 1;

    private int currentLevelIndex;
    private final Set<SpaceShipManager> shipSet;

    public HangarService() {
        currentLevelIndex = 0;
        shipSet = new HashSet<>();
    }

    public int getCurrentCapacity() {
        return UPGRADE_LEVELS.get(currentLevelIndex).effect();
    }

    public int getCurrentAvailableDocks() {
        return getCurrentCapacity() - shipSet.size();
    }

    public boolean addShip(SpaceShipManager ship) throws StorageException {
        if (getCurrentAvailableDocks() > 0) {
            return shipSet.add(ship);
        } throw new StorageException("No more docks available");
    }

    public boolean removeShip(SpaceShipManager ship) {
        return shipSet.contains(ship);
    }

    public Set<SpaceShipManager> getAllShips() {
        return new HashSet<>(shipSet);
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
