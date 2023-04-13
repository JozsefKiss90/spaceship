package com.codecool.spaceship.model;

import com.codecool.spaceship.model.ship.SpaceShip;

import java.util.*;

public class Hangar implements Upgradeable {

    private static final List<Map<Resource,Integer>> UPGRADE_NEEDS = new ArrayList<>(){{
        add(null);
        add(new HashMap<>(){{
            put(Resource.METAL,5);
        }});
        add(new HashMap<>(){{
            put(Resource.METAL,20);
            put(Resource.SILICONE,20);
        }});
        add(new HashMap<>(){{
            put(Resource.METAL,100);
            put(Resource.SILICONE,100);
            put(Resource.CRYSTAL,50);
        }});
        add(new HashMap<>(){{
            put(Resource.METAL,500);
            put(Resource.SILICONE,150);
            put(Resource.CRYSTAL,100);
        }});
    }};

    private static final List<Integer> UPGRADE_CAPACITIES = new ArrayList<>() {{
        add(2);
        add(4);
        add(6);
        add(8);
        add(10);
    }};

    private static final int MAX_LEVEL_INDEX = 4;

    private int currentLevelIndex;
    private final Set<SpaceShip> shipSet;

    public Hangar() {
        currentLevelIndex = 0;
        shipSet = new HashSet<>();
    }

    public int getCurrentCapacity() {
        return UPGRADE_CAPACITIES.get(currentLevelIndex);
    }

    private int getCurrentAvailableDocs() {
        return getCurrentCapacity() - shipSet.size();
    }

    public void addShip(SpaceShip ship) throws StorageException {
        if (getCurrentAvailableDocs()>0) {
            shipSet.add(ship);
        } else throw new StorageException();
    }

    //get miner ships

    //get scout ships

    //get fighter ships

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
