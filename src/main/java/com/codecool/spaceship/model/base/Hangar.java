package com.codecool.spaceship.model.base;

import com.codecool.spaceship.model.*;
import com.codecool.spaceship.model.exception.StorageException;
import com.codecool.spaceship.model.exception.UpgradeNotAvailableException;
import com.codecool.spaceship.model.ship.MinerShip;
import com.codecool.spaceship.model.ship.SpaceShip;

import java.util.*;
import java.util.stream.Collectors;

public class Hangar implements Upgradeable {

    private static final List<Level<Integer>> UPGRADE_LEVELS = new ArrayList<>() {{
        add(new Level<>(1, 2, null));
        add(new Level<>(2, 4, new HashMap<>() {{
            put(Resource.METAL, 5);
        }}));
        add(new Level<>(3, 6, new HashMap<>() {{
            put(Resource.METAL, 20);
            put(Resource.SILICONE, 20);
        }}));
        add(new Level<>(4, 8, new HashMap<>() {{
            put(Resource.METAL, 100);
            put(Resource.SILICONE, 100);
            put(Resource.CRYSTAL, 50);
        }}));
        add(new Level<>(5, 10, new HashMap<>() {{
            put(Resource.METAL, 500);
            put(Resource.SILICONE, 150);
            put(Resource.CRYSTAL, 100);
        }}));
    }};

    private static final int MAX_LEVEL_INDEX = UPGRADE_LEVELS.size()-1;

    private int currentLevelIndex;
    private final Set<SpaceShip> shipSet;

    public Hangar() {
        currentLevelIndex = 0;
        shipSet = new HashSet<>();
    }

    public int getCurrentCapacity() {
        return UPGRADE_LEVELS.get(currentLevelIndex).effect();
    }

    public int getCurrentAvailableDocks() {
        return getCurrentCapacity() - shipSet.size();
    }

    public void addShip(SpaceShip ship) throws StorageException {
        if (getCurrentAvailableDocks() > 0) {
            shipSet.add(ship);
        } else throw new StorageException("No more docks available");
    }

    public Set<MinerShip> getMinerShips() {
        return shipSet.stream().filter(ship->ship instanceof MinerShip).map(ship->(MinerShip) ship).collect(Collectors.toSet());
    }
    public Set<SpaceShip> getAllShips() {
        return new HashSet<>(shipSet);
    }

    //get scout ships

    //get fighter ships

    @Override
    public Map<Resource, Integer> getUpgradeCost() throws UpgradeNotAvailableException {
        if (currentLevelIndex < MAX_LEVEL_INDEX) return new HashMap<>(UPGRADE_LEVELS.get(currentLevelIndex+1).cost());
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
